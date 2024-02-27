package service;

import com.google.gson.Gson;
import dataAccess.*;
import dataModels.*;

public class RegistrationService {
        private final UserDataAccess userDataAccess;
        private final AuthDataAccess authDataAccess;

        public RegistrationService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
            this.userDataAccess = userDataAccess;
            this.authDataAccess = authDataAccess;
        }

        public AuthData register(User newUser) throws RegistrationException {
            // Check if the username already exists
            User existingUser = userDataAccess.getUser(newUser.getUsername());
            if (existingUser != null) {
                throw new IllegalStateException("Username already exists");
            } else {
                userDataAccess.addUser(newUser);
                authDataAccess.addAuthToken(newUser.getUsername());

                return authDataAccess.getAuthData(newUser.getUsername());
            }

        }
}

