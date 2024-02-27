package server;

import com.google.gson.Gson;
import dataAccess.*;
import dataModels.*;
import org.eclipse.jetty.security.LoginService;
import service.RegistrationException;
import service.*;
import spark.*;

public class Server {

    private RegistrationService registrationService;
    private final LoginService loginService;
    private ApplicationService applicationService;
    final Gson gson;

    public Server(){
        DataMemory dataMemory = new DataMemory();
        UserDataAccess userDataAccess = new UserDataAccess(dataMemory);
        GameDataAccess gameDataAccess = new GameDataAccess(dataMemory);
        AuthDataAccess authDataAccess = new AuthDataAccess(dataMemory);
        registrationService = new RegistrationService(userDataAccess, authDataAccess);
        loginService = new LoginService();
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
            String result = registrationService.register(user);
            response.status(200); // Success
            return result;
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

    public String loginHandler(Request request, Response response) {
        try {
            User user = gson.fromJson(request.body(), User.class);
            String authToken = loginService.authenticate(user.getUsername(), user.getPassword());
            response.status(200); // Success
            return gson.toJson(new SuccessResponse(user.getUsername(), authToken));
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
}
