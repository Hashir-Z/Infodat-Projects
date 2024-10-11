package com.infodat.library_borrow_springboot.backend;

import static com.infodat.library_borrow_springboot.backend.InitDatabase.*;
import static com.infodat.library_borrow_springboot.backend.TerminalTasks.clearTerminal;
import static com.infodat.library_borrow_springboot.backend.TerminalTasks.takeInput;

import java.sql.ResultSet;
import java.util.Scanner;

public class MainMenu {
    public void displayMenu() {
        LibraryImpl libObj = new LibraryImpl();

        testConnection();
        clearTerminal(false);

        Scanner inputScanner = new Scanner(System.in);

        boolean run = true;
        int choice = 0;
        User userObj = new User(libObj);
        Admin adminObj = new Admin(libObj);

        do {
            System.out.println(
                    "========================================= \n\t\tMain Menu \n========================================= ");

            System.out.print("[1] User \n[2] Admin \n[3] Exit \nEnter Your Choice: ");
            choice = takeInput();
            switch (choice) {
                case -1 -> {
                    break;
                }
                case 1 -> // User Main Menu
                {
                    userObj.mainMenu();
                    clearTerminal(false);
                    break;
                }
                case 2 -> // Admin Main Menu
                {
                    adminObj.mainMenu();
                    clearTerminal(false);
                    break;
                }
                case 3 -> // Exit Program
                {
                    System.out.println("Exiting Program!");
                    run = false;
                }
                default -> {
                    System.out.println("Invalid Choice! Please try again.");
                    clearTerminal(true);
                }

            }
        } while (run);
        inputScanner.close();
    }
}
