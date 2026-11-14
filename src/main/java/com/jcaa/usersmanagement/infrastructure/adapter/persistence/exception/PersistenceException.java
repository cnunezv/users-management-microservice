package com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception;

public final class PersistenceException extends RuntimeException {

  private static final String MESSAGE_SAVE = "Failed to save user with ID: '%s'.";
  private static final String MESSAGE_UPDATE = "Failed to update user with ID: '%s'.";
  private static final String MESSAGE_FIND = "Failed to find user with ID: '%s'.";
  private static final String MESSAGE_EMAIL = "Failed to find user with email: '%s'.";
  private static final String MESSAGE_ALL = "Failed to retrieve all users.";
  private static final String MESSAGE_DELETE = "Failed to delete user with ID: '%s'.";
  private static final String MESSAGE_CONNECTION = "Could not establish database connection.";
  private static final String MESSAGE_CARRERA_SAVE = "Failed to save carrera de taxi with ID: '%s'.";
  private static final String MESSAGE_CARRERA_UPDATE =
      "Failed to update carrera de taxi with ID: '%s'.";
  private static final String MESSAGE_CARRERA_FIND = "Failed to find carrera de taxi with ID: '%s'.";
  private static final String MESSAGE_CARRERA_ALL = "Failed to retrieve all carreras de taxi.";
  private static final String MESSAGE_MIGRATION = "Failed to apply the database migration.";
  private static final String MESSAGE_CARRERA_DELETE =
      "Failed to delete carrera de taxi with ID: '%s'.";

  private PersistenceException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static PersistenceException becauseSaveFailed(final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_SAVE, userId), cause);
  }

  public static PersistenceException becauseUpdateFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_UPDATE, userId), cause);
  }

  public static PersistenceException becauseFindByIdFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_FIND, userId), cause);
  }

  public static PersistenceException becauseFindByEmailFailed(
      final String email, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_EMAIL, email), cause);
  }

  public static PersistenceException becauseFindAllFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_ALL, cause);
  }

  public static PersistenceException becauseDeleteFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_DELETE, userId), cause);
  }

  public static PersistenceException becauseConnectionFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_CONNECTION, cause);
  }

  public static PersistenceException becauseCarreraSaveFailed(
      final String carreraId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_CARRERA_SAVE, carreraId), cause);
  }

  public static PersistenceException becauseCarreraUpdateFailed(
      final String carreraId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_CARRERA_UPDATE, carreraId), cause);
  }

  public static PersistenceException becauseCarreraFindByIdFailed(
      final String carreraId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_CARRERA_FIND, carreraId), cause);
  }

  public static PersistenceException becauseCarreraFindAllFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_CARRERA_ALL, cause);
  }

  public static PersistenceException becauseCarreraDeleteFailed(
      final String carreraId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_CARRERA_DELETE, carreraId), cause);
  }

  public static PersistenceException becauseMigrationFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_MIGRATION, cause);
  }
}
