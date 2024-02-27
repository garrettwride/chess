package service;

import dataAccess.AuthDataAccess;
import dataAccess.GameDataAccess;
import dataModels.Game;
import java.util.List;
import java.util.Random;

public class JoinGameService {
    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;

    public JoinGameService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
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
    public List<Game> listGames(String authToken) throws AuthenticationException {
        // Check if the authToken is valid
        String username = authDataAccess.getUsername(authToken);

        if (username == null) {
            throw new AuthenticationException("Error: Unauthorized");
        } else {
            // delete an authToken
            authDataAccess.deleteAuthToken(username);
            return gameDataAccess.getAllGames();
        }
    }

    // Method to generate a unique game ID (you can implement this logic)
    private int generateUniqueGameID() {
            Random random = new Random();
            // Generate a random integer between 1 and 1000
            int randomNumber = random.nextInt(1000) + 1;
            return randomNumber;

    }
}

