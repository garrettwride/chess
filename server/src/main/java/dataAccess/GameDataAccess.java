package dataAccess;

import model.GameData;
import java.util.List;


public class GameDataAccess {
    private final DataMemory dataMemory;

    public GameDataAccess(DataMemory dataMemory) {
        this.dataMemory = dataMemory;
    }

    // Method to add a new game
    public void addGame(GameData game) {
        dataMemory.addGame(game);
    }

    // Method to get a game by gameID
    public GameData getGame(int gameID) {
        return dataMemory.getGame(gameID);
    }

    // Method to update a game
    public void updateGame(int gameID, String username, String teamColor) {
        GameData updatedGame = getGame(gameID);
        if (teamColor.equalsIgnoreCase("WHITE")) {
            updatedGame.setWhiteUsername(username);
        } else if (teamColor.equalsIgnoreCase("BLACK")) {
            updatedGame.setBlackUsername(username);
        } else {
            throw new IllegalArgumentException("Error: Invalid team color");
        }
        dataMemory.updateGame(gameID, updatedGame);
    }


    // Method to retrieve all games
    public List<GameData> getAllGames() {
        return dataMemory.getAllGames();
    }

    public void clear() {
        dataMemory.clearGames();
    }
}
