package com.infodat.library_borrow_springboot.backend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Scanner;

import static com.infodat.library_borrow_springboot.backend.TerminalTasks.clearTerminal;
import static com.infodat.library_borrow_springboot.backend.TerminalTasks.takeInput;
import static com.infodat.library_borrow_springboot.backend.InitDatabase.stmt;
import static com.infodat.library_borrow_springboot.backend.InitDatabase.conn;

public class User extends AuthHandler implements IUser {
    private String userType = "User";
    private boolean isAdmin = false;

    // Constructor to set variable values to reflect admin
    public User(LibraryImpl libObj) {
        super(libObj);
        this.setUserType(userType);
        this.setisAdmin(isAdmin);
    }

    @Override
    protected void dashboard() {
        clearTerminal(false);

        Scanner inputScanner = new Scanner(System.in);

        boolean run = true;
        int choice = 0;

        do {
            System.out.println(
                    "========================================= \n\t\tMain Menu \n========================================= ");

            System.out.print("[1] Borrow Book \n[2] Return Book \n[3] Logout \nEnter Your Choice: ");
            choice = takeInput();
            switch (choice) {
                case -1 -> {
                    break;
                }

                case 1 -> // Display All Books
                {
                    borrowBook();
                    clearTerminal(false);
                    break;
                }
                case 2 -> // Admin Main Menu
                {
                    returnBook();
                    clearTerminal(false);
                    break;
                }
                case 3 -> // Exit Program
                {
                    System.out.println("Exiting Program!");
                    run = false;
                }
                default -> // Default
                {
                    System.out.println("Invalid Choice! Please try again.");
                    clearTerminal(true);
                }

            }
        } while (run);
    }

    @Override
    public void borrowBook() {
        clearTerminal(false);

        Scanner inputScanner = new Scanner(System.in);

        boolean run = true;
        int choice = 0;

        do {
            System.out.println(
                    "========================================= \n\t\tBorrow Book \n========================================= ");

            System.out.print(
                    "[1] Display All Books \n[2] Search by Title \n[3] Search By ISBN \n[4] Exit \nEnter Your Choice: ");
            choice = takeInput();
            switch (choice) {
                case -1 -> {
                    break;
                }
                case 1 -> // Display All Books
                {
                    libObj.displayAllBooks();
                    clearTerminal(true);
                    break;
                }
                case 2 -> // Borrow By Title
                {
                    libObj.searchBook(getUsername(), "Title");
                    clearTerminal(true);
                    break;
                }
                case 3 -> // Borrow By ISBN
                {
                    libObj.searchBook(getUsername(), "ISBN");
                    clearTerminal(true);
                    break;
                }
                case 4 -> // Exit Program
                {
                    run = false;
                }
                default -> // Default
                {
                    System.out.println("Invalid Choice! Please try again.");
                    clearTerminal(true);
                }

            }
        } while (run);
    }

    @Override
    public void returnBook() {
        int j = 1;
        int choice;
        Scanner inputScanner = new Scanner(System.in);
        List<IBook> booksOwned = libObj.getBooksOwned(getUsername());

        System.out.println("Books Borrowed:");

        for (var iterable_element : booksOwned) {
            System.out.print("[" + j + "] ");
            iterable_element.displayInfo();
            j++;
        }

        if (j == 1) {
            System.out.println("You have no books borrowed!");
            clearTerminal(true);
            return;
        }

        do {
            System.out.print("Which Book Would You Like To Return: ");
            choice = takeInput() - 1;

            if (j < choice) {
                System.out.print("Incorrect choice!");
                System.out.println("");
            }
        } while (j < choice);

        String isbn = booksOwned.get(choice).getISBN();
        // Update DB
        String QUERY = "SELECT * FROM Books where ISBN = '" + isbn + "'";

        // Check if book records already exist
        try (ResultSet rs = stmt.executeQuery(QUERY)) {
            if (rs.next()) {
                String updateQuery = "UPDATE Books SET BorrowedBy = NULL WHERE ISBN = '" + isbn + "'";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.executeUpdate();
                    System.out.println("Book Returned Successfully!");
                    // Delete from IBooks list.
                    for (int i = 0; i < libObj.libBooks.size(); i++) {
                        if (libObj.libBooks.get(i).getISBN() == isbn) {
                            libObj.libBooks.remove(i);
                        }
                    }
                    clearTerminal(true);
                    return;
                } catch (Exception e) {
                    System.out.println("Failed");
                }

            }
        } catch (Exception e) {
            System.out.println("Failed");
        }

        clearTerminal(true);
    }
}