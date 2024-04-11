package service;

import chess.ChessGame;
import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import model.GameData;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class JoinGameService {
    private static GameDataAccess gameDataAccess = null;
    private final AuthDataAccess authDataAccess;

    public JoinGameService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
    this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    // Method to create a new game
    public int createGame(String gameName, String authToken) throws AuthenticationException, DataAccessException, SQLException {
        String username = authDataAccess.getUsername(authToken);

        if (username == null) {
            throw new AuthenticationException("Error: Unauthorized");
        } else {
            // Generate a unique gameID (you can implement this logic)
            int gameID = generateUniqueGameID();

            //Create new chess game
            ChessGame chessGame = new ChessGame();

            // Create a new game object
            GameData game = new GameData(gameID, null, null, gameName, chessGame);

            // Add the game to the data store
            gameDataAccess.addGame(game);

            return gameID;
        }

    }

    // Method to join an existing game
    public void joinGame(String authToken, String teamColor, int gameID) throws AuthenticationException, DataAccessException, SQLException {
        // Retrieve the game by gameID
        GameData game = gameDataAccess.getGame(gameID);

        // Check if the authToken is valid
        String username = authDataAccess.getUsername(authToken);

        if (username == null) {
            throw new AuthenticationException("Error: Unauthorized");
        } else {
            // Check if the game exists
            if (game != null) {
                // Check if the teamColor is WHITE or BLACK
                if (teamColor.equalsIgnoreCase("WHITE")) {
                    // Check if the whiteUsername is null
                    if (game.getWhiteUsername() == null) {
                        // Update the game with white player username
                        gameDataAccess.updateGame(gameID, username, teamColor);
                    } else {
                        throw new IllegalStateException("Error: already taken");
                    }
                } else if (teamColor.equalsIgnoreCase("BLACK")) {
                    // Check if the blackUsername is null
                    if (game.getBlackUsername() == null) {
                        // Update the game with black player username
                        gameDataAccess.updateGame(gameID, username, teamColor);
                    } else {
                        throw new IllegalStateException("Error: already taken");
                    }
                } else {
                    throw new IllegalArgumentException("Error: Invalid team color");
                }
            } else {
                throw new IllegalArgumentException("Error: Game not found");
            }
        }
    }

    public void observeGame(int gameID, String authToken) throws AuthenticationException, DataAccessException, SQLException {

        // Check if the authToken is valid
        String username = authDataAccess.getUsername(authToken);

        if (username == null) {
            throw new AuthenticationException("Error: Unauthorized");
        }
        // Retrieve the game by gameID
        GameData game = getGame(gameID);
        if (game == null){
            throw new IllegalArgumentException("Error: Game not found");

        }
    }


    // Method to list available games
    public List<GameData> listGames(String authToken) throws AuthenticationException, DataAccessException, SQLException {
        // Check if the authToken is valid
        String username = authDataAccess.getUsername(authToken);

        if (username == null) {
            throw new AuthenticationException("Error: Unauthorized");
        } else {

            return gameDataAccess.getAllGames();
        }
    }

    public void removeFromGame(String authToken, int gameID) throws AuthenticationException, DataAccessException, SQLException {
        GameData game = gameDataAccess.getGame(gameID);

        String username = authDataAccess.getUsername(authToken);

        if (username == null) {
            throw new AuthenticationException("Error: Unauthorized");
        } else {
            if (game != null) {
                if (username.equals(game.getWhiteUsername()) || username.equals(game.getBlackUsername())) {
                    if (username.equals(game.getWhiteUsername())) {
                        gameDataAccess.updateGame(gameID, null, "WHITE");
                    } else {
                        gameDataAccess.updateGame(gameID, null, "BLACK");
                    }
                } else {
                    throw new IllegalStateException("Error: Player not in the game");
                }
            } else {
                throw new IllegalArgumentException("Error: Game not found");
            }
        }
    }

    public static GameData getGame(int gameID) throws SQLException {
        return gameDataAccess.getGame(gameID);
    }

    // Method to generate a unique game ID (you can implement this logic)
    private int generateUniqueGameID() {
            Random random = new Random();
            // Generate a random integer between 1 and 1000
            int randomNumber = random.nextInt(1000) + 1;
            return randomNumber;

    }
}

