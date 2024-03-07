package serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import model.UserData;

import org.junit.jupiter.api.*;
import dataAccess.*;
import service.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

public class CreateGameTest {

    private JoinGameService joinGameService;
    private RegistrationService registrationService;
    private LoginService loginService;
    private Connection connection;
    private ApplicationService applicationService;
    @BeforeEach
    public void setUp() throws Exception {
        DatabaseManager.createDatabase();
        GameDataAccess gameDataAccess = new GameDataAccess();
        AuthDataAccess authDataAccess = new AuthDataAccess();
        UserDataAccess userDataAccess = new UserDataAccess();
        this.joinGameService = new JoinGameService(gameDataAccess, authDataAccess);
        this.registrationService = new RegistrationService(userDataAccess, authDataAccess);
        this.loginService = new LoginService(authDataAccess, userDataAccess);
        applicationService = new ApplicationService(userDataAccess, gameDataAccess, authDataAccess);
        connection = DatabaseManager.getConnection();
        connection.setAutoCommit(false);
        applicationService.clear();
    }

    @AfterEach
    public void tearDown() throws Exception {
        connection.rollback();
        connection.close();
    }

    @Test
    public void testCreateGameSuccess() throws RegistrationException, AuthenticationException, DataAccessException, NoSuchAlgorithmException {
        UserData newUser = new UserData("testuser", "password123", "test@email");
        registrationService.register(newUser);

        // Login to obtain an authToken
        String authToken = loginService.authenticate("testuser", "password123");
        // Create a new game
        String gameName = "MyGame";
        assertDoesNotThrow(() -> joinGameService.createGame(gameName, authToken));
    }

    @Test
    public void testCreateGameFailureDuplicateID() throws RegistrationException, AuthenticationException, DataAccessException, NoSuchAlgorithmException {
        UserData newUser = new UserData("testuser", "password123", "test@email");
        registrationService.register(newUser);

        // Login to obtain an authToken
        String authToken = loginService.authenticate("testuser", "password123");
        // Create a new game
        String gameName = "MyGame";
        assertDoesNotThrow(() -> joinGameService.createGame(gameName, authToken));

        String badAuthToken = "";

        assertThrows(AuthenticationException.class, () -> joinGameService.createGame(gameName, badAuthToken));
    }
}

