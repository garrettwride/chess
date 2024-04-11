package server.websocket;

import com.google.gson.Gson;
import dataAccess.*;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.ErrorResponse;
import service.JoinGameService;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;
import chess.*;
import service.LoginService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, SQLException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer((JoinPlayerCommand) command, session);
            case JOIN_OBSERVER -> joinObserver((JoinObserverCommand) command, session);
            case MAKE_MOVE -> makeMove((MakeMoveCommand) command, session);
            case LEAVE -> leave((LeaveCommand) command, session);
            case RESIGN -> resign((ResignCommand) command, session);
        }
    }

    private void joinPlayer(JoinPlayerCommand command, Session session) throws IOException, DataAccessException, SQLException {
        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        ChessGame.TeamColor playerColor = command.getPlayerColor();
        ChessGame game = JoinGameService.getGame(gameID).getGame();
        try {
            connections.sendToClient(authToken, new LoadGameMessage(game));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        connections.add(authToken, session);
        String username = LoginService.getUsername(authToken);
        var message = String.format("%s joined as a player", username);
        var notification = new NotificationMessage(message);
        connections.broadcast(authToken, notification);
    }

    private void joinObserver(JoinObserverCommand command, Session session) throws DataAccessException, SQLException {
        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        String username = LoginService.getUsername(authToken);
        ChessGame game = JoinGameService.getGame(gameID).getGame();
        try {
            connections.sendToClient(authToken, new LoadGameMessage(game));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var message = String.format("%s joined as an observer", username);
        var notification = new NotificationMessage(message);
        try {
            connections.broadcast(authToken, notification);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void makeMove(MakeMoveCommand command, Session session) {
        int gameID = command.getGameID();
        ChessMove move = command.getMove();
        String authToken = command.getAuthString();
    }

    private void leave(LeaveCommand command, Session session) {
        int gameID = command.getGameID();
        String authToken = command.getAuthString();


    }

    private void resign(ResignCommand command, Session session) {
        int gameID = command.getGameID();
        String authToken = command.getAuthString();

    }
}