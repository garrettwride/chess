package model;

import chess.ChessGame;
import com.google.gson.Gson;

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

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }
    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }


    public String toString() {
        return new Gson().toJson(this);
    }
}
