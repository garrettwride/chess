package dataAccess;

import model.UserData;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class UserDataAccess {
    // Method to add a new user
    public void addUser(UserData user) throws DataAccessException {
        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            String hashedPassword = hashPassword(user.getPassword());
            statement.setString(1, user.getUsername());
            statement.setString(2, hashedPassword);
            statement.setString(3, user.getEmail());
            statement.executeUpdate();
        } catch (SQLException | NoSuchAlgorithmException e) {
            throw new DataAccessException("Error adding user: " + e.getMessage());
        }
    }


    // Method to retrieve a user by username
    public UserData getUser(String username) throws DataAccessException {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(username, password, email);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving user: " + e.getMessage());
        }
        return null; // Return null if user not found
    }

    public void clear() throws DataAccessException {
        String query = "DELETE FROM users";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing users: " + e.getMessage());
        }
    }

    // Method to hash a password using SHA-256 algorithm
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}
