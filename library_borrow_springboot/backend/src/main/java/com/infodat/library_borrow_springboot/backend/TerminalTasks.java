package com.infodat.library_borrow_springboot.backend;

import java.io.IOException;
import java.util.Scanner;

public class TerminalTasks {
    public static void clearTerminal(boolean waitInput) {
        // Wait for user input so that user can see the error
        if (waitInput) {
            System.out.println("");
            System.out.println("Press any key to continue...");

            try {
                new ProcessBuilder("cmd", "/c", "pause > nul").inheritIO().start().waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Clear terminal
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static int takeInput() {
        Scanner inputScanner = new Scanner(System.in);
        String input = inputScanner.nextLine();
        int num = -1;

        try {
            num = Integer.parseInt(input);
        } catch (Exception e) {
            System.out.println("Invalid Input! Please try again.");
            clearTerminal(true);
        }
        return num;
    }
}
