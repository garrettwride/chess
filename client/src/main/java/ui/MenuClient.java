package ui;

import java.util.Arrays;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.*;
import messages.SuccessResponse;
import model.*;
import exception.ResponseException;
import websocket.*;

public class MenuClient {
    public Gson gson;
    private String authToken = null;
    private final ServerFacade server;
    private final String serverUrl;
    private NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private State state = State.SIGNEDOUT;
    private GameState gameState = GameState.NOT_JOINED;

    public MenuClient(String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }


    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();
                case "login" -> login(params);
                case "register" -> register(params);
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> joinObserver(params);
                case "leave" -> leaveGame(params);
                case "resign" -> resignGame(params);
                case "move" -> makeMove(params);
                case "quit" -> "quit";
                default -> "Invalid command. Type 'help' for available commands.";
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            var name = params[0];
            String auth = authToken;
            String ID = server.addGame(name, auth);
            return String.format("Successfully created game. Assigned ID: %s", ID);
        }
        throw new ResponseException(400, "Expected: <name>");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        String auth = authToken;

        // Retrieve the JSON array of games from the server
        JsonArray gamesArray = server.listGames(auth);

        // Parse the JSON array to extract individual game objects
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < gamesArray.size(); i++) {
            JsonObject game = gamesArray.get(i).getAsJsonObject();

            // Extract game properties and append to the result string
            String gameId = game.get("gameID").getAsString();
            String whiteUsername = (game.get("whiteUsername") != null) ? game.get("whiteUsername").getAsString() : "";
            String blackUsername = (game.get("blackUsername") != null) ? game.get("blackUsername").getAsString() : "";
            String gameName = game.get("gameName").getAsString();

            result.append("Game ID: ").append(gameId)
                    .append(", White Player: ").append(whiteUsername)
                    .append(", Black Player: ").append(blackUsername)
                    .append(", Game Name: ").append(gameName)
                    .append('\n');
        }

        return result.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 2) {
            try {
                var id = Integer.parseInt(params[0]);
                String auth = authToken;
                var playerColor = (params[1]).toUpperCase();
                GameInfo gameInfo = new GameInfo();
                gameInfo.setPlayerColor(playerColor);
                gameInfo.setGameID(id);
                SuccessResponse response = server.joinGame(gameInfo, auth);
                if (playerColor.equals("BLACK")) {
                    ws.joinPlayer(id, ChessGame.TeamColor.BLACK);
                } else {
                    ws.joinPlayer(id, ChessGame.TeamColor.WHITE);
                }

                new DrawBoard();
                gameState = GameState.PLAYER;
                return response.getMessage();
                }
            catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "<ID> <WHITE|BLACK|<empty>>");
    }

    public String joinObserver(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            try {
                var id = Integer.parseInt(params[0]);
                String auth = authToken;
                GameInfo gameInfo = new GameInfo();
                gameInfo.setGameID(id);
                server.joinGame(gameInfo, auth);
                ws.joinObserver(id);
                new DrawBoard();
                gameState = GameState.OBSERVER;
                return "Successfully joined as observer";
            }
            catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "<ID>");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        ws = null;
        String auth = authToken;
        try {
            SuccessResponse response = server.deauthenticate(auth);
            state = State.SIGNEDOUT;
            authToken = null;
            return response.getMessage();
        } catch (ResponseException e) {

            throw e;
        }
    }


    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            var username = params[0];
            var password = params[1];
            UserData user = new UserData(username, password, null);

            // Make the authentication request
            AuthData authData = server.authenticate(user);

            // Extract the authentication token
            authToken = authData.getAuthToken();

            if (authToken != null) {
                state = State.SIGNEDIN;
                ws = new WebSocketFacade(serverUrl, notificationHandler, authToken);
                return "You signed in.";
            }
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            var username = params[0];
            var password = params[1];
            var email = params[2];
            UserData user = new UserData(username, password, email);
            AuthData authData = server.register(user);

            String auth = authData.getAuthToken();
            authToken = auth;
            if (authToken != null) {
                state = State.SIGNEDIN;
                return String.format("%s successfully registered", authData.getUsername());
            }
        }
        throw new ResponseException(400, "<username> <password> <email>");
    }

    public String leaveGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            var gameID = Integer.parseInt(params[0]);
            ws.leave(gameID);
            gameState = GameState.NOT_JOINED;
            return "You left the game.";
        }
        throw new ResponseException(400, "Expected: <ID>");
    }

    public String resignGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            var gameID = Integer.parseInt(params[0]);
            ws.resign(gameID);
            gameState = GameState.NOT_JOINED;
            return "You left the game.";
        }
        throw new ResponseException(400, "Expected: <ID>");
    }

    public String makeMove(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 4 || params.length == 3) {
            var gameID = Integer.parseInt(params[0]);
            ChessMove move;
            if (params.length == 4) {
                move = new ChessMove(parseCoordinate(params[1]), parseCoordinate(params[2]), parsePieceType(params[3]));

            } else {
                move = new ChessMove(parseCoordinate(params[1]), parseCoordinate(params[2]), null);
            }

            ws.makeMove(gameID, move);
            return "You left the game.";
        }
        throw new ResponseException(400, "Expected: <ID> <starting position> <end position> <promotion piece>");
    }

    public String help() {
        if (gameState == gameState.PLAYER) {
            return """
                    - redraw
                    - leave
                    - resign
                    - highlight
                    - move
                    - help
                    - quit
                    """;
        } else if (gameState == GameState.OBSERVER) {
            return """
                    - redraw
                    - leave
                    - help
                    - quit
                    """;
        } else if (state == State.SIGNEDOUT) {
            return """
                    - register <username> <password> <email>
                    - login <username> <password>
                    - help
                    - quit
                    """;
        }
        return """
                - list
                - create <name>
                - join <ID> [WHITE|BLACK|<empty>]
                - observe <ID>
                - logout
                - help
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }

    public ChessPosition parseCoordinate(String coordinate) throws IllegalArgumentException {
        if (coordinate.length() != 2) {
            throw new IllegalArgumentException("Invalid coordinate string: " + coordinate);
        }

        int col = coordinate.charAt(0) - 'a';

        int row = Character.getNumericValue(coordinate.charAt(1)) - 1;

        if (row < 0 || row > 7 || col < 0 || col > 7) {
            throw new IllegalArgumentException("Invalid coordinate string: " + coordinate);
        }

        return new ChessPosition(row, col);
    }

    public ChessPiece.PieceType parsePieceType(String pieceString) throws IllegalArgumentException {
        pieceString = pieceString.toUpperCase();

        switch (pieceString) {
            case "KING":
                return ChessPiece.PieceType.KING;
            case "QUEEN":
                return ChessPiece.PieceType.QUEEN;
            case "BISHOP":
                return ChessPiece.PieceType.BISHOP;
            case "KNIGHT":
                return ChessPiece.PieceType.KNIGHT;
            case "ROOK":
                return ChessPiece.PieceType.ROOK;
            case "PAWN":
                return ChessPiece.PieceType.PAWN;
            default:
                throw new IllegalArgumentException("Invalid piece type: " + pieceString);
        }
    }
}
