package ui;


import com.google.gson.Gson;
import webSocketMessages.serverMessages.*;
import websocket.NotificationHandler;

import static ui.EscapeSequences.*;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;



public class Menu implements NotificationHandler {

    private MenuClient client;
    public Menu(String serverUrl) {

        client = new MenuClient(serverUrl, this);
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        displayMenu(out);
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);

    }

    public static void main(String[] args){
        new Menu("qwerty");
    }

    private void displayMenu(PrintStream out){
        System.out.println("\uD83D\uDC36 Welcome to chess. Sign in to start.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {

            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);

                System.out.print(BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
        System.exit(0);
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }


    @Override
    public void notify(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        ServerMessage.ServerMessageType type = serverMessage.getServerMessageType();

        switch (type) {
            case ERROR:
                ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                String errorText = errorMessage.getErrorMessage();
                System.out.println("Error: " + errorText);
                break;
            case NOTIFICATION:
                NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);;
                String notificationText = notificationMessage.getMessage();
                System.out.println(notificationText);
                if (notificationText.contains("Game over.")){
                    client.gameState = GameState.GAME_OVER;
                    System.out.print(client.help());
                }
                break;
            case LOAD_GAME:
                LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                if (loadGameMessage.getGame() == null) {
                    System.out.println("Game over.");
                    client.gameState = GameState.GAME_OVER;
                    System.out.print(client.help());
                } else {
                    client.loadGame(loadGameMessage.getGame());
                }
                break;
            default:
                System.out.println("Unknown message type: " + type);
                break;
        }

        printPrompt();
    }
}
