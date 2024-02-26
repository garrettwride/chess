package server;

import com.google.gson.Gson;
import dataAccess.*;
import dataModels.User;
import service.RegistrationException;
import service.*;
import spark.*;

public class Server {

    private RegistrationService registrationService;

    public Server(){
        DataMemory dataMemory = new DataMemory();
        UserDataAccess userDataAccess = new UserDataAccess(dataMemory);
        registrationService = new RegistrationService(userDataAccess);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Define routes
        Spark.post("/user", this::handleRegistration);

        Spark.awaitInitialization();

        return desiredPort;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private String handleRegistration(Request request, Response response) {
        try {
            User user = Gson.fromJson(request.body(), User.class);
            String result = registrationService.register(user);
            response.status(200); // Success
            return result;
        } catch (IllegalArgumentException e) {
            response.status(400); // Bad request
            return Gson.toJson(new ErrorResponse("Error: bad request"));
        } catch (IllegalStateException e) {
            response.status(403); // Already taken
            return Gson.toJson(new ErrorResponse("Error: already taken"));
        } catch (Exception e) {
            response.status(500); // Internal server error
            return Gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        }
    }
}
