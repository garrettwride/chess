package model;

import chess.ChessGame;
import com.google.gson.Gson;

import java.util.Collection;
import java.util.Objects;

public class GameData {
    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame game;

    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
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

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }


    public String toString() {
        return new Gson().toJson(this);
    }

    public boolean containsPlayer(String username) {
        if (Objects.equals(username, blackUsername) || Objects.equals(username, whiteUsername)){
            return true;
        } else {
            return false;
        }
    }
}
