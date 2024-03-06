package dataAccessTests;

import static org.junit.Assert.*;

import chess.ChessGame;
import dataAccess.DatabaseManager;
import dataAccess.GameDataAccess;
import model.GameData;
import org.junit.*;
import java.sql.*;
import java.util.List;


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
        GameData retrievedGame = getGameFromDatabase(gameID);

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
        addGameToDatabase(gameData);

        // Act
        GameData retrievedGame = gameDataAccess.getGame(gameID);

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
        addGameToDatabase(gameData);

        // Act
        gameDataAccess.clear();
        GameData retrievedGame = getGameFromDatabase(gameID);

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
        addGameToDatabase(gameData1);

        int gameID2 = 2;
        String whiteUsername2 = "user3";
        String blackUsername2 = "user4";
        String gameName2 = "game2";
        ChessGame game2 = new ChessGame(); // Create another ChessGame object
        GameData gameData2 = new GameData(gameID2, whiteUsername2, blackUsername2, gameName2, game2);
        addGameToDatabase(gameData2);

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
        addGameToDatabase(gameData);

        // Act
        gameDataAccess.updateGame(gameID, newUsername, teamColor);

        // Assert
        GameData updatedGameData = gameDataAccess.getGame(gameID);
        assertEquals(newUsername, teamColor.equalsIgnoreCase("WHITE") ? updatedGameData.getWhiteUsername() : updatedGameData.getBlackUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateGame_Negative_InvalidTeamColor() throws Exception {
        // Negative test for updating a game with invalid team color
        // Arrange
        int gameID = 1;
        String newUsername = "newUser";
        String teamColor = "INVALID_COLOR";

        // Add a game to the database
        GameData gameData = new GameData(gameID, "oldUser", "oldUser", "oldGame", new ChessGame());
        addGameToDatabase(gameData);

        // Act
        gameDataAccess.updateGame(gameID, newUsername, teamColor);

        // Assert (Exception expected)
    }

    private void addGameToDatabase(GameData gameData) throws SQLException {
        String query = "INSERT INTO games (id, game_name, white_player, black_player, game) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, gameData.getGameID());
            statement.setString(2, gameData.getGameName());
            statement.setString(3, gameData.getWhiteUsername());
            statement.setString(4, gameData.getBlackUsername());
            statement.setString(5, ""); // For now, just insert an empty string for the game
            statement.executeUpdate();
        }
    }

    private GameData getGameFromDatabase(int gameID) throws SQLException {
        String query = "SELECT * FROM games WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, gameID);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String whiteUsername = rs.getString("white_player");
                    String blackUsername = rs.getString("black_player");
                    String gameName = rs.getString("game_name");
                    // For now, just return an empty ChessGame object
                    ChessGame game = new ChessGame();
                    return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                }
            }
        }
        return null;
    }
}