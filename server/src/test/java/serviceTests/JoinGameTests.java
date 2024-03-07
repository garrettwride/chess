package serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import dataAccess.*;
import model.*;
import service.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JoinGameTests {

    private JoinGameService joinGameService;
    private RegistrationService registrationService;
    private LoginService loginService;

    JoinGameTests() {
        GameDataAccess gameDataAccess = new GameDataAccess();
        AuthDataAccess authDataAccess = new AuthDataAccess();
        UserDataAccess userDataAccess = new UserDataAccess();
        this.joinGameService = new JoinGameService(gameDataAccess, authDataAccess);
        this.registrationService = new RegistrationService(userDataAccess, authDataAccess);
        this.loginService = new LoginService(authDataAccess, userDataAccess);
    }

    @Test
    public void testJoinGameSuccess() throws RegistrationException, AuthenticationException, DataAccessException, SQLException {
        // Register white player
        UserData whiteUser = new UserData("whitePlayer", "password123", "test@email");
        registrationService.register(whiteUser);

        // Register black player
        UserData blackUser = new UserData("blackPlayer", "password456", "test@email2");
        registrationService.register(blackUser);

        // Login white player to obtain an authToken
        String whiteAuthToken = loginService.authenticate("whitePlayer", "password123");

        // Create a game
        String gameName = "Chess Game 1";
        int gameID = joinGameService.createGame(gameName, whiteAuthToken);

        // Join the game as black player
        joinGameService.joinGame(whiteAuthToken, "White", gameID);

        // List all games
        List<GameData> games = joinGameService.listGames(whiteAuthToken);
        assertFalse(games.isEmpty());

        // Find the game by ID
        Optional<GameData> optionalGame = games.stream().filter(g -> g.getGameID() == gameID).findFirst();
        assertTrue(optionalGame.isPresent());

        // Assert properties of the game
        GameData game = optionalGame.get();
        assertEquals("whitePlayer", game.getWhiteUsername());
    }


    @Test
    public void testJoinGameFailureGameNotFound() throws RegistrationException, AuthenticationException, DataAccessException {
        // Register black player
        UserData blackUser = new UserData("blackPlayer", "password456", "test@email");
        registrationService.register(blackUser);

        // Login black player to obtain an authToken
        String blackAuthToken = loginService.authenticate("blackPlayer", "password456");

        // Attempt to join a non-existing game
        assertThrows(IllegalArgumentException.class, () -> joinGameService.joinGame(blackAuthToken, "BLACK", 1000));
    }
}


