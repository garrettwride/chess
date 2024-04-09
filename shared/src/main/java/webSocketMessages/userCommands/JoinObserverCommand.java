package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand {
    private final int gameID;

    public JoinObserverCommand(int gameID, String authToken) {
        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
