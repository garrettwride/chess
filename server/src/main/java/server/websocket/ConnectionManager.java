package server.websocket;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Game> games = new ConcurrentHashMap<>();

    public void createGame(int gameId) {
        var game = new Game(gameId);
        games.put(gameId, game);
    }

    public Game getGame(int gameId) {
        return games.get(gameId);
    }

    public void removeGame(int gameId) {
        games.remove(gameId);
    }
}
