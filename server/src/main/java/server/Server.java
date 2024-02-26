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
            try {
                // Extract username, password, and email from request body
                String username = request.queryParams("username");
                String password = request.queryParams("password");
                String email = request.queryParams("email");

                // Call register method of RegistrationService
                String authToken = RegistrationService.register(username, password, email);

                // Return the generated authToken as the response
                response.status(200);
                return authToken;
            } catch (RegistrationException e) {
                // Handle registration error
                response.status(400); // Bad Request
                return "Registration failed: " + e.getMessage();
            } catch (Exception e) {
                // Handle other unexpected errors
                response.status(500); // Internal Server Error
                return "Internal server error: " + e.getMessage();
            }
        });
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
