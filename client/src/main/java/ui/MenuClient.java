package ui;

import java.util.Arrays;

import com.google.gson.Gson;
import model.*;
import exception.ResponseException;
import websocket.*;

public class MenuClient {
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
////    private final NotificationHandler notificationHandler;
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
                //case "register" -> register(params);
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
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            visitorName = String.join("-", params);
            //ws = new WebSocketFacade(serverUrl, notificationHandler);
            //ws.enterPetShop(visitorName);
            return String.format("You signed in as %s.", visitorName);
        }
        throw new ResponseException(400, "Expected: <yourname>");
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 2) {
            var name = params[0];
            var auth = params[1];
            GameInfo game = new GameInfo();
            game.setGameName(name);
            game = server.addGame(game, auth);
            return String.format("You joined %s. Assigned ID: %d", game.getGameID());
        }
        throw new ResponseException(400, "Expected: <name> <CAT|DOG|FROG>");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        String auth = "";
        var pets = server.listGames(auth);
        var result = new StringBuilder();
        var gson = new Gson();
        for (var pet : pets) {
            result.append(gson.toJson(pet)).append('\n');
        }
        return result.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 2) {
            try {
                var id = Integer.parseInt(params[1]);
                String auth = "";
                var playerColor = (params[0]);
                var game = getGame(id);
                if (game != null) {
                    server.joinGame(id, playerColor, auth);
                    return "Succefully joined game";
                }
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Expected: <pet id>");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
       // ws.leavePetShop(visitorName);
        //ws = null;
        state = State.SIGNEDOUT;
        return String.format("%s left the shop", visitorName);
    }

    private GameData getGame(int id) throws ResponseException {
        String auth = "";
        for (var game : server.listGames(auth)) {
            if (game.getGameID() == id) {
                return game;
            }
        }
        return null;
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - signIn <yourname>
                    - quit
                    """;
        }
        return """
                - list
                - adopt <pet id>
                - rescue <name> <CAT|DOG|FROG|FISH>
                - adoptAll
                - signOut
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
