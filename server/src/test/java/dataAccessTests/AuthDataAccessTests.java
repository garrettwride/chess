package dataAccessTests;

import static org.junit.Assert.*;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.AuthDataAccess;
import model.AuthData;
import org.junit.*;
import java.sql.*;

public class AuthDataAccessTests {

    private AuthDataAccess authDataAccess;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        authDataAccess = new AuthDataAccess();
        connection = DatabaseManager.getConnection();
        connection.setAutoCommit(false);
    }

    @After
    public void tearDown() throws Exception {
        connection.rollback();
        connection.close();
    }

    @Test
    public void testAddAuthToken_Positive() throws Exception {
        // Positive test for adding an auth token
        // Arrange
        String username = "testUser";

        // Act
        String authToken = authDataAccess.addAuthToken(username);

        // Assert
        assertNotNull(authToken);
    }

    @Test
    public void testGetAuthToken_Positive() throws Exception {
        // Positive test for retrieving an auth token
        // Arrange
        String username = "testUser";
        String authToken = authDataAccess.addAuthToken(username);

        // Act
        String retrievedAuthToken = authDataAccess.getAuthToken(authToken);

        // Assert
        assertEquals(authToken, retrievedAuthToken);
    }

    @Test
    public void testGetAuthData_Positive() throws Exception {
        // Positive test for retrieving auth data
        // Arrange
        String username = "testUser";
        String authToken = authDataAccess.addAuthToken(username);

        // Act
        AuthData authData = authDataAccess.getAuthData(authToken);

        // Assert
        assertNotNull(authData);
        assertEquals(username, authData.getUsername());
        assertEquals(authToken, authData.getAuthToken());
    }

    @Test(expected = DataAccessException.class)
    public void testAddAuthToken_Negative_UserNotFound() throws Exception {
        // Negative test for adding an auth token with user not found
        // Arrange
        String username = "nonExistentUser";

        // Act
        authDataAccess.addAuthToken(username);

        // Assert
        // Expects DataAccessException to be thrown
    }

    @Test(expected = DataAccessException.class)
    public void testGetAuthToken_Negative_AuthTokenNotFound() throws Exception {
        // Negative test for retrieving an auth token with non-existent auth token
        // Arrange
        String authToken = "nonExistentToken";

        // Act
        authDataAccess.getAuthToken(authToken);

        // Assert
        // Expects DataAccessException to be thrown
    }

    @Test(expected = DataAccessException.class)
    public void testGetAuthData_Negative_AuthDataNotFound() throws Exception {
        // Negative test for retrieving auth data with non-existent auth token
        // Arrange
        String authToken = "nonExistentToken";

        // Act
        authDataAccess.getAuthData(authToken);

        // Assert
        // Expects DataAccessException to be thrown
    }

}

