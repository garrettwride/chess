package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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
        int row = position.getRow();
        int col = position.getColumn();

        if (row > 0 && row < 9 && col > 0 && col < 9) {
            ChessPiece piece = board.getPiece(position);

            if (piece != null && piece.getTeamColor() != this.getTeamColor()) {
                return true;
            }
            if (piece == null) {
                return true;
            }
        }
        return false;
    }


    private HashSet<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> positionsSet = new HashSet<>();
        if (checkPosition(board, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1))) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1)));
        }

        if (checkPosition(board, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1))) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1)));
        }

        if (checkPosition(board, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()))) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())));
        }

        if (checkPosition(board, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()))) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())));
        }

        if (checkPosition(board, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1))) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)));
        }

        if (checkPosition(board, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1))) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)));
        }

        if (checkPosition(board, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1))) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1)));
        }

        if (checkPosition(board, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1))) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)));
        }

        return positionsSet;
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
        HashSet<ChessMove> positionsSet = new HashSet<>();

        //check piece type
        if (this.getPieceType() == PieceType.KING){
            positionsSet = kingMoves(board, myPosition);
        }

        return positionsSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "type=" + type +
                ", pieceColor=" + pieceColor +
                '}';
    }
}
