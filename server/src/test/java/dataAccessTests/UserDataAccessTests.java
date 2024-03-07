package dataAccessTests;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import dataAccess.UserDataAccess;
import model.UserData;
import org.junit.*;
import org.junit.Test;
import dataAccess.*;
import java.sql.*;

public class UserDataAccessTests {

    private UserDataAccess userDataAccess;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        userDataAccess = new UserDataAccess();
        connection = DatabaseManager.getConnection();
        connection.setAutoCommit(false);
    }

    @After
    public void tearDown() throws Exception {
        connection.rollback();
        connection.close();
    }

    @Test
    public void testGetUser_Positive() throws Exception {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        addUserToDatabase(username, password);

        // Act
        UserData retrievedUser = userDataAccess.getUser(username);

        // Assert
        assertNotNull(retrievedUser);
        assertEquals(username, retrievedUser.getUsername());
        assertEquals(password, retrievedUser.getPassword());
    }

    @Test
    public void testGetUser_Negative_UserNotFound() throws Exception {
        // Arrange
        String username = "nonExistingUser";

        // Act
        UserData retrievedUser = userDataAccess.getUser(username);

        // Assert
        assertNull(retrievedUser);
    }

    @Test
    public void testAddUser_Positive() throws Exception {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        UserData userData = new UserData(username, password);

        // Act
        userDataAccess.addUser(userData);
        UserData retrievedUser = getUserFromDatabase(username);

        // Assert
        assertNotNull(retrievedUser);
        assertEquals(username, retrievedUser.getUsername());
        assertEquals(password, retrievedUser.getPassword());
    }

    @Test
    public void testAddUser_Negative_UserAlreadyExists() throws Exception {
        // Arrange
        String username = "existingUser";
        String password = "existingPassword";
        UserData existingUser = new UserData(username, password);
        userDataAccess.addUser(existingUser); // Add the user once

        userDataAccess.addUser(existingUser); // Try to add the same user again
    }

    @Test
    public void testClear() throws Exception {
        // Arrange
        String username = "userToClear";
        String password = "passwordToClear";
        UserData userData = new UserData(username, password);
        userDataAccess.addUser(userData);

        // Act
        userDataAccess.clear();
        UserData retrievedUser = getUserFromDatabase(username);

        // Assert
        assertNull(retrievedUser);
    }

    private void addUserToDatabase(String username, String password) throws SQLException {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        }
    }

    private UserData getUserFromDatabase(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");
                    return new UserData(username, password);
                }
            }
        }
        return null;
    }
}
