package model;

import com.google.gson.annotations.SerializedName;

public class GameInfo {
    @SerializedName("playerColor")
    private String playerColor;

    @SerializedName("gameID")
    private Integer gameID; // Use Integer instead of int to allow null values

    @SerializedName("gameName")
    private String gameName;

    // Getters and setters for playerColor, gameID, and gameName
    public String getPlayerColor() {
        return playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    // Constructor to handle null values
    public GameInfo() {
        this.playerColor = null;
        this.gameID = null;
        this.gameName = null;
    }
}

