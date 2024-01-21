package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessPiece.PieceType type;
    private ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    private boolean checkPosition(ChessBoard board, ChessPosition position) {
        if(board[position.getRow()][position.getColumn()].getTeamColor() != this.getTeamColor() & position.getRow() != 9 & position.getRow() != 0 & position.getColumn() != 9 & position.getColumn() != 0){
            return true;
        } else {
            return false;
        }
    }

    private HashSet<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> endPositionsSet = new HashSet<>();
        if (checkPosition(board, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1))){

        }
        return endPositionsSet;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //create new hash set
        HashSet<ChessMove> endPositionsSet = new HashSet<>();

        //check piece type
        if (this.getPieceType() == PieceType.KING){
            endPositionsSet = kingMoves(board, myPosition);
        }

        return endPositionsSet;
    }
}
