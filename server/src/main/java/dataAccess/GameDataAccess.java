package dataAccess;

import chess.ChessGame;
import dataModels.Game;

public class GameDataAccess {
    private final DataMemory dataMemory;

    public GameDataAccess(DataMemory dataMemory) {
        this.dataMemory = dataMemory;
    }

    // Method to add a new user
    public void addGame(Game game) {
        dataMemory.addGame(game);
    }

    // Method to retrieve a game by username
    public Game getGame(String username) {
        return dataMemory.getGame(username);
    }

    public void clear() {
        dataMemory.clearGames();
    }
}
