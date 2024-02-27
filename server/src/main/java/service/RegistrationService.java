package service;

import com.google.gson.Gson;
import dataAccess.*;
import dataModels.*;

public class RegistrationService {
        private final UserDataAccess userDataAccess;

        public RegistrationService(UserDataAccess userDataAccess) {
            this.userDataAccess = userDataAccess;
        }

        public String register(User newUser) throws RegistrationException {
            // Check if the username already exists
            User existingUser = userDataAccess.getUser(newUser.getUsername());
            if (existingUser != null) {
                throw new IllegalStateException("Username already exists");
            } else {
                userDataAccess.addUser(newUser);
                Gson gson = new Gson();
                return gson.toJson(newUser);
            }

            // Serialize the new user to JSON

        }
}

