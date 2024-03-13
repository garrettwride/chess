package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import ui.EscapeSequences;

public class DrawBoard {

    private static final int BOARD_SIZE = 8; // Chess board is 8x8
    private static final int SQUARE_SIZE_IN_CHARS = 3; // Adjust based on your preference
    private static final int LINE_WIDTH_IN_CHARS = 1; // Width of vertical lines between squares

    public void DrawBoard() {


        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        drawChessBoard(out);
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);

}

public static void main(String[] args){
        new DrawBoard();
}

    private static void drawChessBoard(PrintStream out) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; squareRow++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    drawSquare(out, row, col, squareRow);
                }
                out.println();
            }
            drawHorizontalLine(out);
        }
    }

    private static void drawSquare(PrintStream out, int row, int col, int squareRow) {
        if ((row + col) % 2 == 0) {
            setWhite(out);
        } else {
            setBlack(out);
        }

        if (squareRow == SQUARE_SIZE_IN_CHARS / 2) {
            // Draw chess piece if needed, else draw an empty square
            // Example: out.print(EscapeSequences.WHITE_KING);
            out.print(EscapeSequences.EMPTY);
        } else {
            out.print(EscapeSequences.EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
        }

        if (col < BOARD_SIZE - 1) {
            drawVerticalLine(out);
        }
    }

    private static void drawHorizontalLine(PrintStream out) {
        for (int i = 0; i < LINE_WIDTH_IN_CHARS; i++) {
            out.print(EscapeSequences.EMPTY.repeat(BOARD_SIZE * SQUARE_SIZE_IN_CHARS + (BOARD_SIZE - 1) * LINE_WIDTH_IN_CHARS));
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

