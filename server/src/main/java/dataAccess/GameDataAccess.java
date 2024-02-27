package dataAccess;

import chess.ChessGame;
import dataModels.Game;
import java.util.List;


public class GameDataAccess {
    private final DataMemory dataMemory;

    public GameDataAccess(DataMemory dataMemory) {
        this.dataMemory = dataMemory;
    }

    // Method to add a new game
    public void addGame(Game game) {
        dataMemory.addGame(game);
    }

    // Method to get a game by gameID
    public Game getGame(int gameID) {
        return dataMemory.getGame(gameID);
    }

    // Method to update a game
    public void updateGame(int gameID, String username) {
        dataMemory.updateGame(gameID, username);
    }

    // Method to retrieve all games
    public List<Game> getAllGames() {
        return dataMemory.getAllGames();
    }

    public void clear() {
        dataMemory.clearGames();
    }
}
