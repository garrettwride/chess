package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;


public class DrawBoard {

    private static final int BOARD_SIZE = 8;
    private static final int LINE_WIDTH_IN_CHARS = 1;

    public DrawBoard() {


        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        drawChessBoard(out);
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);

}

public static void main(String[] args){
        DrawBoard drawBoard = new DrawBoard();
}

    private static void drawChessBoard(PrintStream out) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                drawSquare(out, row, col);
            }
            out.println();
            drawHorizontalLine(out);
        }
    }

    private static void drawSquare(PrintStream out, int row, int col) {
        if ((row + col) % 2 == 0) {
            setWhite(out);
        } else {
            setBlack(out);
        }

        if (row == 0) {
            if (col == 0) {
                out.print(EscapeSequences.WHITE_ROOK);
            }
            if (col == 1) {
                out.print(EscapeSequences.WHITE_KNIGHT);
            }

        } else {
            out.print(EscapeSequences.EMPTY);
        }

        if (col < BOARD_SIZE - 1) {
            drawVerticalLine(out);
        }
    }

    private static void drawHorizontalLine(PrintStream out) {
        for (int i = 0; i < LINE_WIDTH_IN_CHARS; i++) {
            out.print(EscapeSequences.EMPTY.repeat(BOARD_SIZE + (BOARD_SIZE - 1) * LINE_WIDTH_IN_CHARS));
            out.println();
        }
    }

    private static void drawVerticalLine(PrintStream out) {
        out.print(EscapeSequences.EMPTY.repeat(LINE_WIDTH_IN_CHARS));
    }

    private static void setWhite(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }
}

