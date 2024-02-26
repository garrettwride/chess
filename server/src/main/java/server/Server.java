package server;

import com.google.gson.Gson;
import dataModels.User;
import spark.*;
import service.RegistrationService;

public class Server {

    private RegistrationService registrationService;

    public void run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Define routes
        Spark.post("/user", this::handleRegistration);

        Spark.awaitInitialization();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private String handleRegistration(Request request, Response response) {
        var user = new Gson().fromJson(request.body(), User.class);

        return registrationService.register(user);
    }
}
