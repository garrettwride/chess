package dataAccess;

import model.GameData;
import chess.ChessGame;
import com.google.gson.Gson;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDataAccess {
    private static final String TABLE_NAME = "game_data";

    public void addGame(GameData gameData) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO " + TABLE_NAME + "(whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, gameData.getWhiteUsername());
            preparedStatement.setString(2, gameData.getBlackUsername());
            preparedStatement.setString(3, gameData.getGameName());
            preparedStatement.setString(4, gameData.toString()); // Convert ChessGame to JSON string
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int gameId = generatedKeys.getInt(1);
                    gameData.setGameID(gameId);
                } else {
                    throw new SQLException("Failed to retrieve generated gameID.");
                }
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public GameData getGame(int gameID) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM " + TABLE_NAME + " WHERE gameID = ?")) {
            preparedStatement.setInt(1, gameID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractGameDataFromResultSet(resultSet);
                }
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<GameData> getAllGames() throws SQLException {
        List<GameData> games = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM " + TABLE_NAME);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                games.add(extractGameDataFromResultSet(resultSet));
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return games;
    }

    public void updateGame(GameData gameData) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE " + TABLE_NAME + " SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?")) {
            preparedStatement.setString(1, gameData.getWhiteUsername());
            preparedStatement.setString(2, gameData.getBlackUsername());
            preparedStatement.setString(3, gameData.getGameName());
            preparedStatement.setString(4, gameData.toString()); // Convert ChessGame to JSON string
            preparedStatement.setInt(5, gameData.getGameID());
            preparedStatement.executeUpdate();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear() throws SQLException, DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM " + TABLE_NAME);
        }
    }

    private GameData extractGameDataFromResultSet(ResultSet resultSet) throws SQLException {
        int gameID = resultSet.getInt("gameID");
        String whiteUsername = resultSet.getString("whiteUsername");
        String blackUsername = resultSet.getString("blackUsername");
        String gameName = resultSet.getString("gameName");
        ChessGame game = new Gson().fromJson(resultSet.getString("game"), ChessGame.class); // Deserialize JSON string to ChessGame
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
}


