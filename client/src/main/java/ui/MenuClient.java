package ui;

import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.*;
import exception.ResponseException;
//import websocket.*;

public class MenuClient {
    public Gson gson;
    private String authToken = null;
    private final ServerFacade server;
    private final String serverUrl;
////    private final NotificationHandler;
//    //private WebSocketFacade ws;
    private State state = State.SIGNEDOUT;
//
    public MenuClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        //this.notificationHandler = notificationHandler;
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
                //case "observe" -> joinObserver(params);
                case "quit" -> "quit";
                default -> "Invalid command. Type 'help' for available commands.";
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
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
            String auth = authData.getAuthToken();
            authToken = auth;

            if (authToken != null) {
                state = State.SIGNEDIN;
                return "You signed in.";
            }
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }


    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            var name = params[0];
            String auth = authToken;
            GameInfo game = new GameInfo();
            game.setGameName(name);
            game = server.addGame(game, auth);
            return String.format("Successfully created game. Assigned ID: %d", game.getGameID());
        }
        throw new ResponseException(400, "Expected: <name>");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        String auth = authToken;
        var games = server.listGames(auth);
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games) {
            result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 2) {
            try {
                var id = Integer.parseInt(params[1]);
                String auth = authToken;
                var playerColor = (params[0]);
                var game = getGame(id);
                if (game != null) {
                    server.joinGame(id, playerColor, auth);
                    return "Successfully joined game";
                }
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "<ID> [WHITE|BLACK|<empty>]");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
       // ws.leavePetShop(visitorName);
        //ws = null;
        String auth = authToken;
        server.deauthenticate(auth);
        state = State.SIGNEDOUT;
        authToken = null;
        return "You signed out";
    }

    private GameData getGame(int id) throws ResponseException {
        String auth = authToken;
        for (var game : server.listGames(auth)) {
            if (game.getGameID() == id) {
                return game;
            }
        }
        return null;
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
                assertSignedIn();
            }
            return String.format("%s successfully registered", authData.getUsername());
        }
        throw new ResponseException(400, "<username> <password> <email>");
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
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
}
