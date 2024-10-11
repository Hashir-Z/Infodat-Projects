package com.infodat.library_borrow_springboot.backend;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

import static com.infodat.library_borrow_springboot.backend.TerminalTasks.clearTerminal;
import static com.infodat.library_borrow_springboot.backend.TerminalTasks.takeInput;
import static com.infodat.library_borrow_springboot.backend.InitDatabase.stmt;
import static com.infodat.library_borrow_springboot.backend.InitDatabase.conn;

public class LibraryImpl implements ILibrary {
    List<IBook> libBooks = new ArrayList<>();

    // Constructor to add 100 books right from the get-go
    public LibraryImpl() {
        // Add 100 books to the DB if they don't exist already
        for (int i = 1; i <= 100; i++) {
            i = bookGenerator(i);
        }

        // Read the DB into memory
        bookImpl book;

        String QUERY = "SELECT * FROM BOOKS";

        try (ResultSet rs = stmt.executeQuery(QUERY);) {
            while (rs.next()) {
                // Retrieve by column name
                System.out.println("Connection Successful!");
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String isbn = rs.getString("ISBN");
                String publisher = rs.getString("Publisher");
                String genre = rs.getString("Genre");
                Date publicationDate = rs.getDate("PublicationDate");
                String borrowedBy = rs.getString("BorrowedBy");
                book = new bookImpl(title, author, isbn, publisher, genre, publicationDate, borrowedBy);
                this.libBooks.add(book);
            }
        } catch (Exception e) {
            System.out.println("Failed");
        }
    }

    @Override
    public void addBook(IBook book) {
        String QUERY = "INSERT INTO Books (Title, Author, ISBN, Publisher, Genre, PublicationDate, BorrowedBy) VALUES (?, ?, ?, ?, ?, ?, NULL)";

        try (PreparedStatement pstmt = conn.prepareStatement(QUERY)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getISBN());
            pstmt.setString(4, book.getPublisher());
            pstmt.setString(5, book.getGenre());

            // Check if the publication date is valid
            java.util.Date pubDate = book.getPublicationDate();
            if (pubDate == null) {
                pubDate = new java.util.Date(); // Set to current date if null
            }
            pstmt.setDate(6, new java.sql.Date(pubDate.getTime()));

            pstmt.executeUpdate();
            this.libBooks.add(book);
            System.out.println("Book added successfully!");
        } catch (SQLException e) {
            System.out.println("Could not add book: " + e.getMessage());
        }
        clearTerminal(true);
    }

    @Override
    public void removeBook(String isbn) {
        // Delete from DB
        String QUERY = "DELETE FROM BOOKS WHERE ISBN = " + isbn;

        try {
            stmt.executeUpdate(QUERY);
        } catch (Exception e) {
            System.out.println("No book with this ISBN found!");
            clearTerminal(true);
            return;
        }

        // Delete from IBooks list.
        for (int i = 0; i < libBooks.size(); i++) {
            if (libBooks.get(i).getISBN() == isbn) {
                libBooks.remove(i);
            }
        }
        System.out.println("Book Deleted From Library System!");
        clearTerminal(true);
    }

    @Override
    public void searchBook(String username, String field) {
        clearTerminal(false);
        String searchTerm;
        String searchResult;
        int choice;
        boolean isTitleSearch = false;
        boolean bookFound = false;
        Scanner inputScanner = new Scanner(System.in);

        System.out.print("Enter Book " + field + ": ");
        searchTerm = inputScanner.nextLine();

        if (field.contains("Title")) {
            isTitleSearch = true;
        }

        for (int i = 0; i < libBooks.size(); i++) {
            clearTerminal(false);
            if (isTitleSearch) {
                searchResult = libBooks.get(i).getTitle();
            } else {
                searchResult = libBooks.get(i).getISBN();
            }

            if (searchResult.contains(searchTerm)) {
                bookFound = true;
                libBooks.get(i).displayInfo();

                // Check if book has been borrowed by user
                if (libBooks.get(i).getBorrowedBy() == username) {
                    System.out.print("Book has Already Been Borrowed by You!");
                    clearTerminal(true);
                    searchBook(username, field);
                    return;
                }
                // Check if book has been borrowed by someone
                else if (libBooks.get(i).getBorrowedBy() != null) {
                    System.out.print("Book has Already Been Borrowed by Someone Else!");
                    clearTerminal(true);
                    searchBook(username, field);
                    return;
                }
                System.out.print("Would you like to borrow this book? \n [1] Yes \n [2] No \nEnter your choice: ");
                choice = takeInput();

                switch (choice) {
                    case 1: {
                        // Update DB
                        String QUERY = "SELECT * FROM Books where " + field + "= '" + searchResult + "'";

                        // Check if book records already exist
                        try (ResultSet rs = stmt.executeQuery(QUERY)) {
                            if (rs.next()) {
                                // Book exists, update the tUsername field
                                String updateQuery = "UPDATE Books SET BorrowedBy = ? WHERE " + field + "= ?";
                                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                    updateStmt.setString(1, username);
                                    updateStmt.setString(2, searchTerm);
                                    updateStmt.executeUpdate();
                                    System.out.println("Book Borrowed Successfully!");
                                    libBooks.get(i).setBorrowedBy(username);
                                    return;
                                } catch (Exception e) {
                                    System.out.println("Failed");
                                }

                            }
                        } catch (Exception e) {
                            System.out.println("Failed");
                        }
                    }
                    case 2: {
                        continue;
                    }
                    default: {
                    }
                }
            }
        }
        if (bookFound) {
            System.out.println("No More Books With This " + field + "!");

        } else {
            System.out.println("No Books Found!");
        }
        clearTerminal(true);
        return;
    }

    @Override
    public void displayAllBooks() {
        for (IBook IBook : libBooks) {
            IBook.displayInfo();
        }
    }

    public List<IBook> returnAllBooks() {
        return libBooks;
    }

    public List<IBook> getBooksOwned(String username) {
        List<IBook> books = new ArrayList<>();

        for (int i = 0; i < libBooks.size(); i++) {
            if (libBooks.get(i).getBorrowedBy() != null && libBooks.get(i).getBorrowedBy().contentEquals(username)) {
                books.add(libBooks.get(i));
            }
        }
        return books;
    }

    // Function to randomly generate 100 books in MySQL DB
    private int bookGenerator(int num) {
        String QUERY = "SELECT COUNT(*) AS TotCount FROM Books";

        // Check if book records already exist
        try (ResultSet rs = stmt.executeQuery(QUERY)) {
            if (rs.next()) {
                int value = rs.getInt("TotCount"); // Use the alias name "TotCount"
                if (value == 100) {
                    return 100;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed");
        }

        // If they don't, then add books to the DB
        QUERY = "INSERT INTO Books (Title, Author, ISBN, Publisher, Genre, PublicationDate, BorrowedBy) VALUES ('Name"
                + num + "', 'Author" + num + "', '" + String.valueOf(num) + "', 'Publisher" + num + "', 'Genre" + num
                + "', '" + new java.sql.Date(System.currentTimeMillis()) + "', NULL)";

        try {
            stmt.executeUpdate(QUERY);
        } catch (Exception e) {
            System.out.println("Failed");
        }
        return num;
    }
}