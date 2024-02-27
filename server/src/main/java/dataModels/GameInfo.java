package dataModels;

import com.google.gson.annotations.SerializedName;

public class GameInfo {
    @SerializedName("playerColor")
    private String playerColor;

    @SerializedName("gameID")
    private int gameID;

    // Getters for playerColor and gameID
    public String getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }
}
