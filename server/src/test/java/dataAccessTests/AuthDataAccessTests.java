package dataAccessTests;

import static org.junit.Assert.*;
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
}

