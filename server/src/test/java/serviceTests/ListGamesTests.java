package serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import model.*;
import org.junit.jupiter.api.Test;
import dataAccess.*;
import service.*;

import java.util.List;

public class ListGamesTests {

    private JoinGameService joinGameService;
    private RegistrationService registrationService;
    private LoginService loginService;

    ListGamesTests() {
        DataMemory dataMemory = new DataMemory();
        GameDataAccess gameDataAccess = new GameDataAccess(dataMemory);
        AuthDataAccess authDataAccess = new AuthDataAccess(dataMemory);
        UserDataAccess userDataAccess = new UserDataAccess(dataMemory);
        this.joinGameService = new JoinGameService(gameDataAccess, authDataAccess);
        this.registrationService = new RegistrationService(userDataAccess, authDataAccess);
        this.loginService = new LoginService(authDataAccess, userDataAccess);
    }

    @Test
    public void testListGamesSuccess() throws RegistrationException, AuthenticationException {
        // Register a user
        UserData newUser = new UserData("testuser", "password123", "testuser@example.com");
        registrationService.register(newUser);

        // Login to obtain an authToken
        String authToken = loginService.authenticate("testuser", "password123");

        // Create some games
        String gameName1 = "Chess Game 1";
        String gameName2 = "Chess Game 2";
        joinGameService.createGame(gameName1, authToken);
        joinGameService.createGame(gameName2, authToken);

        // List all games
        List<GameData> games = joinGameService.listGames(authToken);
        assertNotNull(games);
        assertFalse(games.isEmpty());
        assertEquals(2, games.size());
    }

    @Test
    public void testListGamesEmpty() throws RegistrationException, AuthenticationException {
        // Register a user
        UserData newUser = new UserData("testuser", "password123", "testuser@example.com");
        registrationService.register(newUser);

        // Login to obtain an authToken
        String authToken = loginService.authenticate("testuser", "password123");

        // List games when no games are available
        List<GameData> games = joinGameService.listGames(authToken);
        assertNotNull(games);
        assertTrue(games.isEmpty());
    }
}


