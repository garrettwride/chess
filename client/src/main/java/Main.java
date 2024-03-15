import chess.*;
import ui.*;
import server.Server;


public class Main {
    public static void main(String[] args) {
        // Start the server
        Server server = new Server();
        int port = server.run(8080);

        // Continue with client initialization
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new Menu(serverUrl);
    }
}
