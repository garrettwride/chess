//package serviceTests;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.List;
//import org.junit.jupiter.api.Test;
//import dataAccess.*;
//import dataModels.Game;
//import service.*;
//
//public class ListGamesTests {
//
//    private JoinGameService joinGameService;
//
//    ListGamesTests() {
//        DataMemory dataMemory = new DataMemory();
//        GameDataAccess gameDataAccess = new GameDataAccess(dataMemory);
//        this.joinGameService = new JoinGameService(gameDataAccess);
//    }
//
//    @Test
//    public void testListGamesSuccess() {
//        // Create some games
//        Game game1 = new Game(1, "whitePlayer", null, "Chess Game 1", new ChessGame());
//        Game game2 = new Game(2, "whitePlayer", "blackPlayer", "Chess Game 2", new ChessGame());
//        joinGameService.createGame(game1);
//        joinGameService.createGame(game2);
//
//        // List all games
//        List<Game> games = joinGameService.listGames();
//        assertNotNull(games);
//        assertFalse(games.isEmpty());
//        assertEquals(2, games.size());
//    }
//
//    @Test
//    public void testListGamesEmpty() {
//        // List games when no games are available
//        List<Game> games = joinGameService.listGames();
//        assertNotNull(games);
//        assertTrue(games.isEmpty());
//    }
//}

