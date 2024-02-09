package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard gameBoard = new ChessBoard();

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    private Collection<ChessMove> allMoves(TeamColor teamColor) {
        HashSet<ChessMove> positionsSet = new HashSet<>();
        for (int i = 1; i < 9; ++i) {
            for (int j = 1; j < 9; ++j) {
                ChessPosition piecePosition = new ChessPosition(i, j);
                ChessPiece currentPiece = gameBoard.getPiece(piecePosition);
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    positionsSet.addAll(currentPiece.pieceMoves(gameBoard, piecePosition));
                }
            }
        }
        return positionsSet;
    }

    public ChessPosition findKing(TeamColor teamColor) {
        for (int i = 1; i <= ChessBoard.SIZE; i++) {
            for (int j = 1; j <= ChessBoard.SIZE; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = getPiece(position);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return position;
                }
            }
        }
        return null; // King not found
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        HashSet<ChessMove> positionsSet = new HashSet<>();

        if (gameBoard.getPiece(startPosition) != null){
            ChessPiece currentPiece = gameBoard.getPiece(startPosition);
            positionsSet = (HashSet<ChessMove>) currentPiece.pieceMoves(gameBoard, startPosition);



            return positionsSet;
        }
        else {
            return null;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        if(teamColor == TeamColor.WHITE){

                    for(ChessMove move : positionsSet) {
                        if(gameBoard.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPosition = gameBoard.findKing(teamColor);
        TeamColor opponentColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        Collection<ChessMove> opponentMoves = allMoves(opponentColor);
        for (ChessMove move : opponentMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        throw new RuntimeException("Not implemented");
    }

    }

@Override
public int hashCode() {
    return super.hashCode();
}

@Override
public boolean equals(Object obj) {
    return super.equals(obj);
}


@Override
public String toString() {
    return "$classname{}";
} {
} {
} {
}


