package dataAccess;

import model.GameData;
import chess.ChessGame;
import com.google.gson.Gson;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDataAccess {
    private static final String TABLE_NAME = "games";

    // Method to add a new game
    public void addGame(GameData game) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO " + TABLE_NAME + " (id, game_name, white_player, black_player, game) " +
                             "VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, game.getGameID());
            preparedStatement.setString(2, game.getGameName());
            preparedStatement.setString(3, game.getWhiteUsername());
            preparedStatement.setString(4, game.getBlackUsername());

            // Serialize the ChessGame object to JSON string
            String gameJson = new Gson().toJson(game.getGame());
            preparedStatement.setString(5, gameJson);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameData getGame(int gameID) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM " + TABLE_NAME + " WHERE id = ?")) {
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

    public void updateGame(int gameID, String username, String teamColor) throws SQLException, DataAccessException {
        String columnName;
        if (teamColor.equalsIgnoreCase("WHITE")) {
            columnName = "white_player";
        } else if (teamColor.equalsIgnoreCase("BLACK")) {
            columnName = "black_player";
        } else {
            throw new IllegalArgumentException("Error: Invalid team color");
        }

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE " + TABLE_NAME + " SET " + columnName + " = ? WHERE id = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, gameID);
            preparedStatement.executeUpdate();
        }
    }

    public void clear() throws SQLException, DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM " + TABLE_NAME);
        }
    }

    private static GameData extractGameDataFromResultSet(ResultSet resultSet) throws SQLException {
        int gameID = resultSet.getInt("id");
        String whiteUsername = resultSet.getString("white_player");
        String blackUsername = resultSet.getString("black_player");
        String gameName = resultSet.getString("game_name");
        ChessGame game = new Gson().fromJson(resultSet.getString("game"), ChessGame.class); // Deserialize JSON string to ChessGame
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
