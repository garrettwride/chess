package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import model.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class JoinGameService {
    private static GameDataAccess gameDataAccess = null;
    private static AuthDataAccess authDataAccess;

    public JoinGameService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public static void makeMove(GameData gameData, ChessMove move) throws InvalidMoveException, SQLException {
        ChessGame game = gameData.getGame();
        game.makeMove(move);
        gameData.setGame(game);
        gameDataAccess.updateGameMove(gameData);
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
            chessGame.getBoard().resetBoard();

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

    public static void removeFromGame(String authToken, int gameID) throws AuthenticationException, DataAccessException, SQLException {
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
                }
            } else {
                throw new IllegalArgumentException("Error: Game not found");
            }
        }
    }

    public static void endGame(int gameID) {
        try {
            gameDataAccess.endGame(gameID);
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
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

    public static boolean isValidAuthToken(String authToken) throws SQLException, DataAccessException {
        return authDataAccess.getUsername(authToken) != null;
    }

    // Method to check if the game exists
    public static boolean gameExists(int gameID) throws SQLException {
        return gameDataAccess.getGame(gameID).getGame() != null;
    }

    public static boolean validID(int gameID) throws SQLException {
        return gameDataAccess.getGame(gameID) != null;
    }

    // Method to check if the user is an observer
    public static boolean isObserver(String authToken, int gameID) throws SQLException, DataAccessException {
        GameData game = gameDataAccess.getGame(gameID);
        String username = authDataAccess.getUsername(authToken);
        return !game.containsPlayer(username);
    }

    // Method to check if the game is over
    public static boolean isGameOver(int gameID) throws SQLException {
        GameData game = gameDataAccess.getGame(gameID);
        return game.getGame().isGameOver();
    }
}

