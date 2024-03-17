package model;

import com.google.gson.annotations.SerializedName;

public class GameInfo {
    @SerializedName("teamColor")
    private String teamColor;

    @SerializedName("gameID")
    private Integer gameID;

    // Getters and setters for teamColor and gameID
    public String getPlayerColor() {
        return teamColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setPlayerColor(String teamColor) {
        this.teamColor = teamColor;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}


