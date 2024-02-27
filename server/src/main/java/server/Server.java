package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.*;
import dataModels.*;
import service.RegistrationException;
import service.*;
import spark.*;

import java.io.Reader;
import java.util.Objects;

public class Server {

    private RegistrationService registrationService;
    private LoginService loginService;
    private ApplicationService applicationService;
    final Gson gson;

    public Server(){
        DataMemory dataMemory = new DataMemory();
        UserDataAccess userDataAccess = new UserDataAccess(dataMemory);
        GameDataAccess gameDataAccess = new GameDataAccess(dataMemory);
        AuthDataAccess authDataAccess = new AuthDataAccess(dataMemory);
        registrationService = new RegistrationService(userDataAccess, authDataAccess);
        loginService = new LoginService(authDataAccess, userDataAccess);
        applicationService = new ApplicationService(userDataAccess, gameDataAccess, authDataAccess);
        this.gson = new Gson();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Define endpoints
        Spark.post("/user", this::handleRegistration);
        Spark.delete("/db", this::handleClear);
        Spark.post("/session", this::handleLogin);
        Spark.delete("/session", this::handleLogout);


        Spark.awaitInitialization();

        return desiredPort;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private String handleRegistration(Request request, Response response) {
        try {
            User user = gson.fromJson(request.body(), User.class);
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
            User user = gson.fromJson(request.body(), User.class);
            String authToken = loginService.authenticate(user.getUsername(), user.getPassword());
            response.status(200); // Success
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("username", user.getUsername());
            jsonObject.addProperty("authToken", authToken);
            return gson.toJson(jsonObject);
        } catch (IllegalArgumentException e) {
            response.status(400); // Bad request
            return gson.toJson(new ErrorResponse("Error: bad request"));
        } catch (IllegalStateException e) {
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
            return gson.toJson(new ErrorResponse("Error: Unauthorized"));
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
