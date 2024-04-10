package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;


public class DrawBoard {

    private static final int BOARD_SIZE = 10;
    private static final int LINE_WIDTH_IN_CHARS = 1;
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
        //print from white perspective
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                drawSquare(out, row, col);
            }
            out.print(EscapeSequences.SET_BG_COLOR_BLACK);
            out.println();

        }

        out.println();

        //print from black perspective
        for (int row = BOARD_SIZE - 1; row >= 0; row--) { // Iterate in reverse order
            for (int col = 0; col < BOARD_SIZE; col++) {
                drawSquare(out, row, col); // Pass row in reverse order
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
            out.print(EscapeSequences.EMPTY);
        }
    }

    private static void makeColumnBorder(PrintStream out, int col) {
        setGrey(out);
        setBlackText(out);
        if (col == 1) {
            out.print(" h ");
        } else if (col == 2) {
            out.print(" g ");
        } else if (col == 3) {
            out.print(" f ");
        } else if (col == 4) {
            out.print(" e ");
        } else if (col == 5) {
            out.print(" d ");
        } else if (col == 6) {
            out.print(" c ");
        } else if (col == 7) {
            out.print(" b ");
        } else if (col == 8) {
            out.print(" a ");
        }else {
            out.print(EscapeSequences.EMPTY);
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

    private static void setBlueText(PrintStream out) {
        out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
    }

    private static void setRedText(PrintStream out) {
        out.print(EscapeSequences.SET_TEXT_COLOR_RED);
    }
    private static void setBlackText(PrintStream out) {
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }
}

