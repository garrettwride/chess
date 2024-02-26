package service;

import com.google.gson.Gson;
import spark.Response;
import dataAccess.;

public class RegistrationService {

    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;

    public RegistrationService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public String register(String username, String password, String email) {
        try {
            // Check if the username already exists
            if (userDataAccess.getUser(username) != null) {
                throw new RegistrationException("Username already exists.");
            }

            // Create a new user
            userDataAccess.createUser(username, password, email);

            // Create an authorization token
            String authToken = createAuthToken(username);

            // Return the authorization token
            return new Gson().toJson(new Response(200, authToken, username));
        } catch (RegistrationException e) {
            // Handle registration exception
            return new Gson().toJson(new Response(400, e.getMessage()));
        } catch (Exception e) {
            // Handle other unexpected exceptions
            return new Gson().toJson(new Response(500, "Internal Server Error"));
        }
    }

    private String createAuthToken(String username) {
        // Generate an authorization token (You can implement this logic)
        // For example, you can generate a random string or use a hashing algorithm
        return "generated_auth_token_for_" + username;
    }
}

