package serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import dataAccess.AuthDataAccess;
import org.junit.jupiter.api.Test;
import dataModels.User;
import service.*;
import dataAccess.*;

public class LoginTests {

    private LoginService loginService;
    private  RegistrationService registrationService;

    LoginTests() {
        DataMemory dataMemory = new DataMemory();
        UserDataAccess userDataAccess = new UserDataAccess(dataMemory);
        AuthDataAccess authDataAccess = new AuthDataAccess(dataMemory);
        this.loginService = new LoginService(authDataAccess, userDataAccess);
        this.registrationService = new RegistrationService(userDataAccess, authDataAccess);
    }

    @Test
    public void testLoginSuccess() {
        // Register a user
        User newUser = new User("testuser", "password123", "testuser@example.com");
        assertDoesNotThrow(() -> registrationService.register(newUser));

        // Login with correct credentials
        assertDoesNotThrow(() -> {
            String authToken = loginService.authenticate("testuser", "password123");
            assertNotNull(authToken);
        });
    }

    @Test
    public void testLoginFailureInvalidCredentials() {
        // Attempt to login with incorrect credentials
        assertThrows(AuthenticationException.class, () -> loginService.authenticate("invaliduser", "invalidpassword"));
    }
}

