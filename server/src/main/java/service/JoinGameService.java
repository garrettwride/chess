package service;

import dataAccess.GameDataAccess;
import dataModels.Game;
import java.util.List;

public class JoinGameService {
    private final GameDataAccess gameDataAccess;

    public JoinGameService(GameDataAccess gameDataAccess) {
        this.gameDataAccess = gameDataAccess;
    }

    // Method to create a new game
    public int createGame(String gameName, String username) {
        // Generate a unique gameID (you can implement this logic)
        int gameID = generateUniqueGameID();

        // Create a new game object
        Game game = new Game(gameID, username, null, gameName, null);

        // Add the game to the data store
        gameDataAccess.addGame(game);

        return gameID;
    }

    // Method to join an existing game
    public boolean joinGame(int gameID, String username) {
        // Retrieve the game by gameID
        Game game = gameDataAccess.getGame(gameID);

        // Check if the game exists and has an empty slot for black player
        if (game != null && game.getBlackUsername() == null) {
            // Update the game with black player username
            gameDataAccess.updateGame(gameID, username);
            return true;
        } else {
            return false; // Game not found or already full
        }
    }

    // Method to list available games
    public List<Game> listGames() {
        return gameDataAccess.getAllGames();
    }

    // Method to generate a unique game ID (you can implement this logic)
    private int generateUniqueGameID() {
        // Your implementation here
        return 0;
    }
}

