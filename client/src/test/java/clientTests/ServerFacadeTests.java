package clientTests;

import com.google.gson.JsonArray;
import model.AuthData;
import model.GameInfo;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import exception.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static String serverUrl;
    private static Server server = new Server();
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverUrl = "http://localhost:" + port;
        facade = new ServerFacade(serverUrl);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    void clearDatabase() throws ResponseException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void testAddGame_Positive() throws ResponseException {
        String username = "newUser";
        String password = "newPassword";
        String email = "newUser@example.com";
        UserData userData = new UserData(username, password, email);
        String gameName = "TestGame";
        String authToken = facade.register(userData).getAuthToken();

        assertDoesNotThrow(() -> {
            String response = facade.addGame(gameName, authToken);
            assertNotNull(response);
        });
    }

    @Test
    public void testAddGame_Negative_InvalidAuthToken() {
        String gameName = "TestGame";
        String invalidAuthToken = "invalidAuthToken";

        assertThrows(ResponseException.class, () -> {
            facade.addGame(gameName, invalidAuthToken);
        });
    }

    @Test
    public void testAuthenticate_Positive() throws ResponseException {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@email";

        UserData userData = new UserData(username, password, email);
        facade.register(userData);

        try {
            AuthData authData = facade.authenticate(userData);
            assertNotNull(authData.getAuthToken());
        } catch (ResponseException e) {
            // If an exception occurs, fail the test
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testAuthenticate_Negative_InvalidCredentials() {
        String username = "testUser";
        String password = "testPassword";

        UserData userData = new UserData(username, password, null);

        try {
            facade.authenticate(userData);
            fail("Expected ResponseException to be thrown");
        } catch (ResponseException e) {
            assertEquals(500, e.statusCode());
        }
    }

    @Test
    public void testDeauthenticate_Positive() throws ResponseException {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@email";

        UserData userData = new UserData(username, password, email);
        facade.register(userData);
        String authToken = facade.authenticate(userData).getAuthToken();

        try {
            facade.deauthenticate(authToken);
        } catch (ResponseException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testDeauthenticate_Negative_InvalidAuthToken() {
        // Negative test for deauthenticating with an invalid auth token
        String authToken = "invalidAuthToken";

        try {
            // Attempt to deauthenticate with an invalid auth token
            facade.deauthenticate(authToken);
            // If no exception is thrown, fail the test
            fail("Expected ResponseException to be thrown");
        } catch (ResponseException e) {
            // Assert that the exception message or status code is as expected
            assertEquals(500, e.statusCode());
            // Add more assertions if needed
        }
    }

    @Test
    public void testRegister_Positive() {
        String username = "newUser";
        String password = "newPassword";
        String email = "newUser@example.com";
        UserData userData = new UserData(username, password, email);

        try {
            AuthData authData = facade.register(userData);
            assertNotNull(authData);
        } catch (ResponseException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testRegister_Negative_DuplicateUsername() {
        String username = "existingUser";
        String password = "password";
        String email = "existingUser@example.com";
        UserData userData = new UserData(username, password, email);

        try {
            facade.register(userData);
            facade.register(userData);
            fail("Expected ResponseException to be thrown");
        } catch (ResponseException e) {
            assertEquals(500, e.statusCode());
        }
    }

    @Test
    public void testJoinGame_Positive() throws ResponseException {
        String username = "newUser";
        String password = "newPassword";
        String email = "newUser@example.com";
        UserData userData = new UserData(username, password, email);
        String gameName = "TestGame";
        String authToken = facade.register(userData).getAuthToken();
        Integer gameID = Integer.parseInt(facade.addGame(gameName, authToken));
        GameInfo gameInfo = new GameInfo();
        gameInfo.setPlayerColor("BLACK");
        gameInfo.setGameID(gameID);

        try {
            facade.joinGame(gameInfo, authToken);
            // Assert success here
        } catch (ResponseException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testJoinGame_Negative_InvalidGameID() throws ResponseException {
        String username = "newUser";
        String password = "newPassword";
        String email = "newUser@example.com";
        UserData userData = new UserData(username, password, email);
        String authToken = facade.register(userData).getAuthToken();
        GameInfo gameInfo = new GameInfo();
        gameInfo.setPlayerColor("BLACK");
        gameInfo.setGameID(-1); // Invalid game ID

        try {
            facade.joinGame(gameInfo, authToken);
            fail("Expected ResponseException to be thrown");
        } catch (ResponseException e) {
            assertEquals(500, e.statusCode());
        }
    }

    @Test
    public void testListGames_Positive() throws ResponseException {
        String username = "newUser";
        String password = "newPassword";
        String email = "newUser@example.com";
        UserData userData = new UserData(username, password, email);
        String gameName = "TestGame";
        String authToken = facade.register(userData).getAuthToken();
        facade.addGame(gameName, authToken);
        try {
            JsonArray games = facade.listGames(authToken);
            assertNotNull(games);
        } catch (ResponseException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testListGames_Negative_Unauthorized() throws ResponseException {
        String username = "newUser";
        String password = "newPassword";
        String email = "newUser@example.com";
        UserData userData = new UserData(username, password, email);
        String gameName = "TestGame";
        String authToken = facade.register(userData).getAuthToken();
        facade.addGame(gameName, authToken);

        try {
            facade.listGames(null); // No authentication token provided
            fail("Expected ResponseException to be thrown");
        } catch (ResponseException e) {
            assertEquals(500, e.statusCode());
        }
    }

}

