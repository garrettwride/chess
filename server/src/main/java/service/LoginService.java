package service;

import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
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

    public String authenticate(String username, String password) throws AuthenticationException, DataAccessException {
        // Check if the username and password are valid
        if (!isValidCredentials(username, password)) {
            throw new AuthenticationException("Invalid username or password");
        } else {
            // Generate an authToken and return it
            return authDataAccess.addAuthToken(username);
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
            authDataAccess.deleteAuthToken(authToken);
        }

    }

    private boolean isValidCredentials(String username, String password) throws DataAccessException {
        UserData checkUser = userDataAccess.getUser(username);
        return checkUser != null && Objects.equals(password, checkUser.getPassword());
    }
}
