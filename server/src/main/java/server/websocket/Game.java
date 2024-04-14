package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private int gameId;
    private ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public Game(int gameId) {
        this.gameId = gameId;
    }

    public void add(String authToken, Session session) {
        if (!connections.containsKey(authToken)) {
            var connection = new Connection(authToken, session);
            connections.put(authToken, connection);
        }
    }

    public void remove(String authToken) {
        connections.remove(authToken);
    }

    public void broadcast(String excludeAuthToken, ServerMessage serverMessage) throws IOException {
        for (var c : connections.values()) {
            if (c.session.isOpen() && !c.authToken.equals(excludeAuthToken)) {
                c.send(new Gson().toJson(serverMessage));
            }
        }
    }

    public void sendToClient(String authToken, ServerMessage serverMessage) throws IOException {
        var connection = connections.get(authToken);
        if (connection != null && connection.session.isOpen()) {
            connection.send(new Gson().toJson(serverMessage));
        }
    }

    public void broadcastAll(String excludeAuthToken, ServerMessage serverMessage) throws IOException {
        sendToClient(excludeAuthToken, serverMessage);
        broadcast(excludeAuthToken, serverMessage);
    }
}
