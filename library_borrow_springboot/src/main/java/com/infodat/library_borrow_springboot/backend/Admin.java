package com.infodat.library_borrow_springboot.backend;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import static com.infodat.library_borrow_springboot.backend.TerminalTasks.clearTerminal;
import static com.infodat.library_borrow_springboot.backend.TerminalTasks.takeInput;

public class Admin extends AuthHandler implements IAdmins {
    private String userType = "Admin";
    private boolean isAdmin = true;

    // Constructor to set variable values to reflect admin
    public Admin(LibraryImpl libObj) {
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

            System.out.print("[1] Add Book \n[2] Remove Book \n[3] Logout \nEnter Your Choice: ");
            choice = takeInput();
            switch (choice) {
                case -1 -> {
                    break;
                }
                case 1 -> // Display All Books
                {
                    addBook();
                    clearTerminal(false);
                    break;
                }
                case 2 -> // Admin Main Menu
                {
                    removeBook();
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

    public void addBook() {
        clearTerminal(false);

        String title;
        String author;
        String isbn;
        String publisher;
        Date publicationDate;
        String genre;
        Scanner inputScanner = new Scanner(System.in);

        System.out.print("Enter Title: ");
        title = inputScanner.nextLine();

        System.out.print("Enter Author: ");
        author = inputScanner.nextLine();

        System.out.print("Enter ISBN: ");
        isbn = inputScanner.nextLine();

        System.out.print("Enter Publisher: ");
        publisher = inputScanner.nextLine();

        System.out.print("Enter Genre: ");
        genre = inputScanner.nextLine();

        SimpleDateFormat format = new SimpleDateFormat("E, MM dd yy");
        System.out.print("Enter PublicationDate (MM DD YY): ");
        try {
            publicationDate = format.parse(inputScanner.nextLine());

        } catch (Exception e) {
            System.out.print("Invalid date input! Setting publishing date to today...");
            publicationDate = new java.sql.Date(System.currentTimeMillis());
        }
        bookImpl newbook = new bookImpl(title, author, isbn, publisher, genre, publicationDate, genre);
        libObj.addBook(newbook);
    }

    public void removeBook() {
        clearTerminal(false);

        String isbn;
        Scanner inputScanner = new Scanner(System.in);

        System.out.print("Enter ISBN of book you'd like to remove: ");
        isbn = inputScanner.nextLine();

        libObj.removeBook(isbn);
    }

}