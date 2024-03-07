package serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import dataAccess.*;
import model.UserData;
import service.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

public class LogoutTests {

    private AuthDataAccess authDataAccess = new AuthDataAccess();
    private RegistrationService registrationService;
    private LoginService loginService;
    private Connection connection;
    private ApplicationService applicationService;
    @BeforeEach
    public void setUp() throws Exception {
        DatabaseManager.createDatabase();
        GameDataAccess gameDataAccess = new GameDataAccess();
        UserDataAccess userDataAccess = new UserDataAccess();
        this.registrationService = new RegistrationService(userDataAccess, authDataAccess);
        this.loginService = new LoginService(authDataAccess, userDataAccess);
        applicationService = new ApplicationService(userDataAccess, gameDataAccess, authDataAccess);
        connection = DatabaseManager.getConnection();
        connection.setAutoCommit(false);
        applicationService.clear();
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

