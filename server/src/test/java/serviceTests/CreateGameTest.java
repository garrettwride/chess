package serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import model.UserData;
import org.junit.jupiter.api.Test;
import dataAccess.*;
import model.GameData;
import chess.ChessGame;
import service.*;

public class CreateGameTest {

    private JoinGameService joinGameService;
    private RegistrationService registrationService;
    private LoginService loginService;

    CreateGameTest() {
        DataMemory dataMemory = new DataMemory();
        GameDataAccess gameDataAccess = new GameDataAccess(dataMemory);
        AuthDataAccess authDataAccess = new AuthDataAccess(dataMemory);
        UserDataAccess userDataAccess = new UserDataAccess(dataMemory);
        this.joinGameService = new JoinGameService(gameDataAccess, authDataAccess);
        this.registrationService = new RegistrationService(userDataAccess, authDataAccess);
        this.loginService = new LoginService(authDataAccess, userDataAccess);
    }

    @Test
    public void testCreateGameSuccess() throws RegistrationException, AuthenticationException {
        UserData newUser = new UserData("testuser", "password123", "testuser@example.com");
        registrationService.register(newUser);

        // Login to obtain an authToken
        String authToken = loginService.authenticate("testuser", "password123");
        // Create a new game
        String gameName = "MyGame";
        assertDoesNotThrow(() -> joinGameService.createGame(gameName, authToken));
    }

    @Test
    public void testCreateGameFailureDuplicateID() throws RegistrationException, AuthenticationException {
        UserData newUser = new UserData("testuser", "password123", "testuser@example.com");
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

