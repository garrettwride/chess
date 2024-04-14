package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.*;

import java.io.IOException;
import java.util.ArrayList;
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
}
