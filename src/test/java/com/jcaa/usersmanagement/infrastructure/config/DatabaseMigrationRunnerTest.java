package com.jcaa.usersmanagement.infrastructure.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;

@DisplayName("DatabaseMigrationRunner")
@ExtendWith(MockitoExtension.class)
class DatabaseMigrationRunnerTest {

  private static final String SCRIPT =
      """
      -- comentario que se ignora
      CREATE DATABASE IF NOT EXISTS crud_usuarios;
      USE crud_usuarios;

      CREATE TABLE IF NOT EXISTS demo (
          id VARCHAR(36) NOT NULL PRIMARY KEY
      );

      -- otro comentario
      INSERT IGNORE INTO demo (id) VALUES ('a');
      """;

  @Mock private DataSource dataSource;
  @Mock private Connection connection;
  @Mock private Statement statement;

  private DatabaseMigrationRunner runner;

  @BeforeEach
  void setUp() {
    runner = new DatabaseMigrationRunner(dataSource);
  }

  @Test
  @DisplayName("splitStatements() separa por punto y coma final y quita comentarios")
  void shouldSplitStatements() {
    final List<String> statements = DatabaseMigrationRunner.splitStatements(SCRIPT);

    assertEquals(4, statements.size());
    assertTrue(statements.get(2).startsWith("CREATE TABLE IF NOT EXISTS demo"));
    assertEquals("INSERT IGNORE INTO demo (id) VALUES ('a')", statements.get(3));
  }

  @Test
  @DisplayName("isSkipped() omite CREATE DATABASE y USE sin importar mayúsculas")
  void shouldSkipDatabaseStatements() {
    assertTrue(DatabaseMigrationRunner.isSkipped("CREATE DATABASE IF NOT EXISTS x"));
    assertTrue(DatabaseMigrationRunner.isSkipped("use crud_usuarios"));
    assertFalse(DatabaseMigrationRunner.isSkipped("CREATE TABLE IF NOT EXISTS demo (id INT)"));
    assertFalse(DatabaseMigrationRunner.isSkipped("INSERT IGNORE INTO users VALUES (1)"));
  }

  @Test
  @DisplayName("migrate() ejecuta solo las sentencias de tablas y datos")
  void shouldExecuteTableAndDataStatements() throws SQLException {
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.createStatement()).thenReturn(statement);

    final int executed =
        runner.migrate(new ByteArrayResource(SCRIPT.getBytes(StandardCharsets.UTF_8)));

    assertEquals(2, executed);
    final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(statement, times(2)).execute(captor.capture());
    assertTrue(captor.getAllValues().get(0).startsWith("CREATE TABLE"));
    assertTrue(captor.getAllValues().get(1).startsWith("INSERT IGNORE"));
  }

  @Test
  @DisplayName("migrate() aplica el schema.sql real del proyecto sin CREATE DATABASE ni USE")
  void shouldApplyProjectSchema() throws SQLException {
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.createStatement()).thenReturn(statement);

    final int executed = runner.migrate(new ClassPathResource(DatabaseMigrationRunner.SCHEMA_LOCATION));

    // users, admin, usuarios de prueba, carreras_taxi y carreras de ejemplo
    assertEquals(5, executed);
    verify(statement, never()).execute(org.mockito.ArgumentMatchers.startsWith("USE"));
    verify(statement, never())
        .execute(org.mockito.ArgumentMatchers.startsWith("CREATE DATABASE"));
  }

  @Test
  @DisplayName("migrate() traduce la SQLException a PersistenceException")
  void shouldWrapSqlException() throws SQLException {
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.createStatement()).thenReturn(statement);
    when(statement.execute(anyString())).thenThrow(new SQLException("boom"));

    final ByteArrayResource script =
        new ByteArrayResource(SCRIPT.getBytes(StandardCharsets.UTF_8));
    assertThrows(PersistenceException.class, () -> runner.migrate(script));
  }
}
