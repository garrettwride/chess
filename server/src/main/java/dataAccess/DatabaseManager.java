package dataAccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to laod db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
        try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {
            // Check if the database exists and drop it if it does

            // Create the database
            String sql = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.executeUpdate();
            }

            String sql2 = "USE " + databaseName;
            try (PreparedStatement statement = connection.prepareStatement(sql2)) {
                statement.executeUpdate();
            }

            // Create tables in the newly created database
            createUsersTable(connection);
            createAuthTokensTable(connection);
            createGamesTable(connection);
        } catch (SQLException e) {
            throw new DataAccessException("Error creating database: " + e.getMessage());
        }
    }

    public static void dropDatabase() throws SQLException {
        Connection connection = DriverManager.getConnection(connectionUrl, user, password);
        String sql = "DROP DATABASE IF EXISTS " + databaseName; // Add IF EXISTS to prevent error if database doesn't exist
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }


    private static void createUsersTable(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                `id` INT AUTO_INCREMENT PRIMARY KEY,
                `username` VARCHAR(255) UNIQUE NOT NULL,
                `password` VARCHAR(255) NOT NULL,
                `email` VARCHAR(255) NOT NULL
                )
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

    private static void createAuthTokensTable(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS auth_tokens (
                    `id` INT AUTO_INCREMENT PRIMARY KEY,
                    `username` VARCHAR(255) NOT NULL,
                    `auth_token` VARCHAR(255) UNIQUE NOT NULL
                )
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

    private static void createGamesTable(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS games (
                `id` INT PRIMARY KEY,
                `game_name` VARCHAR(255) NOT NULL,
                `white_player` VARCHAR(255),
                `black_player` VARCHAR(255),
                `game` TEXT
                )
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
//    public static Connection getConnection() throws DataAccessException {
//        try {
//            var conn = DriverManager.getConnection(connectionUrl, user, password);
//            conn.setCatalog(databaseName);
//            createDatabase();
//            return conn;
//        } catch (SQLException e) {
//            throw new DataAccessException(e.getMessage());
//        }
//    }

    public static Connection getConnection() throws DataAccessException {
        Connection conn = null;
        try {
            // Attempt to establish a connection
            conn = DriverManager.getConnection(connectionUrl, user, password);

            // Check if the connection is valid
            if (conn != null && !conn.isClosed()) {
            } else {
                System.err.println("Failed to establish connection or connection is closed.");
                // You can throw an exception here or handle the error accordingly
                // For simplicity, let's throw an exception
                throw new DataAccessException("Failed to establish connection or connection is closed.");
            }

            // Set the catalog based on the database name
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            // Handle connection exception
            throw new DataAccessException("Error establishing connection: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "DatabaseManager{}";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
