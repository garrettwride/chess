package ui;


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
    public void notify(ServerMessage serverMessage) {
        ServerMessage.ServerMessageType type = serverMessage.getServerMessageType();

        switch (type) {
            case ERROR:
                ErrorMessage errorMessage = (ErrorMessage) serverMessage;
                String errorText = errorMessage.getErrorMessage();
                System.out.println("Error: " + errorText);
                break;
            case NOTIFICATION:
                NotificationMessage notificationMessage = (NotificationMessage) serverMessage;
                String notificationText = notificationMessage.getMessage();
                System.out.println(notificationText);
                break;
            case LOAD_GAME:
                LoadGameMessage loadGameMessage = (LoadGameMessage) serverMessage;
                client.loadGame(loadGameMessage.getGame());
                break;
            default:
                System.out.println("Unknown message type: " + type);
                break;
        }

        printPrompt();
    }

}
