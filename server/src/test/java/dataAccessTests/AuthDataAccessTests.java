package dataAccessTests;

import static org.junit.Assert.*;
import dataAccess.DatabaseManager;
import dataAccess.AuthDataAccess;
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

