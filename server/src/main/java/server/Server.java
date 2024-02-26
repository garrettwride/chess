package server;

import spark.*;
import com.google.gson.Gson;

public class Server {
    private final Gson gson = new Gson();


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

                // Serialize the response object to JSON
                String jsonResponse = gson.toJson(authToken);

                // Set the content type to JSON
                response.type("application/json");

                // Return the serialized JSON response
                return jsonResponse;
            } catch (RegistrationException e) {
                // Handle registration error
                response.status(400); // Bad Request
                return gson.toJson("Registration failed: " + e.getMessage());
            } catch (Exception e) {
                // Handle other unexpected errors
                response.status(500); // Internal Server Error
                return gson.toJson("Internal server error: " + e.getMessage());
            }
        });
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
