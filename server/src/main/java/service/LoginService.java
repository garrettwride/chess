package service;

import dataAccess.AuthDataAccess;
import dataAccess.UserDataAccess;
import model.UserData;

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
        } else if (authDataAccess.getAuthToken(username) != null) {
            authDataAccess.addAuthToken(username + "2");
            return authDataAccess.getAuthToken(username + "2");
        } else {
            // Generate an authToken and return it
            authDataAccess.addAuthToken(username);
            return authDataAccess.getAuthToken(username);
        }

    }

    public void deauthenticate(String authToken) throws AuthenticationException {
        // Check if the authToken is valid
        String username = authDataAccess.getUsername(authToken);

        if (username == null) {
            throw new AuthenticationException("Error: Unauthorized");
        }
        else {
            // delete an authToken
            authDataAccess.deleteAuthToken(username);
        }

    }

    private boolean isValidCredentials(String username, String password) {
        UserData checkUser = userDataAccess.getUser(username);
        return checkUser != null && Objects.equals(password, checkUser.getPassword());
    }
}

