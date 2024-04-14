package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String authToken, Session session) {
        var connection = new Connection(authToken, session);
        connections.put(authToken, connection);
    }

    public void remove(String authToken) {
        connections.remove(authToken);
    }

    public void broadcast(String excludeAuthToken, ServerMessage serverMessage) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(excludeAuthToken)) {
                    c.send(new Gson().toJson(serverMessage));
                }
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
