package server;

import spark.*;
import service.RegistrationService;

public class Server {

    private RegistrationService registrationService;

    public void run(int desiredPort) {
        Spark.port(desiredPort);

        // Define routes
        Spark.post("/user", this::handleRegistration);

        Spark.staticFiles.location("web");

        Spark.awaitInitialization();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private String handleRegistration(Request request, Response response) {
        // Get the value of the "username" parameter from the request
        String username = request.queryParams("username");

// Get the value of the "password" parameter from the request
        String password = request.queryParams("password");

// Get the value of the "email" parameter from the request
        String email = request.queryParams("email");
        return registrationService.register(username, password, email);
    }
}
