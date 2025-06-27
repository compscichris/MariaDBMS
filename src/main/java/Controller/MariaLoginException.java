package Controller;

/**
 * MariaLoginException is a custom exception class that handles the
 */
public class MariaLoginException extends Exception{
    public enum Error{
        NO_ERROR("No error detected"),
        JDBC_DRIVER_NOT_FOUND("JDBC driver missing"),
        CREDENTIALS_ERROR("Username or password invalid"),
        SQL_SYNTAX_ERROR("SQL syntax error"),
        DATABASE_CONNECTION_ERROR("Database connection failed"),
        FILE_NOT_FOUND("SQL file not found");
        private final String message;
        Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
    public MariaLoginException(Error errorType)
    {
        super(errorType.getMessage());
    }
    public MariaLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}

