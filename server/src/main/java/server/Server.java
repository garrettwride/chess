package server;

import com.google.gson.Gson;
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
        // Get the value of the "username" parameter from the request
        var pet = new Gson().fromJson(request.body(), Pet.class);

// Get the value of the "password" parameter from the request
        String password = request.queryParams("password");

// Get the value of the "email" parameter from the request
        String email = request.queryParams("email");
        return registrationService.register(username, password, email);
    }
}
