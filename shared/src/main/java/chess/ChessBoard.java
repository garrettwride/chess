package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    public ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (ChessPiece[] col : squares){
            for (ChessPiece piece : col){
                piece = null;
            }
        }

        ChessPosition rook1b_position;
        rook1b_position = new ChessPosition(1,1);
        ChessPiece rook1b;
        rook1b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPiece(rook1b_position, rook1b);

        ChessPosition rook1w_position;
        rook1w_position = new ChessPosition(8,1);
        ChessPiece rook1w;
        rook1w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPiece(rook1w_position, rook1w);

        ChessPosition knight1b_position;
        knight1b_position = new ChessPosition(1, 2);
        ChessPiece knight1b;
        knight1b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT); // Change the piece type to KNIGHT
        addPiece(knight1b_position, knight1b);

        ChessPosition knight1w_position;
        knight1w_position = new ChessPosition(8, 2);
        ChessPiece knight1w;
        knight1w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT); // Set the color to WHITE and piece type to KNIGHT
        addPiece(knight1w_position, knight1w);

        ChessPosition bishop1b_position;
        bishop1b_position = new ChessPosition(1, 3);
        ChessPiece bishop1b;
        bishop1b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPiece(bishop1b_position, bishop1b);

        ChessPosition bishop1w_position;
        bishop1w_position = new ChessPosition(8, 3);
        ChessPiece bishop1w;
        bishop1w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPiece(bishop1w_position, bishop1w);

        ChessPosition queenb_position;
        queenb_position = new ChessPosition(1, 4);
        ChessPiece queenb;
        queenb = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        addPiece(queenb_position, queenb);

        ChessPosition queenw_position;
        queenw_position = new ChessPosition(8, 4);
        ChessPiece queenw;
        queenw = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        addPiece(queenw_position, queenw);

        ChessPosition kingb_position;
        kingb_position = new ChessPosition(1, 5);
        ChessPiece kingb;
        kingb = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        addPiece(kingb_position, kingb);

        ChessPosition kingw_position;
        kingw_position = new ChessPosition(8, 5);
        ChessPiece kingw;
        kingw = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        addPiece(kingw_position, kingw);

        ChessPosition bishop2b_position;
        bishop2b_position = new ChessPosition(1, 6);
        ChessPiece bishop2b;
        bishop2b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPiece(bishop2b_position, bishop2b);

        ChessPosition bishop2w_position;
        bishop2w_position = new ChessPosition(8, 6);
        ChessPiece bishop2w;
        bishop2w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPiece(bishop2w_position, bishop2w);

        ChessPosition knight2b_position;
        knight2b_position = new ChessPosition(1, 7);
        ChessPiece knight2b;
        knight2b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPiece(knight2b_position, knight2b);

        ChessPosition knight2w_position;
        knight2w_position = new ChessPosition(8, 7);
        ChessPiece knight2w;
        knight2w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPiece(knight2w_position, knight2w);

        ChessPosition rook2b_position;
        rook2b_position = new ChessPosition(1, 8);
        ChessPiece rook2b;
        rook2b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPiece(rook2b_position, rook2b);

        ChessPosition rook2w_position;
        rook2w_position = new ChessPosition(8, 8);
        ChessPiece rook2w;
        rook2w = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPiece(rook2w_position, rook2w);

        for (int i = 1; i < 9; i++){
            ChessPosition pawnb_position;
            pawnb_position = new ChessPosition(2, i);
            ChessPiece pawnb;
            pawnb = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            addPiece(pawnb_position, pawnb);
        }

        for (int i = 1; i < 9; i++) {
            ChessPosition pawnw_position;
            pawnw_position = new ChessPosition(7, i);
            ChessPiece pawnw;
            pawnw = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            addPiece(pawnw_position, pawnw);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }
}
