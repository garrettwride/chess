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

        public String register(User newUser) throws RegistrationException {
            // Check if the username already exists
            User existingUser = userDataAccess.getUser(newUser.getUsername());
            if (existingUser != null) {
                throw new RegistrationException("Username already exists");
            }

            // Serialize the new user to JSON
            Gson gson = new Gson();
            return gson.toJson(newUser);
        }

        private String createAuthToken() {
            return UUID.randomUUID().toString();
        }
}

