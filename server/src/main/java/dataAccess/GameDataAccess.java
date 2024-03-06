package dataAccess;

import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameDataAccess {
    // Method to add a new game
    public void addGame(GameData game) throws DataAccessException {
        String query = "INSERT INTO game_data (gameID, whiteUsername, blackUsername) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, game.getGameID());
            statement.setString(2, game.getWhiteUsername());
            statement.setString(3, game.getBlackUsername());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error adding game: " + e.getMessage());
        }
    }

    // Method to get a game by gameID
    public GameData getGame(int gameID) throws DataAccessException {
        String query = "SELECT * FROM game_data WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, gameID);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    return new GameData(gameID, whiteUsername, blackUsername);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving game: " + e.getMessage());
        }
        return null; // Return null if game not found
    }

    // Method to update a game
    public void updateGame(int gameID, String username, String teamColor) throws DataAccessException {
        GameData updatedGame = getGame(gameID);
        if (teamColor.equalsIgnoreCase("WHITE")) {
            updatedGame.setWhiteUsername(username);
        } else if (teamColor.equalsIgnoreCase("BLACK")) {
            updatedGame.setBlackUsername(username);
        } else {
            throw new IllegalArgumentException("Error: Invalid team color");
        }
        String query = "UPDATE game_data SET whiteUsername = ?, blackUsername = ? WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, updatedGame.getWhiteUsername());
            statement.setString(2, updatedGame.getBlackUsername());
            statement.setInt(3, gameID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating game: " + e.getMessage());
        }
    }

    // Method to retrieve all games
    public List<GameData> getAllGames() throws DataAccessException {
        List<GameData> games = new ArrayList<>();
        String query = "SELECT * FROM game_data";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int gameID = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                games.add(new GameData(gameID, whiteUsername, blackUsername));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving all games: " + e.getMessage());
        }
        return games;
    }

    // Method to clear games
    public void clear() throws DataAccessException {
        String query = "DELETE FROM game_data";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing games: " + e.getMessage());
        }
    }
}

