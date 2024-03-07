package serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import dataAccess.*;
import model.UserData;
import service.*;

import java.security.NoSuchAlgorithmException;

public class LogoutTests {

    private LoginService loginService;
    private RegistrationService registrationService;
    private AuthDataAccess authDataAccess;
    private UserDataAccess userDataAccess;

    LogoutTests() {
        userDataAccess = new UserDataAccess();
        authDataAccess = new AuthDataAccess();
        loginService = new LoginService(authDataAccess, userDataAccess);
        registrationService = new RegistrationService(userDataAccess, authDataAccess);
    }

    @Test
    public void testLogoutSuccess() throws AuthenticationException, RegistrationException, DataAccessException, NoSuchAlgorithmException {
        // Register a user
        UserData newUser = new UserData("testuser", "password123", "test@email");
        registrationService.register(newUser);

        // Login to obtain an authToken
        String authToken = loginService.authenticate("testuser", "password123");

        // Logout
        assertDoesNotThrow(() -> {
            loginService.deauthenticate(authToken);
        });

        // Check if authToken is invalidated
        assertNull(authDataAccess.getAuthToken("testuser"));
    }

    @Test
    public void testLogoutFailureInvalidAuthToken() {
        // Attempt to logout with an invalid authToken
        assertThrows(AuthenticationException.class, () -> loginService.deauthenticate("invalidAuthToken"));
    }
}

