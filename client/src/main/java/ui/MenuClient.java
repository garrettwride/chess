package ui;

import java.util.Arrays;

import com.google.gson.Gson;
import model.*;
import exception.ResponseException;
import websocket.*;
//import server.ServerFacade;

public class MenuClient {
    private String visitorName = null;
//    //private final ServerFacade server;
    private final String serverUrl;
////    private final NotificationHandler notificationHandler;
//    //private WebSocketFacade ws;
    private State state = State.SIGNEDOUT;
//
    public MenuClient(String serverUrl) {
        //server = new ServerFacade(serverUrl);
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
                case "observe" -> joinObserver(params);
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
            var type = PetType.valueOf(params[1].toUpperCase());
            var pet = new GameInfo(0, name, type);
            //pet = server.addPet(pet);
            return String.format("You rescued %s. Assigned ID: %d", pet.name(), pet.id());
        }
        throw new ResponseException(400, "Expected: <name> <CAT|DOG|FROG>");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        var pets = server.listPets();
        var result = new StringBuilder();
        var gson = new Gson();
        for (var pet : pets) {
            result.append(gson.toJson(pet)).append('\n');
        }
        return result.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            try {
                var id = Integer.parseInt(params[0]);
                var pet = getGame(id);
                if (pet != null) {
                    server.deletePet(id);
                    return String.format("%s says %s", pet.name(), pet.sound());
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

    private Pet getGame(int id) throws ResponseException {
        for (var pet : server.listPets()) {
            if (pet.id() == id) {
                return pet;
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
