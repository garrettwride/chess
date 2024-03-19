package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import exception.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static String serverUrl = "http://localhost:8080";
    private static Server server = new Server();
    private static ServerFacade facade = new ServerFacade(serverUrl);

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void testAddGame_Positive() {
        // Positive test for adding a game
        String gameName = "TestGame";
        String authToken = "validAuthToken";

        assertDoesNotThrow(() -> {
            String response = facade.addGame(gameName, authToken);
            assertNotNull(response);
            // Add additional assertions if needed
        });
    }

    @Test
    public void testAddGame_Negative_InvalidAuthToken() {
        // Negative test for adding a game with an invalid auth token
        String gameName = "TestGame";
        String invalidAuthToken = "invalidAuthToken";

        assertThrows(ResponseException.class, () -> {
            facade.addGame(gameName, invalidAuthToken);
        });
    }

    // Write similar positive and negative tests for other methods like authenticate, deauthenticate, joinGame, listGames, and register
}

