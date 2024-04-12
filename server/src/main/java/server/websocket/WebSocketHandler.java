package server.websocket;

import com.google.gson.Gson;
import dataAccess.*;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.ErrorResponse;
import service.AuthenticationException;
import service.JoinGameService;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;
import chess.*;
import service.LoginService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, SQLException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> {
                JoinPlayerCommand join = new Gson().fromJson(message, JoinPlayerCommand.class);
                joinPlayer(join, session);
            }
            case JOIN_OBSERVER -> joinObserver((JoinObserverCommand) command, session);
            case MAKE_MOVE -> makeMove((MakeMoveCommand) command, session);
            case LEAVE -> leave((LeaveCommand) command, session);
            case RESIGN -> resign((ResignCommand) command, session);
        }
    }

    private void joinPlayer(JoinPlayerCommand command, Session session) throws IOException, DataAccessException, SQLException {
        session.getRemote().sendString("Working");
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

        connections.add(authToken, session);
        var message = String.format("%s joined as an observer", username);
        var notification = new NotificationMessage(message);
        try {
            connections.broadcast(authToken, notification);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void makeMove(MakeMoveCommand command, Session session) throws SQLException, DataAccessException {
        int gameID = command.getGameID();
        ChessMove move = command.getMove();
        String authToken = command.getAuthString();
        ChessGame game = JoinGameService.getGame(gameID).getGame();

        try {
            game.makeMove(move);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }


        LoadGameMessage updatedGameMessage = new LoadGameMessage(game);
        try {
            connections.broadcastAll(authToken, updatedGameMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String username = LoginService.getUsername(authToken);
        String moveDescription = getMoveDescription(move, game);
        var notification = new NotificationMessage(username + " made a move: " + moveDescription);
        try {
            connections.broadcast(authToken, notification);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GameData gameData = JoinGameService.getGame(gameID);
        ChessGame.TeamColor currentUserColor = (username.equals(gameData.getWhiteUsername())) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        ChessGame.TeamColor opponentColor = (currentUserColor == ChessGame.TeamColor.WHITE) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
        String opponentUsername = (currentUserColor == ChessGame.TeamColor.WHITE) ? gameData.getBlackUsername() : gameData.getWhiteUsername();
        checkStatus(opponentColor, game, opponentUsername, authToken, gameID);
    }

    private void leave(LeaveCommand command, Session session) throws DataAccessException {
        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        try {
            JoinGameService.removeFromGame(authToken, gameID);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        connections.remove(authToken);
        String username = LoginService.getUsername(authToken);
        var message = String.format("%s left game", username);
        var notification = new NotificationMessage(message);
        try {
            connections.broadcast(authToken, notification);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void resign(ResignCommand command, Session session) throws DataAccessException, SQLException {
        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        ChessGame game = JoinGameService.getGame(gameID).getGame();

        try {
            endGame(gameID, authToken);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String username = LoginService.getUsername(authToken);
        var message = String.format("%s resigned from game", username);
        var notification = new NotificationMessage(message);
        try {
            connections.broadcast(authToken, notification);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getMoveDescription(ChessMove move, ChessGame game) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();

        Character pieceSymbol = game.getBoard().getPiece(start).getSymbol();
        String startSquare = positionToAlgebraicNotation(start);
        String endSquare = positionToAlgebraicNotation(end);
        return String.format("%s from %s to %s", pieceSymbol, startSquare, endSquare);
    }

    private String positionToAlgebraicNotation(ChessPosition position) {
        char file = (char) ('a' + position.getColumn() - 1);
        int rank = position.getRow();
        return String.format("%c%d", file, rank);
    }
    public void checkStatus(ChessGame.TeamColor teamColor, ChessGame game, String username, String authToken, int gameID) {
        boolean isInCheck = game.isInCheck(teamColor);
        boolean isInCheckmate = game.isInCheckmate(teamColor);
        boolean isInStalemate = game.isInStalemate(teamColor);

        if (game.findKing(teamColor, game.getBoard()) == null) {
            var notification = new NotificationMessage(username + "lost");
            try {
                connections.broadcastAll(authToken, notification);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                endGame(gameID, authToken);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (isInCheckmate) {
            var notification = new NotificationMessage(username + " is in checkmate!");
            try {
                connections.broadcastAll(authToken, notification);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                endGame(gameID, authToken);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (isInStalemate) {
            var notification = new NotificationMessage("Players are in stalemate!");
            try {
                connections.broadcastAll(authToken, notification);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                endGame(gameID, authToken);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (isInCheck) {
            var notification = new NotificationMessage(username + " is in check!");
            try {
                connections.broadcastAll(authToken, notification);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void endGame(int gameID, String authToken) throws SQLException {
        JoinGameService.endGame(gameID);
        ChessGame endedGame = JoinGameService.getGame(gameID).getGame();
        LoadGameMessage updatedGameMessage = new LoadGameMessage(endedGame);
        try {
            connections.broadcastAll(authToken, updatedGameMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}