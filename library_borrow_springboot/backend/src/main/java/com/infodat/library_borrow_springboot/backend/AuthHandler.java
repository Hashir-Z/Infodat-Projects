package com.infodat.library_borrow_springboot.backend;

import java.sql.ResultSet;
import java.util.Scanner;

import static com.infodat.library_borrow_springboot.backend.TerminalTasks.clearTerminal;
import static com.infodat.library_borrow_springboot.backend.TerminalTasks.takeInput;
import static com.infodat.library_borrow_springboot.backend.InitDatabase.stmt;

public abstract class AuthHandler {
    private String userType;
    private String username;
    private boolean isAdmin;
    LibraryImpl libObj;

    public AuthHandler(LibraryImpl libObj) {
        this.libObj = libObj;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setisAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void mainMenu() {
        clearTerminal(false);

        boolean run = true;
        int choice;

        do {
            Scanner inputScanner = new Scanner(System.in);
            System.out.println("============================================== \n\t\t" + userType
                    + " Main Menu \n==============================================");

            System.out.print("[1] Login \n[2] Signup \n[3] Back \nEnter Your Choice: ");
            choice = takeInput();
            switch (choice) {
                case -1 -> {
                    break;
                }
                case 1 -> // Login Menu
                {
                    loginMenu();
                    run = false;
                    break;
                }
                case 2 -> // Sign up Menu
                {
                    signupMenu();
                    clearTerminal(false);
                    break;
                }
                case 3 -> // Go back
                {
                    run = false;
                    break;
                }
                default -> {
                    System.out.println("Invalid Choice!");
                    clearTerminal(true);
                }
            }
        } while (run);
    }

    private void loginMenu() {
        clearTerminal(false);
        Scanner inputScanner = new Scanner(System.in);

        boolean run = true;
        String password;

        boolean success;
        do {
            System.out.println(
                    "========================================= \n\t\tLogin \n=========================================");

            System.out.print("Enter Username: ");
            username = inputScanner.nextLine();
            success = validateInput(username, "tUsername");
            if (success) {
                System.out.print("Enter Password: ");
                password = inputScanner.nextLine();
                success = validateInput(password, "tPassword");
                if (success) {
                    dashboard();
                    run = false;
                } else {
                    System.out.print("Incorrect Password!");
                    clearTerminal(true);
                }
            } else {
                System.out.print("No Such User Exists!");
                clearTerminal(true);
            }
        } while (run);
    }

    private void signupMenu() {
        clearTerminal(false);
        System.out.println(
                "========================================= \n\t\tSign Up \n=========================================");

        // Variables
        Scanner inputScanner = new Scanner(System.in);
        String password;

        // Getting Username + Password
        System.out.print("Enter Username: ");
        username = inputScanner.nextLine();
        if (validateInput(username, "tUsername")) {
            System.out.print("Username Already Exists!");
            clearTerminal(true);
            signupMenu();
            return;
        }

        System.out.print("Enter Password: ");
        password = inputScanner.nextLine();

        // Add the user
        addUsers(username, password);
        System.out.println("User Added! You may now login...");

        // Clear terminal + close scanner
        clearTerminal(true);
    }

    private boolean validateInput(String value, String field) {
        String QUERY = "SELECT * FROM users where (" + field + ", bisAdmin) IN (('" + value + "'," + isAdmin + "))";

        try (ResultSet rs = stmt.executeQuery(QUERY);) {
            while (rs.next()) {
                return true; // Success
            }
        } catch (Exception e) {
            return false; // Failed
        }
        return false;
    }

    private void addUsers(String username, String password) {
        String QUERY = "INSERT INTO users (tUsername, tPassword, bisAdmin) VALUES ('" + username + "', '" + password
                + "', " + isAdmin + ")";

        try {
            stmt.executeUpdate(QUERY);
        } catch (Exception e) {
            System.out.println("Failed");
        }
    }

    // Abstract method to be implemented by child classes
    protected abstract void dashboard();
}