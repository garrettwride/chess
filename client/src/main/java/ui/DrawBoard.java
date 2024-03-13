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
        new DrawBoard();
}

    private static void drawChessBoard(PrintStream out) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                drawSquare(out, row, col);
            }
            out.print(EscapeSequences.SET_BG_COLOR_BLACK);
            out.println();
            //drawHorizontalLine(out);
        }
    }

    private static void drawSquare(PrintStream out, int row, int col) {
        if ((row + col) % 2 == 0) {
            setWhite(out);
        } else {
            setBlack(out);
        }

        if (row == 0) {
            // Place white pieces in row 0
            setBlue(out);
            if (col == 0) {
                out.print(EscapeSequences.WHITE_ROOK);
            } else if (col == 1) {
                out.print(EscapeSequences.WHITE_KNIGHT);
            } else if (col == 2) {
                out.print(EscapeSequences.WHITE_BISHOP);
            } else if (col == 3) {
                out.print(EscapeSequences.WHITE_QUEEN);
            } else if (col == 4) {
                out.print(EscapeSequences.WHITE_KING);
            } else if (col == 5) {
                out.print(EscapeSequences.WHITE_BISHOP);
            } else if (col == 6) {
                out.print(EscapeSequences.WHITE_KNIGHT);
            } else if (col == 7) {
                out.print(EscapeSequences.WHITE_ROOK);
            }
        } else if (row == 1) {
            // Place white pawns in row 1
            setBlue(out);
            out.print(EscapeSequences.WHITE_PAWN);
        } else if (row == 6) {
            // Place black pawns in row 6
            setRed(out);
            out.print(EscapeSequences.BLACK_PAWN);
        } else if (row == 7) {
            // Place black pieces in row 7
            setRed(out);
            if (col == 0) {
                out.print(EscapeSequences.BLACK_ROOK);
            } else if (col == 1) {
                out.print(EscapeSequences.BLACK_KNIGHT);
            } else if (col == 2) {
                out.print(EscapeSequences.BLACK_BISHOP);
            } else if (col == 3) {
                out.print(EscapeSequences.BLACK_QUEEN);
            } else if (col == 4) {
                out.print(EscapeSequences.BLACK_KING);
            } else if (col == 5) {
                out.print(EscapeSequences.BLACK_BISHOP);
            } else if (col == 6) {
                out.print(EscapeSequences.BLACK_KNIGHT);
            } else if (col == 7) {
                out.print(EscapeSequences.BLACK_ROOK);
            }
        } else {
            out.print(EscapeSequences.EMPTY);
        }

        if (col < BOARD_SIZE - 1) {
            //drawVerticalLine(out);
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
    }

    private static void setBlack(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
    }

    private static void setBlue(PrintStream out) {
        out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
    }

    private static void setRed(PrintStream out) {
        out.print(EscapeSequences.SET_TEXT_COLOR_RED);
    }
}

