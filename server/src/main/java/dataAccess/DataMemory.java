package dataAccess;

import java.util.HashMap;
import java.util.Map;
import dataModels.*;
import chess.ChessGame;

public class DataMemory {
    // Data structures for storing Users, AuthTokens, and Games
    private Map<String, User> users;
    private Map<String, AuthData> authTokens;
    private Map<Integer, Game> games;

    public DataMemory() {
        // Initialize the data structures
        users = new HashMap<>();
        authTokens = new HashMap<>();
        games = new HashMap<>();
    }

    // Methods for adding, retrieving, updating, and deleting data objects
    // User-related methods
    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public void clearUsers() {
        users.clear();
    }

    // AuthToken-related methods
    public void addAuthToken(AuthData authToken) {
        authTokens.put(authToken.getUsername(), authToken);
    }

    public AuthData getAuthToken(String authToken) {
        return authTokens.get(authToken);
    }

    public void deleteAuthToken(String username) {
        authTokens.remove(username);
    }

    public void clearAuthTokens(){
        authTokens.clear();
    }

    // Game-related methods
    public void addGame(Game game) {
        games.put(game.getGameID(), game);
    }

    public Game getGame(String gameID) {
        return games.get(gameID);
    }
    public void clearGames() {
        games.clear();
    }

}

