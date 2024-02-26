package service;

import com.google.gson.Gson;
import dataAccess.*;
import dataModels.*;
import java.util.UUID;

public class RegistrationService {
        private final UserDataAccess userDataAccess;

        public RegistrationService(UserDataAccess userDataAccess) {
            this.userDataAccess = userDataAccess;
        }

        public String register(String username, String password, String email) throws RegistrationException {
            // Check if the username already exists
            User existingUser = userDataAccess.getUser(username);
            if (existingUser != null) {
                throw new RegistrationException("Username already exists");
            }

            // Create a new user
            User newUser = new User(username, password, email);
            userDataAccess.addUser(newUser);

            // Serialize the new user to JSON
            Gson gson = new Gson();
            return gson.toJson(newUser);
        }

        private String createAuthToken() {
            return UUID.randomUUID().toString();
        }
}

