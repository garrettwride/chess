//package serviceTests;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.Test;
//import dataAccess.*;
//import dataModels.Game;
//import service.*;
//
//public class JoinGameTests {
//
//    private JoinGameService joinGameService;
//
//    JoinGameTests() {
//        DataMemory dataMemory = new DataMemory();
//        GameDataAccess gameDataAccess = new GameDataAccess(dataMemory);
//        this.joinGameService = new JoinGameService(gameDataAccess);
//    }
//
//    @Test
//    public void testJoinGameSuccess() {
//        // Create a game
//        Game newGame = new Game(1, "whitePlayer", null, "Chess Game 1", new ChessGame());
//        assertDoesNotThrow(() -> joinGameService.createGame(newGame));
//
//        // Join the game
//        String joinResult = joinGameService.joinGame("blackPlayer", 1);
//        assertNotNull(joinResult);
//        assertTrue(joinResult.contains("joined"));
//    }
//
//    @Test
//    public void testJoinGameFailureGameNotFound() {
//        // Attempt to join a non-existing game
//        assertThrows(GameNotFoundException.class, () -> joinGameService.joinGame("blackPlayer", 1000));
//    }
//}

