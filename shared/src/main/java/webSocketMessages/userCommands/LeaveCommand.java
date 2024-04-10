package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {
    private final int gameID;

    public LeaveCommand(int gameID, String authToken) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
