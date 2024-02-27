//package serviceTests;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.Test;
//import dataAccess.*;
//import dataModels.Game;
//import service.*;
//
//public class CreateGameTest {
//
//    private JoinGameService joinGameService;
//
//    CreateGameTest() {
//        DataMemory dataMemory = new DataMemory();
//        GameDataAccess gameDataAccess = new GameDataAccess(dataMemory);
//        this.joinGameService = new JoinGameService(gameDataAccess);
//    }
//
//    @Test
//    public void testCreateGameSuccess() {
//        // Create a new game
//        Game newGame = new Game(1, "whitePlayer", null, "Chess Game 1", new ChessGame());
//        assertDoesNotThrow(() -> joinGameService.createGame(newGame));
//    }
//
//    @Test
//    public void testCreateGameFailureDuplicateID() {
//        // Create a game with duplicate ID
//        Game newGame = new Game(1, "whitePlayer", null, "Chess Game 1", new ChessGame());
//        joinGameService.createGame(newGame);
//
//        // Attempt to create another game with the same ID
//        assertThrows(GameIDAlreadyExistsException.class, () -> joinGameService.createGame(newGame));
//    }
//}

