//package ui;
//
////import webSocketMessages.Notification;
//
//import static ui.EscapeSequences.*;
//import java.io.PrintStream;
//import java.nio.charset.StandardCharsets;
//import java.util.Scanner;
//
//
//
//public class Menu {
//    public Menu() {
//
//
//        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
//        out.print(EscapeSequences.ERASE_SCREEN);
//        displayMenu(out);
//        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
//        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
//
//    }
//
//    public static void main(String[] args){
//        new Menu();
//    }
//
//    private static void displayMenu(PrintStream out){
//        System.out.println("\uD83D\uDC36 Welcome to the pet store. Sign in to start.");
//        System.out.print(client.help());
//
//        Scanner scanner = new Scanner(System.in);
//        var result = "";
//        while (!result.equals("quit")) {
//            printPrompt();
//            String line = scanner.nextLine();
//
//            try {
//                result = client.eval(line);
//                System.out.print(BLUE + result);
//            } catch (Throwable e) {
//                var msg = e.toString();
//                System.out.print(msg);
//            }
//        }
//        System.out.println();
//    }
//
////    public void notify(Notification notification) {
////        System.out.println(RED + notification.message());
////        printPrompt();
////    }
//
//    private void printPrompt() {
//        System.out.print("\n" + RESET + ">>> " + GREEN);
//    }
//
//
//}
