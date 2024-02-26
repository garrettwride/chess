package dataAccess;

import java.util.HashMap;
import java.util.Map;
import dataModels.*;

public class DataMemory {
    // Data structures for storing Users, AuthTokens, and Games
    private Map<String, User> users;
    private Map<String, AuthToken> authTokens;
//    private Map<String, Game> games;

    public DataMemory() {
        // Initialize the data structures
        users = new HashMap<>();
        authTokens = new HashMap<>();
//        games = new HashMap<>();
    }

    // Methods for adding, retrieving, updating, and deleting data objects
    // User-related methods
    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public User getUser(String username) {
        return users.get(username);
    }

    // AuthToken-related methods
    public void addAuthToken(String authToken, User user) {
        authTokens.put(authToken, new AuthToken(authToken, user));
    }

    public AuthToken getAuthToken(String authToken) {
        return authTokens.get(authToken);
    }

//    // Game-related methods
//    public void addGame(Game game) {
//        games.put(game.getGameID(), game);
//    }
//
//    public Game getGame(String gameID) {
//        return games.get(gameID);
//    }



}

