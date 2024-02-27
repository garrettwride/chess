package dataModels;

import chess.ChessGame;
import com.google.gson.Gson;

public record Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    // Constructor to handle null values for whiteUsername and blackUsername
    public Game {
        if (whiteUsername == null || whiteUsername.isEmpty()) {
            whiteUsername = null;
        }
        if (blackUsername == null || blackUsername.isEmpty()) {
            blackUsername = null;
        }
    }

    public ChessGame getGame() {
        return game;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getWhiteUsername() {
        return whiteUsername;
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
