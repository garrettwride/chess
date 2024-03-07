package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {
    private static final int SIZE = 8;
    public ChessPiece[][] squares = new ChessPiece[SIZE][SIZE];
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
    public void removePiece(ChessPosition position) {
        squares[position.getRow() - 1][position.getColumn() - 1] = null;
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
        // Clear the board
        squares = new ChessPiece[SIZE][SIZE];

        // Define the piece types and their initial positions
        ChessPiece.PieceType[] whitePieces = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };
        ChessPiece.PieceType[] blackPieces = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };

        // Set up white pieces
        placePieces(1, ChessGame.TeamColor.WHITE, whitePieces);
        // Set up black pieces
        placePieces(8, ChessGame.TeamColor.BLACK, blackPieces);

        // Set up white pawns
        placePawns(2, ChessGame.TeamColor.WHITE);
        // Set up black pawns
        placePawns(7, ChessGame.TeamColor.BLACK);
    }

    private void placePieces(int row, ChessGame.TeamColor color, ChessPiece.PieceType[] pieces) {
        for (int i = 1; i <= pieces.length; i++) {
            addPiece(new ChessPosition(row, i), new ChessPiece(color, pieces[i - 1]));
        }
    }

    private void placePawns(int row, ChessGame.TeamColor color) {
        for (int i = 1; i <= SIZE; i++) {
            addPiece(new ChessPosition(row, i), new ChessPiece(color, ChessPiece.PieceType.PAWN));
        }
    }


    @Override
    public ChessBoard clone() {
        try {
            ChessBoard clonedBoard = (ChessBoard) super.clone();
            clonedBoard.squares = new ChessPiece[SIZE][SIZE];
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (this.squares[i][j] != null) {
                        clonedBoard.squares[i][j] = (ChessPiece) this.squares[i][j].clone();
                    }
                }
            }
            return clonedBoard;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
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
                "squares=" + Arrays.deepToString(squares) +
                '}';
    }
}
