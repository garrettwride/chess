package serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import dataAccess.*;
import model.UserData;
import service.*;

public class LogoutTests {

    private LoginService loginService;
    private RegistrationService registrationService;
    private AuthDataAccess authDataAccess;
    private UserDataAccess userDataAccess;

    LogoutTests() {
        DataMemory dataMemory = new DataMemory();
        userDataAccess = new UserDataAccess(dataMemory);
        authDataAccess = new AuthDataAccess(dataMemory);
        loginService = new LoginService(authDataAccess, userDataAccess);
        registrationService = new RegistrationService(userDataAccess, authDataAccess);
    }

    @Test
    public void testLogoutSuccess() throws AuthenticationException, RegistrationException {
        // Register a user
        UserData newUser = new UserData("testuser", "password123", "testuser@example.com");
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

