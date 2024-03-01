package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(gameBoard, chessGame.gameBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, gameBoard);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", gameBoard=" + gameBoard +
                '}';
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    private Collection<ChessMove> allMoves(TeamColor teamColor, ChessBoard board) {
        HashSet<ChessMove> positionsSet = new HashSet<>();
        for (int i = 1; i < 9; ++i) {
            for (int j = 1; j < 9; ++j) {
                ChessPosition piecePosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(piecePosition);
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> validMoves = currentPiece.pieceMoves(board, piecePosition);
                    positionsSet.addAll(validMoves);
                }
            }
        }
        return positionsSet;
    }

    public ChessPosition findKing(TeamColor color, ChessBoard board) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) {
                    return position;
                }
            }
        }
        return null;
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // Get the piece at the specified position
        ChessPiece currentPiece = gameBoard.getPiece(startPosition);

        // Check if the position is empty
        if (currentPiece == null) {
            return null;
        }

        // Get all possible moves for the piece
        Collection<ChessMove> allMoves = currentPiece.pieceMoves(gameBoard, startPosition);
        HashSet<ChessMove> validMoves = new HashSet<>();

        // Iterate through each move
        for (ChessMove move : allMoves) {
            // Clone the board and apply the move
            ChessBoard clonedBoard = gameBoard.clone();
            applyMove(move, clonedBoard);

            // Check if the move results in the team not being in check
            if (!isInCheck(teamTurn, clonedBoard)) {
                // Add the move to the set of valid moves
                validMoves.add(move);
            }
        }
        return validMoves;
    }
    private Collection<ChessMove> validMovesForTeam(TeamColor teamColor) {
        // HashSet to store valid moves
        HashSet<ChessMove> validMoves = new HashSet<>();

        // Iterate through each position on the board
        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) {
                ChessPosition piecePosition = new ChessPosition(i, j);
                ChessPiece currentPiece = gameBoard.getPiece(piecePosition);

                // Check if the piece exists and belongs to the specified team
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    // Get valid moves for the current piece
                    Collection<ChessMove> pieceMoves = currentPiece.pieceMoves(gameBoard, piecePosition);

                    // Iterate through each move
                    for (ChessMove move : pieceMoves) {
                        // Clone the board and apply the move
                        ChessBoard clonedBoard = gameBoard.clone();
                        applyMove(move, clonedBoard);

                        // Check if the move results in the team not being in check
                        if (!isInCheck(teamColor, clonedBoard)) {
                            // Add the move to the set of valid moves
                            validMoves.add(move);
                        }
                    }
                }
            }
        }
        return validMoves;
    }


    private Collection<ChessMove> allMoves(TeamColor teamColor) {
        // HashSet to store all possible moves
        HashSet<ChessMove> positionsSet = new HashSet<>();

        // Iterate through each position on the board
        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) {
                ChessPosition piecePosition = new ChessPosition(i, j);
                ChessPiece currentPiece = gameBoard.getPiece(piecePosition);

                // Check if the piece exists and belongs to the specified team
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    // Get valid moves for the current piece
                    Collection<ChessMove> validMoves = currentPiece.pieceMoves(gameBoard, piecePosition);

                    // Add the valid moves to the set of all moves
                    positionsSet.addAll(validMoves);
                }
            }
        }
        return positionsSet;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece movingPiece = gameBoard.getPiece(startPosition);

        if (movingPiece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Invalid move: not piece's team's turn");
        }

        if (movingPiece == null || !movingPiece.pieceMoves(gameBoard, startPosition).contains(move)) {
            throw new InvalidMoveException("Invalid move: " + move);
        }

        ChessBoard clonedBoard = gameBoard.clone();
        applyMove(move, clonedBoard);

        if (isInCheck(teamTurn, clonedBoard)) {
            throw new InvalidMoveException("Invalid move: puts own king in check");
        }

        applyMove(move);

        if (getTeamTurn() == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
        }
    }


    public void applyMove(ChessMove move, ChessBoard board) {
        if (move.getPromotionPiece() == null) {
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        }
        else {
            board.addPiece(move.getEndPosition(), new ChessPiece(getTeamTurn(), move.getPromotionPiece()));
        }
        board.removePiece(move.getStartPosition());
    }
    public void applyMove(ChessMove move) {
        if (move.getPromotionPiece() == null) {
            gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
        }
        else {
            gameBoard.addPiece(move.getEndPosition(), new ChessPiece(getTeamTurn(), move.getPromotionPiece()));
        }
        gameBoard.removePiece(move.getStartPosition());
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor, gameBoard);
        TeamColor opponentColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        Collection<ChessMove> opponentMoves = allMoves(opponentColor, gameBoard);
        for (ChessMove move : opponentMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPosition = findKing(teamColor, board);
        TeamColor opponentColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        Collection<ChessMove> opponentMoves = allMoves(opponentColor, board);
        for (ChessMove move : opponentMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
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
        if (!isInCheck(teamColor)) {
            return false;
        }

        return loopBoard(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }

        return loopBoard(teamColor);
    }

    private boolean loopBoard(TeamColor teamColor) {
        for (int i = 1; i < 9; ++i) {
            for (int j = 1; j < 9; ++j) {
                ChessPosition piecePosition = new ChessPosition(i, j);
                ChessPiece currentPiece = gameBoard.getPiece(piecePosition);
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> validMoves = validMoves(piecePosition);
                    if (!validMoves.isEmpty()){
                        return false;
                    }
                }
            }
        }

        return true;
    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.gameBoard;
    }
}
