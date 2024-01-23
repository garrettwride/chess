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

    private int checkPosition(ChessBoard board, int row, int col) {

        if (row > 0 && row < 9 && col > 0 && col < 9) {
            ChessPosition position;
            position = new ChessPosition(row, col);
            ChessPiece piece = board.getPiece(position);

            if (piece != null && piece.getTeamColor() != this.getTeamColor()) {
                return 2;
            }
            if (piece == null) {
                return 1;
            }
        }
        return 0;
    }

    private ChessMove move(ChessBoard board, ChessPosition myPosition, int i, int j){
        int row = myPosition.getRow() + i;
        int col = myPosition.getColumn() + j;
        if (checkPosition(board, row, col) != 0) {
            return new ChessMove(myPosition, new ChessPosition(row, col));
        }
        return null;
    }


    private HashSet<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> positionsSet = new HashSet<>();

        positionsSet.add(move(board, myPosition, 1, 0));
        positionsSet.add(move(board, myPosition, 1, -1));
        positionsSet.add(move(board, myPosition, 0, -1));
        positionsSet.add(move(board, myPosition, -1, -1));
        positionsSet.add(move(board, myPosition, -1, 0));
        positionsSet.add(move(board, myPosition, -1, 1));
        positionsSet.add(move(board, myPosition, 0, 1));
        positionsSet.add(move(board, myPosition, 1, 1));

        return positionsSet;
    }

    private HashSet<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> positionsSet = new HashSet<>();

       positionsSet = rookMoves(board, myPosition);
       positionsSet.addAll(bishopMoves(board, myPosition));

        return positionsSet;
    }

    private HashSet<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> positionsSet = new HashSet<>();

        int i = 1;
        int j = 1;
        while ((checkPosition(board,myPosition.getRow() + i, myPosition.getColumn() + j)) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)));
            if ((checkPosition(board, myPosition.getRow() + i, myPosition.getColumn() + j)) == 2) {
                break;
            }
            ++i;
            ++j;
        }

        i = -1;
        j = -1;
        while ((checkPosition(board, myPosition.getRow() + i, myPosition.getColumn() + j)) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)));
            if ((checkPosition(board, myPosition.getRow() + i, myPosition.getColumn() + j)) == 2) {
                break;
            }
            --i;
            --j;
        }

        i = 1;
        j = -1;
        while ((checkPosition(board, myPosition.getRow() + i, myPosition.getColumn() + j)) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)));
            if ((checkPosition(board, myPosition.getRow() + i, myPosition.getColumn() + j)) == 2) {
                break;
            }
            ++i;
            --j;
        }

        i = -1;
        j = 1;
        while ((checkPosition(board, myPosition.getRow() + i, myPosition.getColumn() + j)) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)));
            if ((checkPosition(board, myPosition.getRow() + i, myPosition.getColumn() + j)) == 2) {
                break;
            }
            --i;
            ++j;
        }

        return positionsSet;
    }

    private HashSet<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> positionsSet = new HashSet<>();

        int i = 1;

        while ((checkPosition(board, myPosition.getRow(), myPosition.getColumn() + i)) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i)));
            if ((checkPosition(board, myPosition.getRow(), myPosition.getColumn() + i)) == 2) {
                break;
            }
            ++i;
        }

        i = -1;
        while ((checkPosition(board, myPosition.getRow(), myPosition.getColumn() + i)) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i)));
            if ((checkPosition(board, myPosition.getRow(), myPosition.getColumn() + i)) == 2) {
                break;
            }
            --i;
        }

        i = 1;
        while ((checkPosition(board,myPosition.getRow() + i, myPosition.getColumn())) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn())));
            if ((checkPosition(board, myPosition.getRow() + i, myPosition.getColumn())) == 2) {
                break;
            }
            ++i;
        }

        i = -1;
        while ((checkPosition(board,myPosition.getRow() + i, myPosition.getColumn())) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn())));
            if ((checkPosition(board, myPosition.getRow() + i, myPosition.getColumn())) == 2) {
                break;
            }
            --i;
        }

        return positionsSet;
    }

    private HashSet<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> positionsSet = new HashSet<>();
        if ((checkPosition(board, myPosition.getRow() - 2, myPosition.getColumn() + 1)) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1)));
        }

        if ((checkPosition(board, myPosition.getRow() - 1, myPosition.getColumn() + 2)) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2)));
        }

        if ((checkPosition(board, myPosition.getRow() + 1, myPosition.getColumn() + 2)) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2)));
        }

        if ((checkPosition(board, myPosition.getRow() + 2, myPosition.getColumn() + 1)) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1)));
        }

        if ((checkPosition(board, myPosition.getRow() + 2, myPosition.getColumn() - 1)) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1)));
        }

        if ((checkPosition(board, myPosition.getRow() + 1, myPosition.getColumn() - 2)) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2)));
        }

        if ((checkPosition(board, myPosition.getRow() - 1, myPosition.getColumn() - 2)) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2)));
        }

        if ((checkPosition(board, myPosition.getRow() - 2, myPosition.getColumn() - 1)) != 0) {
            positionsSet.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1)));
        }

        return positionsSet;
    }

    private HashSet<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> positionsSet = new HashSet<>();

        if (this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (myPosition.getRow() + 1 < 8) {
                ChessPosition position;
                position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                ChessPiece piece = board.getPiece(position);

                if (piece == null) {
                    positionsSet.add(new ChessMove(myPosition, position));

                    if (myPosition.getRow() == 2) {
                        position = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                        piece = board.getPiece(position);

                        if (piece == null) {
                            positionsSet.add(new ChessMove(myPosition, position));
                        }
                    }
                }
            } else {
                ChessPosition position;
                position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                ChessPiece piece = board.getPiece(position);

                if (piece == null) {
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.KNIGHT));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.QUEEN));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.BISHOP));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.ROOK));
                }
            }

            if (myPosition.getRow() + 1 < 8 && myPosition.getColumn() - 1 > 0) {
                ChessPosition position;
                position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() != this.getTeamColor()) {
                    positionsSet.add(new ChessMove(myPosition, position));
                }
            } else if (myPosition.getColumn() - 1 > 0){
                ChessPosition position;
                position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() != this.getTeamColor()) {
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.KNIGHT));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.QUEEN));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.BISHOP));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.ROOK));
                }
            }

            if (myPosition.getRow() + 1 < 8 && myPosition.getColumn() + 1 < 9) {
                ChessPosition position;
                position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() != this.getTeamColor()) {
                    positionsSet.add(new ChessMove(myPosition, position));                }
            } else if (myPosition.getColumn() + 1 < 9){
                ChessPosition position;
                position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() != this.getTeamColor()) {
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.KNIGHT));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.QUEEN));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.BISHOP));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.ROOK));
                }
            }
        } else {
            if (myPosition.getRow() - 1 > 1) {
                ChessPosition position;
                position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                ChessPiece piece = board.getPiece(position);

                if (piece == null) {
                    positionsSet.add(new ChessMove(myPosition, position));

                    if (myPosition.getRow() == 7) {
                        position = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                        piece = board.getPiece(position);

                        if (piece == null) {
                            positionsSet.add(new ChessMove(myPosition, position));
                        }
                    }
                }
            } else {
                ChessPosition position;
                position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                ChessPiece piece = board.getPiece(position);

                if (piece == null) {
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.KNIGHT));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.QUEEN));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.BISHOP));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.ROOK));
                }
            }

            if (myPosition.getRow() - 1 > 1 && myPosition.getColumn() - 1 > 0) {
                ChessPosition position;
                position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() != this.getTeamColor()) {
                    positionsSet.add(new ChessMove(myPosition, position));                }
            } else if (myPosition.getColumn() - 1 > 0){
                ChessPosition position;
                position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() != this.getTeamColor()) {
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.KNIGHT));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.QUEEN));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.BISHOP));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.ROOK));
                }
            }

            if (myPosition.getRow() - 1 > 1 && myPosition.getColumn() + 1 < 9) {
                ChessPosition position;
                position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() != this.getTeamColor()) {
                    positionsSet.add(new ChessMove(myPosition, position));
                }
            } else if (myPosition.getColumn() + 1 < 9) {
                ChessPosition position;
                position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() != this.getTeamColor()) {
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.KNIGHT));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.QUEEN));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.BISHOP));
                    positionsSet.add(new ChessMove(myPosition, position, PieceType.ROOK));
                }
            }
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
        } else if (this.getPieceType() == PieceType.QUEEN){
            positionsSet = queenMoves(board, myPosition);
        } else if (this.getPieceType() == PieceType.BISHOP){
            positionsSet = bishopMoves(board, myPosition);
        } else if (this.getPieceType() == PieceType.ROOK){
            positionsSet = rookMoves(board, myPosition);
        } else if (this.getPieceType() == PieceType.KNIGHT){
            positionsSet = knightMoves(board, myPosition);
        } else if (this.getPieceType() == PieceType.PAWN){
            positionsSet = pawnMoves(board, myPosition);
        }

        positionsSet.removeIf(Objects::isNull);
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
