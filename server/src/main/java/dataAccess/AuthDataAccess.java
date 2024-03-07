package dataAccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthDataAccess {
    // Method to add a new authToken for a user
    public String addAuthToken(String username) throws DataAccessException {
        String authToken = createAuthToken();
        String query = "INSERT INTO auth_tokens (username, authToken) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, authToken);
            statement.executeUpdate();
            return authToken;
        } catch (SQLException e) {
            throw new DataAccessException("Error adding authToken: " + e.getMessage());
        }
    }

    // Method to retrieve authToken by token
    public String getAuthToken(String authToken) throws DataAccessException {
        String query = "SELECT authToken FROM auth_tokens WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, authToken);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("authToken");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving authToken: " + e.getMessage());
        }
        return null; // Return null if authToken not found
    }

    // Method to retrieve AuthData by token
    public AuthData getAuthData(String authToken) throws DataAccessException {
        String query = "SELECT * FROM auth_tokens WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, authToken);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    return new AuthData(username, authToken);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving AuthData: " + e.getMessage());
        }
        return null; // Return null if AuthData not found
    }

    // Method to retrieve username by token
    public String getUsername(String authToken) throws DataAccessException {
        String query = "SELECT username FROM auth_tokens WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, authToken);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving username: " + e.getMessage());
        }
        return null; // Return null if username not found
    }

    // Method to delete authToken by username
    public void deleteAuthToken(String username) throws DataAccessException {
        String query = "DELETE FROM auth_tokens WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting authToken: " + e.getMessage());
        }
    }

    // Method to clear authTokens
    public void clear() throws DataAccessException {
        String query = "DELETE FROM auth_tokens";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing authTokens: " + e.getMessage());
        }
    }

    // Method to create a new authToken
    private String createAuthToken() {
        return UUID.randomUUID().toString();
    }
}

