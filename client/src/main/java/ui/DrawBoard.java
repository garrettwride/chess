package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class DrawBoard {

    private static final int BOARD_SIZE = 10;
    private ChessBoard chessBoard;

    public DrawBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        drawChessBoards(out);
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        new DrawBoard(board);
    }

    private void drawChessBoards(PrintStream out) {
        // Print from white perspective
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                drawSquare(out, row, col);
            }
            out.print(EscapeSequences.SET_BG_COLOR_BLACK);
            out.println();
        }

        out.println();

        // Print from black perspective
        for (int row = BOARD_SIZE - 1; row >= 0; row--) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                drawSquare(out, row, col);
            }
            out.print(EscapeSequences.SET_BG_COLOR_BLACK);
            out.println();
        }
    }

    private void drawSquare(PrintStream out, int row, int col) {
        if ((row + col) % 2 == 0) {
            setWhite(out);
        } else {
            setBlack(out);
        }

        ChessPosition position = new ChessPosition(row + 1, col + 1);
        ChessPiece piece = chessBoard.getPiece(position);

        if (piece != null) {
            out.print(piece.getSymbol());
        } else {
            if (row == 0 || row == BOARD_SIZE - 1) {
                makeColumnBorder(out, col);
            } else if (col == 0 || col == BOARD_SIZE - 1) {
                makeRowBorder(out, row);
            } else {
                out.print(EscapeSequences.EMPTY);
            }
        }
    }

    private static void makeColumnBorder(PrintStream out, int col) {
        setGrey(out);
        setBlackText(out);
        out.print(" " + (col + 1) + " ");
    }

    private static void makeRowBorder(PrintStream out, int row) {
        setGrey(out);
        setBlackText(out);
        char label = (char) ('a' + (BOARD_SIZE - 1 - row));
        out.print(" " + label + " ");
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
}


