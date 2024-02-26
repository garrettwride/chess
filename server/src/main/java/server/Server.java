package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        registerEndpoints();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void registerEndpoints() {
        // Handle POST request to /user
        Spark.post("/user", (request, response) -> {
            // Extract username, password, and email from request body
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            String email = request.queryParams("email");

            // Call register method of RegistrationService
            String authToken = RegistrationService.register(username, password, email);

            // Return the generated authToken as the response
            response.status(200);
            return authToken;
        });
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
