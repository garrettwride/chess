package ui;

import java.util.Arrays;

import com.google.gson.*;
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
                var playerColor = (params[1]);
                GameInfo gameInfo = new GameInfo();
                gameInfo.setPlayerColor(playerColor);
                gameInfo.setGameID(id);
                server.joinGame(gameInfo, auth);
                return "Successfully joined game";
                }
            catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "<ID> <WHITE|BLACK|<empty>>");
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

//    private GameData getGame(int id) throws ResponseException {
//        String auth = authToken;
//        JsonArray gamesArray = server.listGames(auth);
//
//        // Process the JSON array
//        for (JsonElement element : gamesArray) {
//            JsonObject gameObject = element.getAsJsonObject();
//
//            // Check if the game ID property exists and is not null
//            JsonElement gameIDElement = gameObject.get("gameID");
//            if (gameIDElement != null && !gameIDElement.isJsonNull()) {
//                int gameID = gameIDElement.getAsInt();
//                if (gameID == id) {
//                    // Extract other game properties and create a GameData object
//                    String whiteUsername = gameObject.get("whiteUsername").getAsString();
//                    String blackUsername = gameObject.get("blackUsername").getAsString();
//                    String gameName = gameObject.get("gameName").getAsString();
//
//                    return new GameData(gameID, whiteUsername, blackUsername, gameName, null);
//                }
//            } else {
//                return null;
//            }
//        }
//
//        return null;
//    }

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
