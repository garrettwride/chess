package dataAccessTests;

import static dataAccess.GameDataAccess.getGame;
import static org.junit.jupiter.api.Assertions.*;

import chess.ChessGame;
import dataAccess.DatabaseManager;
import dataAccess.GameDataAccess;
import model.GameData;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;


public class GameDataAccessTests {

    private GameDataAccess gameDataAccess;
    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        DatabaseManager.dropDatabase();
        DatabaseManager.createDatabase();
        gameDataAccess = new GameDataAccess();
        connection = DatabaseManager.getConnection();
        connection.setAutoCommit(false);
    }

    @AfterEach
    public void tearDown() throws Exception {
        connection.rollback();
        connection.close();
    }

    @Test
    public void testAddGame_Positive() throws Exception {
        // Arrange
        int gameID = 1;
        String whiteUsername = "whitePlayer";
        String blackUsername = "blackPlayer";
        String gameName = "testGame";
        ChessGame game = new ChessGame(); // Create a new ChessGame object
        GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);

        // Act
        gameDataAccess.addGame(gameData);
        GameData retrievedGame = getGame(gameID);

        // Assert
        assertNotNull(retrievedGame);
        assertEquals(gameID, retrievedGame.getGameID());
        assertEquals(whiteUsername, retrievedGame.getWhiteUsername());
        assertEquals(blackUsername, retrievedGame.getBlackUsername());
        assertEquals(gameName, retrievedGame.getGameName());
        assertNotNull(retrievedGame.getGame());
    }

    @Test
    public void testGetGame_Positive() throws Exception {
        // Arrange
        int gameID = 2;
        String whiteUsername = "user1";
        String blackUsername = "user2";
        String gameName = "game1";
        ChessGame game = new ChessGame(); // Create a new ChessGame object
        GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        gameDataAccess.addGame(gameData);

        // Act
        GameData retrievedGame = getGame(gameID);

        // Assert
        assertNotNull(retrievedGame);
        assertEquals(gameID, retrievedGame.getGameID());
        assertEquals(whiteUsername, retrievedGame.getWhiteUsername());
        assertEquals(blackUsername, retrievedGame.getBlackUsername());
        assertEquals(gameName, retrievedGame.getGameName());
        assertNotNull(retrievedGame.getGame());
    }

    @Test
    public void testClear() throws Exception {
        // Arrange
        int gameID = 3;
        String whiteUsername = "user3";
        String blackUsername = "user4";
        String gameName = "game2";
        ChessGame game = new ChessGame(); // Create a new ChessGame object
        GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        gameDataAccess.addGame(gameData);

        // Act
        gameDataAccess.clear();
        GameData retrievedGame = getGame(gameID);

        // Assert
        assertNull(retrievedGame);
    }

    @Test
    public void testGetAllGames_Positive() throws Exception {
        // Positive test for getting all games
        // Arrange
        int gameID1 = 1;
        String whiteUsername1 = "user1";
        String blackUsername1 = "user2";
        String gameName1 = "game1";
        ChessGame game1 = new ChessGame(); // Create a new ChessGame object
        GameData gameData1 = new GameData(gameID1, whiteUsername1, blackUsername1, gameName1, game1);
        gameDataAccess.addGame(gameData1);

        int gameID2 = 2;
        String whiteUsername2 = "user3";
        String blackUsername2 = "user4";
        String gameName2 = "game2";
        ChessGame game2 = new ChessGame(); // Create another ChessGame object
        GameData gameData2 = new GameData(gameID2, whiteUsername2, blackUsername2, gameName2, game2);
        gameDataAccess.addGame(gameData2);

        // Act
        List<GameData> games = gameDataAccess.getAllGames();

        // Assert
        assertEquals(2, games.size());
        // Additional assertions for each game data object in the list
        // You can compare the properties of each game data object retrieved from the database
    }

    @Test
    public void testGetAllGames_Negative() throws Exception {
        // Negative test for getting all games when there are no games in the database
        // Act
        List<GameData> games = gameDataAccess.getAllGames();

        // Assert
        assertTrue(games.isEmpty());
    }

    @Test
    public void testUpdateGame_Positive() throws Exception {
        // Positive test for updating a game
        // Arrange
        int gameID = 1;
        String newUsername = "newUser";
        String teamColor = "WHITE";

        // Add a game to the database
        GameData gameData = new GameData(gameID, "oldUser", "oldUser", "oldGame", new ChessGame());
        gameDataAccess.addGame(gameData);

        // Act
        gameDataAccess.updateGame(gameID, newUsername, teamColor);

        // Assert
        GameData updatedGameData = getGame(gameID);
        assertEquals(newUsername, updatedGameData.getWhiteUsername());
    }

    @Test()
    public void testUpdateGame_Negative_InvalidTeamColor() throws Exception {
        // Negative test for updating a game with invalid team color
        // Arrange
        int gameID = 1;
        String newUsername = "newUser";
        String teamColor = "INVALID_COLOR";

        // Add a game to the database
        GameData gameData = new GameData(gameID, "oldUser", "oldUser", "oldGame", new ChessGame());
        gameDataAccess.addGame(gameData);

        // Act
        assertThrows(IllegalArgumentException.class, ()->gameDataAccess.updateGame(gameID, newUsername, teamColor));

        // Assert (Exception expected)
    }

}

