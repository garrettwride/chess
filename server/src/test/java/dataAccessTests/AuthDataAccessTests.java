package dataAccessTests;

import static org.junit.jupiter.api.Assertions.*;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.AuthDataAccess;
import model.AuthData;
import org.junit.jupiter.api.*;
import java.sql.*;

public class AuthDataAccessTests {

    private AuthDataAccess authDataAccess;
    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        DatabaseManager.dropDatabase();
        DatabaseManager.createDatabase();
        authDataAccess = new AuthDataAccess();
        connection = DatabaseManager.getConnection();
        connection.setAutoCommit(false);
    }

    @AfterEach
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

    @Test
    public void testAddAuthToken_Negative_EmptyUsername() throws Exception {
        // Negative test for adding an auth token with an empty username
        // Arrange
        String emptyUsername = "";

        // Act
         assertThrows(DataAccessException.class, ()->authDataAccess.addAuthToken(emptyUsername));

    }



    @Test
    public void testGetAuthToken_Negative_AuthTokenNotFound() throws Exception {
        // Negative test for retrieving an auth token with non-existent auth token
        // Arrange
        String authToken = "nonExistentToken";

        // Act
        String retrievedAuthToken = authDataAccess.getAuthToken(authToken);

        // Assert
        assertNull(retrievedAuthToken);
    }

    @Test
    public void testGetAuthData_Negative_AuthDataNotFound() throws Exception {
        // Negative test for retrieving auth data with non-existent auth token
        // Arrange
        String authToken = "nonExistentToken";

        // Act
        AuthData retrievedAuthData = authDataAccess.getAuthData(authToken);

        // Assert
        assertNull(retrievedAuthData);
    }


    @Test
    public void testGetUsername_Positive() throws Exception {
        // Positive test for retrieving username using auth token
        // Arrange
        String username = "testUser";
        String authToken = authDataAccess.addAuthToken(username);

        // Act
        String retrievedUsername = authDataAccess.getUsername(authToken);

        // Assert
        assertEquals(username, retrievedUsername);
    }

    @Test
    public void testGetUsername_Negative_AuthTokenNotFound() throws Exception {
        // Negative test for retrieving username with non-existent auth token
        // Arrange
        String authToken = "nonExistentToken";

        // Act
        String username = authDataAccess.getUsername(authToken);

        // Assert
        assertNull(username);
    }




    @Test
    public void testDeleteAuthToken_Positive() throws Exception {
        // Positive test for deleting auth token
        // Arrange
        String username = "testUser";
        String authToken = authDataAccess.addAuthToken(username);

        // Act
        authDataAccess.deleteAuthToken(authToken);

        // Assert
        assertNull(authDataAccess.getUsername(authToken));
    }

    @Test
    public void testDeleteAuthToken_Negative_UserNotFound() throws Exception {
        // Negative test for deleting auth token with non-existent user
        // Arrange
        String username = "nonExistentUser";

        // Act
        assertThrows(DataAccessException.class, ()->authDataAccess.deleteAuthToken(username));

        // Assert
        // Expects DataAccessException to be thrown
    }

    @Test
    public void testClear_Positive() throws Exception {
        // Positive test for clearing auth tokens
        // Arrange
        String username1 = "testUser1";
        String authToken1 = "testToken1";
        authDataAccess.addAuthToken(username1);

        String username2 = "testUser2";
        String authToken2 = "testToken2";
        authDataAccess.addAuthToken(username2);

        // Act
        authDataAccess.clear();

        // Assert
        assertNull(authDataAccess.getUsername(authToken1));
        assertNull(authDataAccess.getUsername(authToken2));
    }

}

