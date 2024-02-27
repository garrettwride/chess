package dataModels;

import chess.ChessGame;
import com.google.gson.Gson;

public record Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    public ChessGame getGame() {
        return game;
    }

    public int getGameID() {
        return gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
