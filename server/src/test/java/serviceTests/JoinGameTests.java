package serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import dataAccess.*;
import model.*;
import service.*;

public class JoinGameTests {

    private JoinGameService joinGameService;
    private RegistrationService registrationService;
    private LoginService loginService;

    JoinGameTests() {
        DataMemory dataMemory = new DataMemory();
        GameDataAccess gameDataAccess = new GameDataAccess(dataMemory);
        AuthDataAccess authDataAccess = new AuthDataAccess(dataMemory);
        UserDataAccess userDataAccess = new UserDataAccess(dataMemory);
        this.joinGameService = new JoinGameService(gameDataAccess, authDataAccess);
        this.registrationService = new RegistrationService(userDataAccess, authDataAccess);
        this.loginService = new LoginService(authDataAccess, userDataAccess);
    }

    @Test
    public void testJoinGameSuccess() throws RegistrationException, AuthenticationException {
        // Register white player
        UserData whiteUser = new UserData("whitePlayer", "password123", "white@example.com");
        registrationService.register(whiteUser);

        // Register black player
        UserData blackUser = new UserData("blackPlayer", "password456", "black@example.com");
        registrationService.register(blackUser);

        // Login white player to obtain an authToken
        String whiteAuthToken = loginService.authenticate("whitePlayer", "password123");

        // Create a game
        String gameName = "Chess Game 1";
        int gameID = joinGameService.createGame(gameName, whiteAuthToken);

        // Join the game as black player
        joinGameService.joinGame(whiteAuthToken, "White", gameID);

        GameData game = joinGameService.getGameByID(gameID); // Assuming there's a method to retrieve game by ID
        assertNotNull(game);
        assertEquals("whitePlayer", game.getWhiteUsername());
    }


    @Test
    public void testJoinGameFailureGameNotFound() throws RegistrationException, AuthenticationException {
        // Register black player
        UserData blackUser = new UserData("blackPlayer", "password456", "black@example.com");
        registrationService.register(blackUser);

        // Login black player to obtain an authToken
        String blackAuthToken = loginService.authenticate("blackPlayer", "password456");

        // Attempt to join a non-existing game
        assertThrows(IllegalArgumentException.class, () -> joinGameService.joinGame(blackAuthToken, "BLACK", 1000));
    }
}


