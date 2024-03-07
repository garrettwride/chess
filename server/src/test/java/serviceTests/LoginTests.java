package serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import dataAccess.AuthDataAccess;
import org.junit.jupiter.api.*;
import model.UserData;
import service.*;
import dataAccess.*;

public class LoginTests {

    private LoginService loginService;
    private  RegistrationService registrationService;

    LoginTests() throws DataAccessException {
        DatabaseManager.createDatabase();
        UserDataAccess userDataAccess = new UserDataAccess();
        AuthDataAccess authDataAccess = new AuthDataAccess();
        this.loginService = new LoginService(authDataAccess, userDataAccess);
        this.registrationService = new RegistrationService(userDataAccess, authDataAccess);
    }

    @Test
    public void testLoginSuccess() {
        // Register a user
        UserData newUser = new UserData("testuser", "password123", "test@email");
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

