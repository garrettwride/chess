package dataAccessTests;

import static org.junit.Assert.*;

import dataAccess.DatabaseManager;
import dataAccess.GameDataAccess;
import org.junit.*;
import java.sql.*;


public class GameDataAccessTests {

    private GameDataAccess gameDataAccess;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        gameDataAccess = new GameDataAccess();
        connection = DatabaseManager.getConnection();
        connection.setAutoCommit(false);
    }

    @After
    public void tearDown() throws Exception {
        connection.rollback();
        connection.close();
    }
