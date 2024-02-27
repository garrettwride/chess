package service;

import dataAccess.AuthDataAccess;
import dataAccess.UserDataAccess;
import dataModels.User;

import java.util.Objects;

public class LoginService {
    private final AuthDataAccess authDataAccess;
    private final UserDataAccess userDataAccess;

    public LoginService(AuthDataAccess authDataAccess, UserDataAccess userDataAccess) {
        this.authDataAccess = authDataAccess;
        this.userDataAccess = userDataAccess;
    }

    public String authenticate(String username, String password) throws AuthenticationException {
        // Check if the username and password are valid
        if (!isValidCredentials(username, password)) {
            throw new AuthenticationException("Invalid username or password");
        }
        else {
            // Generate an authToken and return it

        }

    }

    private boolean isValidCredentials(String username, String password) {
        User checkUser = userDataAccess.getUser(username);
        return checkUser != null && Objects.equals(password, checkUser.getPassword());
    }
}

