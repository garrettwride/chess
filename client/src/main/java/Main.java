import chess.*;
import ui.*;


public class Main {
    public static void main(String[] args) {

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
