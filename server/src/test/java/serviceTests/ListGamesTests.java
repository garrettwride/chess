package serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import model.*;
import org.junit.jupiter.api.Test;
import dataAccess.*;
import service.*;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class ListGamesTests {

    private JoinGameService joinGameService;
    private RegistrationService registrationService;
    private LoginService loginService;

    ListGamesTests() {
        GameDataAccess gameDataAccess = new GameDataAccess();
        AuthDataAccess authDataAccess = new AuthDataAccess();
        UserDataAccess userDataAccess = new UserDataAccess();
        this.joinGameService = new JoinGameService(gameDataAccess, authDataAccess);
        this.registrationService = new RegistrationService(userDataAccess, authDataAccess);
        this.loginService = new LoginService(authDataAccess, userDataAccess);
    }

    @Test
    public void testListGamesSuccess() throws RegistrationException, AuthenticationException, DataAccessException, SQLException, NoSuchAlgorithmException {
        // Register a user
        UserData newUser = new UserData("testuser", "password123", "test@email");
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
    public void testListGamesEmpty() throws RegistrationException, AuthenticationException, DataAccessException, SQLException, NoSuchAlgorithmException {
        // Register a user
        UserData newUser = new UserData("testuser", "password123", "test@email");
        registrationService.register(newUser);

        // Login to obtain an authToken
        String authToken = loginService.authenticate("testuser", "password123");

        // List games when no games are available
        List<GameData> games = joinGameService.listGames(authToken);
        assertNotNull(games);
        assertTrue(games.isEmpty());
    }
}


