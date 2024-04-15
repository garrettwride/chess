package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;

public class DrawBoard {

        private static final int BOARD_SIZE = 10;
        private ChessBoard chessBoard;

        public DrawBoard(ChessBoard chessBoard) {
            this.chessBoard = chessBoard;
        }

        public static void main(String[] args) {
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            DrawBoard draw = new DrawBoard(board);
            ChessGame game = new ChessGame();
            Collection<ChessMove> legalMoves = game.validMoves(new ChessPosition(2,2));
            Collection<ChessPosition> endPositions = new HashSet<>();

            for (ChessMove move : legalMoves) {
                ChessPosition endPosition = move.getEndPosition();
                endPositions.add(endPosition);
            }
            draw.draw(board, endPositions);
        }

        public void draw(ChessBoard chessBoard, Collection<ChessPosition> legalMoves){
            this.chessBoard = chessBoard;
            var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
            out.print(EscapeSequences.ERASE_SCREEN);
            out.print(EscapeSequences.SET_BG_COLOR_BLACK);
            out.println();
            if (legalMoves != null) {
                setGreenBackgroundForLegalMoves(out, legalMoves);
            } else {
                drawChessBoards(out);
            }
            out.print(EscapeSequences.SET_BG_COLOR_BLACK);
            out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        }

        private void setGreenBackgroundForLegalMoves(PrintStream out, Collection<ChessPosition> legalMoves) {
            // Print from white perspective
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    ChessPosition currentPosition = new ChessPosition(row , col);
                    boolean isLegalMove = legalMoves.contains(currentPosition);
                    drawSquare(out, row, col, isLegalMove);
                }
                    out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                    out.println();

            }

            out.println();

            // Print from black perspective
            for (int row = BOARD_SIZE - 1; row >= 0; row--) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    ChessPosition currentPosition = new ChessPosition(row , col);
                    boolean isLegalMove = legalMoves.contains(currentPosition);
                    drawSquare(out, row, col, isLegalMove);
                }
                out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                out.println();
            }
        }

        private static void setGreen(PrintStream out) {
            out.print(EscapeSequences.SET_BG_COLOR_GREEN);
        }

    private void drawChessBoards(PrintStream out) {
        // Print from white perspective
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                drawSquare(out, row, col, false);
            }
            out.print(EscapeSequences.SET_BG_COLOR_BLACK);
            out.println();
        }

        out.println();

        // Print from black perspective
        for (int row = BOARD_SIZE - 1; row >= 0; row--) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                drawSquare(out, row, col, false);
            }
            out.print(EscapeSequences.SET_BG_COLOR_BLACK);
            out.println();
        }
    }

        private void drawSquare(PrintStream out, int row, int col, boolean isLegalMove) {
            if (!isLegalMove) {
                if ((row + col) % 2 == 0) {
                    setBlack(out);
                } else {
                    setWhite(out);
                }
            } else setGreen(out);

            if (row == 0 || row == BOARD_SIZE - 1) {
                makeColumnBorder(out, col);
            } else if (col == 0 || col == BOARD_SIZE - 1) {
                makeRowBorder(out, row);
            } else {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = chessBoard.getPiece(position);
                if (piece != null) {
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        setBlueText(out);
                    } else {
                        setRedText(out);
                    }
                    out.print(" " + piece.getSymbol() + " ");
                    resetTextColor(out);
                } else {
                    out.print(EscapeSequences.EMPTY);
                }
            }
        }

        private static void makeRowBorder(PrintStream out, int col) {
            setGrey(out);
            if (col == 0 || col == BOARD_SIZE - 1) {
                out.print(EscapeSequences.EMPTY);
            } else {
                setBlackText(out);
                out.print(" " + (col) + " ");
            }
        }

        private static void makeColumnBorder(PrintStream out, int row) {
            setGrey(out);
            if (row == 0 || row == BOARD_SIZE - 1) {
                out.print(EscapeSequences.EMPTY);
            } else {
                setBlackText(out);
                char label = (char) ('a' + (row - 1));
                out.print(" " + label + " ");
            }
        }


        private static void setWhite(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
    }

    private static void setGrey(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
    }

    private static void setBlackText(PrintStream out) {
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }
        private static void setBlueText(PrintStream out) {
            out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        }

        private static void setRedText(PrintStream out) {
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        }

        private static void resetTextColor(PrintStream out) {
            out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        }
}