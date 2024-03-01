package service;

import dataAccess.*;
import model.*;

public class RegistrationService {
        private final UserDataAccess userDataAccess;
        private final AuthDataAccess authDataAccess;

        public RegistrationService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
            this.userDataAccess = userDataAccess;
            this.authDataAccess = authDataAccess;
        }

        public AuthData register(UserData newUser) throws RegistrationException {
            // Check if the username already exists
            UserData existingUser = userDataAccess.getUser(newUser.getUsername());
            if (existingUser != null) {
                throw new IllegalStateException("Username already exists");
            } else {
                userDataAccess.addUser(newUser);
                String authToken = authDataAccess.addAuthToken(newUser.getUsername());

                return authDataAccess.getAuthData(authToken);
            }

        }
}

