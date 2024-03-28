package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dataAccess.*;
import messages.SuccessResponse;
import model.*;
import service.*;
import spark.*;

import java.util.List;
import java.util.Objects;

public class Server {

    private RegistrationService registrationService;
    private LoginService loginService;

    private JoinGameService joinGameService;
    private ApplicationService applicationService;
    private UserDataAccess userDataAccess = new UserDataAccess();
    private GameDataAccess gameDataAccess = new GameDataAccess();
    private AuthDataAccess authDataAccess = new AuthDataAccess();
    public Gson gson;

    public Server(){
    }

    public int run(int desiredPort) {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        registrationService = new RegistrationService(userDataAccess, authDataAccess);
        loginService = new LoginService(authDataAccess, userDataAccess);
        joinGameService = new JoinGameService(gameDataAccess, authDataAccess);
        applicationService = new ApplicationService(userDataAccess, gameDataAccess, authDataAccess);
        gson = new Gson();
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Define endpoints
        Spark.post("/user", this::handleRegistration);
        Spark.delete("/db", this::handleClear);
        Spark.post("/session", this::handleLogin);
        Spark.delete("/session", this::handleLogout);
        Spark.post("/game", this::handleCreateGame);
        Spark.put("/game", this::handleJoinGame);
        Spark.get("/game", this::handleListGames);

        Spark.awaitInitialization();

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private String handleRegistration(Request request, Response response) {
        try {
            UserData user = gson.fromJson(request.body(), UserData.class);
            if (user.getUsername() == null || user.getPassword() == null){
                throw new IllegalArgumentException("Error: bad request");
            }
            AuthData result = registrationService.register(user);
            response.status(200); // Success
            return gson.toJson(result, AuthData.class);
        } catch (IllegalArgumentException e) {
            response.status(400); // Bad request
            return gson.toJson(new ErrorResponse("Error: bad request"));
        } catch (IllegalStateException e) {
            response.status(403); // Already taken
            return gson.toJson(new ErrorResponse("Error: already taken"));
        } catch (Exception e) {
            response.status(500); // Internal server error
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        }
    }

    public String handleLogin(Request request, Response response) {
        try {
            UserData user = gson.fromJson(request.body(), UserData.class);
            String authToken = loginService.authenticate(user.getUsername(), user.getPassword());
            response.status(200); // Success
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("username", user.getUsername());
            jsonObject.addProperty("authToken", authToken);
            return gson.toJson(jsonObject);
        } catch (IllegalArgumentException e) {
            response.status(400); // Bad request
            return gson.toJson(new ErrorResponse("Error: bad request"));
        } catch (AuthenticationException e) {
            response.status(401); // Unauthorized
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (Exception e) {
            response.status(500); // Internal server error
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        }
    }


    public String handleLogout(Request request, Response response) {
        try {
            String authToken = request.headers("authorization");
            if (authToken == null) {
                throw new AuthenticationException("Error: Unauthorized");
            }
            loginService.deauthenticate(authToken);
            response.status(200); // Success
            return gson.toJson(new SuccessResponse("logged out successfully"));
        } catch (IllegalArgumentException e) {
            response.status(400); // Bad request
            return gson.toJson(new ErrorResponse("Error: bad request"));
        } catch (AuthenticationException e) {
            response.status(401); // Unauthorized
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (Exception e) {
            response.status(500); // Internal server error
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        }
    }


    // Handler for listing all available games
    private String handleListGames(Request request, Response response) {
        try {
            String authToken = request.headers("authorization");
            if (authToken == null) {
                throw new AuthenticationException("Error: Unauthorized");
            }

            // Call the JoinGameService method to get all available games
            List<GameData> games = joinGameService.listGames(authToken);

            // Create a JSON array to store game information
            JsonArray gamesArray = new JsonArray();
            for (GameData game : games) {
                JsonObject gameObject = new JsonObject();
                gameObject.addProperty("gameID", game.getGameID());
                gameObject.addProperty("whiteUsername", game.getWhiteUsername());
                gameObject.addProperty("blackUsername", game.getBlackUsername());
                gameObject.addProperty("gameName", game.getGameName());
                gamesArray.add(gameObject);
            }

            // Create a JSON object to wrap the games array
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.add("games", gamesArray);

            response.status(200); // Success
            return gson.toJson(jsonResponse);
        } catch (AuthenticationException e) {
            response.status(401); // Unauthorized
            return gson.toJson(new ErrorResponse("Error: Unauthorized"));
        } catch (Exception e) {
            response.status(500); // Internal server error
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        }
    }


    // Handler for creating a new game
    private String handleCreateGame(Request request, Response response) {
        try {
            String authToken = request.headers("authorization");

            // Parse the JSON request body into GameInfo object
            GameData gameData = gson.fromJson(request.body(), GameData.class);

            // Extract the game name from the GameInfo object
            String gameName = gameData.getGameName();
            // Call the createGame method and get the game ID
            int gameID = joinGameService.createGame(gameName, authToken);

            // Create a JSON object with the game ID
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("gameID", gameID);

            response.status(200); // Success
            return gson.toJson(jsonResponse);
        } catch (IllegalArgumentException e) {
            response.status(400); // Bad request
            return gson.toJson(new ErrorResponse("Error: bad request"));
        } catch (AuthenticationException e) {
            response.status(401); // Unauthorized
            return gson.toJson(new ErrorResponse("Error: Unauthorized"));
        } catch (Exception e) {
            response.status(500); // Internal server error
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        }
    }

    // Handler for joining an existing game
    private String handleJoinGame(Request request, Response response) {
        try {
            String authToken = request.headers("authorization");
            if (authToken == null) {
                throw new AuthenticationException("Error: Unauthorized");
            }
            // Extract necessary information from the request
            GameInfo gameInfo = gson.fromJson(request.body(), GameInfo.class);

            String playerColor = gameInfo.getPlayerColor();
            int gameID = gameInfo.getGameID();
            if(playerColor != null){
                // Call the JoinGameService method to join an existing game
                joinGameService.joinGame(authToken, playerColor, gameID);
            } else {
                joinGameService.observeGame(gameID, authToken);
            }

            response.status(200);
            return gson.toJson(new SuccessResponse("Game successfully joined"));
        } catch (IllegalArgumentException e) {
            response.status(400); // Bad request
            return gson.toJson(new ErrorResponse("Error: bad request"));
        } catch (AuthenticationException e) {
            response.status(401); // Unauthorized
            return gson.toJson(new ErrorResponse("Error: Unauthorized"));
        } catch (IllegalStateException e) {
            response.status(403); // Forbidden
            return gson.toJson(new ErrorResponse("Error: already taken"));
        } catch (Exception e) {
            response.status(500); // Internal server error
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        }

    }

    private String handleClear(Request request, Response response) {
        try {
            applicationService.clear();
            response.status(200); // Success
            return gson.toJson(new SuccessResponse("Data cleared successfully"));

        } catch (Exception e) {
            response.status(500); // Internal server error
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return Objects.equals(registrationService, server.registrationService) && Objects.equals(loginService, server.loginService) && Objects.equals(applicationService, server.applicationService) && Objects.equals(gson, server.gson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationService, loginService, applicationService, gson);
    }

    @Override
    public String toString() {
        return "Server{" +
                "registrationService=" + registrationService +
                ", loginService=" + loginService +
                ", applicationService=" + applicationService +
                ", gson=" + gson +
                '}';
    }
}