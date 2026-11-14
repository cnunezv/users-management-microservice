package com.jcaa.usersmanagement.infrastructure.config;

import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Migración de la base de datos al arrancar la aplicación.
 *
 * <p>Se activa con {@code DB_MIGRATE_ON_STARTUP=true}. Aplica {@code schema.sql} sobre la base ya
 * configurada, adaptándolo a un MySQL administrado en la nube:
 *
 * <ul>
 *   <li>Omite {@code CREATE DATABASE} y {@code USE}: en la nube la base ya existe, tiene otro
 *       nombre y el usuario no puede crear bases.
 *   <li>El script usa {@code CREATE TABLE IF NOT EXISTS} e {@code INSERT IGNORE}, así que es
 *       idempotente y se puede ejecutar en cada arranque.
 * </ul>
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "db.migrate-on-startup", havingValue = "true")
public class DatabaseMigrationRunner implements ApplicationRunner {

  static final String SCHEMA_LOCATION = "schema.sql";

  private static final Pattern STATEMENT_SEPARATOR = Pattern.compile(";\\s*$", Pattern.MULTILINE);
  private static final Pattern LINE_COMMENT = Pattern.compile("(?m)^\\s*--.*$");
  private static final List<String> SKIPPED_PREFIXES = List.of("CREATE DATABASE", "USE ");

  private final DataSource dataSource;

  @Override
  public void run(final ApplicationArguments args) {
    migrate(new ClassPathResource(SCHEMA_LOCATION));
  }

  /** Ejecuta las sentencias del script y devuelve cuántas se aplicaron. */
  int migrate(final Resource script) {
    final List<String> statements = splitStatements(read(script));
    int executed = 0;
    try (final Connection connection = dataSource.getConnection();
        final Statement statement = connection.createStatement()) {
      for (final String sql : statements) {
        if (isSkipped(sql)) {
          continue;
        }
        statement.execute(sql);
        executed++;
      }
    } catch (final SQLException exception) {
      throw PersistenceException.becauseMigrationFailed(exception);
    }
    log.info("[DatabaseMigrationRunner] Migración aplicada: {} sentencias ejecutadas.", executed);
    return executed;
  }

  static List<String> splitStatements(final String script) {
    final String withoutComments = LINE_COMMENT.matcher(script).replaceAll("");
    return Arrays.stream(STATEMENT_SEPARATOR.split(withoutComments))
        .map(String::trim)
        .filter(sql -> !sql.isEmpty())
        .toList();
  }

  static boolean isSkipped(final String sql) {
    final String normalized = sql.toUpperCase(Locale.ROOT);
    return SKIPPED_PREFIXES.stream().anyMatch(normalized::startsWith);
  }

  private static String read(final Resource script) {
    try (final InputStream input = script.getInputStream()) {
      return new String(input.readAllBytes(), StandardCharsets.UTF_8);
    } catch (final IOException exception) {
      throw ConfigurationException.becauseLoadFailed(exception);
    }
  }
}
