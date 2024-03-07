package dataAccessTests;

import static org.junit.jupiter.api.Assertions.*;

import dataAccess.UserDataAccess;
import model.UserData;
import org.junit.jupiter.api.*;
import dataAccess.*;
import java.sql.*;

public class UserDataAccessTests {

    private UserDataAccess userDataAccess;
    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        DatabaseManager.createDatabase();
        userDataAccess = new UserDataAccess();
        connection = DatabaseManager.getConnection();
        connection.setAutoCommit(false);
    }

    @AfterEach
    public void tearDown() throws Exception {
        connection.rollback();
        connection.close();
    }

    @Test
    public void testGetUser_Positive() throws Exception {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        String email = "test@email";
        UserData userData = new UserData(username, password, email);

        // Act
        userDataAccess.addUser(userData);
        UserData retrievedUser = getUserFromDatabase(username);

        // Assert
        assertNotNull(retrievedUser);
        assertEquals(username, retrievedUser.getUsername());
        assertEquals(email, retrievedUser.getEmail());
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
        String email = "test@email";
        UserData userData = new UserData(username, password, email);

        // Act
        userDataAccess.addUser(userData);
        UserData retrievedUser = getUserFromDatabase(username);

        // Assert
        assertNotNull(retrievedUser);
        assertEquals(username, retrievedUser.getUsername());
        assertEquals(email, retrievedUser.getEmail());
    }

    @Test
    public void testAddUser_Negative_UserAlreadyExists() throws Exception {
        // Arrange
        String username = "existingUser";
        String password = "existingPassword";
        String email = "test@email";
        UserData existingUser = new UserData(username, password, email);
        userDataAccess.addUser(existingUser); // Add the user once

        // Act
        assertThrows(DataAccessException.class, ()-> userDataAccess.addUser(existingUser)); // Try to add the same user again

        // Assert
        // Expects DataAccessException to be thrown
    }


    @Test
    public void testClear() throws Exception {
        // Arrange
        String username = "userToClear";
        String password = "passwordToClear";
        String email = "test@email";
        UserData userData = new UserData(username, password, email);
        userDataAccess.addUser(userData);

        // Act
        userDataAccess.clear();
        UserData retrievedUser = getUserFromDatabase(username);

        // Assert
        assertNull(retrievedUser);
    }

    private void addUserToDatabase(String username, String password, String email) throws SQLException {
        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
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
                    String email = rs.getString("email");
                    return new UserData(username, password, email);
                }
            }
        }
        return null;
    }

}
