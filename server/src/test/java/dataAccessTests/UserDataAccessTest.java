package dataAccessTests;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import dataAccess.UserDataAccess;
import model.UserData;
import org.junit.*;
import org.junit.jupiter.api.Test;
import dataAccess.*;
import java.sql.*;

public class UserDataAccessTest {

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
        userDataAccess.addUser(new UserData(username, password));

        // Act
        UserData userData = userDataAccess.getUser(username);

        // Assert
        assertNotNull(userData);
        assertEquals(username, userData.getUsername());
        assertEquals(password, userData.getPassword());
    }

    @Test
    public void testGetUser_Negative_UserNotFound() throws Exception {
        // Arrange
        String username = "nonexistentUser";

        // Act
        UserData userData = userDataAccess.getUser(username);

        // Assert
        assertNull(userData);
    }
}
