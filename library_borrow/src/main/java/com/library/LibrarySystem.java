package com.library;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

// import com.library.LibrarySystem.User;

public class LibrarySystem
{
    // Database connection variables
    public static Connection conn = null;
    public static Statement stmt = null;
    static final String DB_URL = "jdbc:mysql://localhost:3306/lms?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
    static final String USER = "sqluser";
    static final String PASS = "password";

    // Constructor for the library system to automatically connect to the DB
    public LibrarySystem()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e)
        {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }

        try
        {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public interface IBook
    {
        String getTitle();

        String getAuthor();

        String getISBN();

        String getPublisher();

        String getGenre();

        Date getPublicationDate();

        String getBorrowedBy();

        void setBorrowedBy(String username);

        void displayInfo();
    }

    public class bookImpl implements IBook
    {
        private String title;
        private String author;
        private String isbn;
        private String publisher;
        private String genre;
        private Date publicationDate;
        private String borrowedBy;

        // Constructor to set values of books
        public bookImpl(String title, String author, String isbn, String publisher, String genre, Date publicationDate,
                String borrowedBy)
        {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.publisher = publisher;
            this.publicationDate = publicationDate;
            this.genre = genre;
            this.borrowedBy = borrowedBy;
        }

        // Getters
        @Override
        public String getTitle()
        {
            return title;
        }

        @Override
        public String getAuthor()
        {
            return author;
        }

        @Override
        public String getISBN()
        {
            return isbn;
        }

        @Override
        public String getPublisher()
        {
            return publisher;
        }

        @Override
        public String getGenre()
        {
            return genre;
        }

        @Override
        public Date getPublicationDate()
        {
            return publicationDate;
        }

        @Override
        public String getBorrowedBy()
        {
            return borrowedBy;
        }

        @Override
        public void setBorrowedBy(String username)
        {
            this.borrowedBy = username;
        }

        @Override
        public void displayInfo()
        {
            System.out.println("Name: " + title);
            System.out.println("Author: " + author);
            System.out.println("ISBN: " + isbn);
            System.out.println("Publisher: " + publisher);
            System.out.println("Genre: " + genre);
            System.out.println("Publication Date: " + publicationDate);
            System.out.println();
        }
    }

    public interface ILibrary
    {
        void addBook(IBook book);

        void removeBook(String isbn);

        void searchBook(String username, String field);

        void displayAllBooks();
    }

    public class LibraryImp1 implements ILibrary
    {
        List<IBook> libBooks = new ArrayList<>();

        // Constructor to add 100 books right from the get-go
        public LibraryImp1()
        {
            // Add 100 books to the DB if they don't exist already
            for (int i = 1; i <= 100; i++)
            {
                i = bookGenerator(i);
            }

            // Read the DB into memory
            bookImpl book;

            String QUERY = "SELECT * FROM BOOKS";

            try (ResultSet rs = stmt.executeQuery(QUERY);)
            {
                while (rs.next())
                {
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
            } catch (Exception e)
            {
                System.out.println("Failed");
            }
        }

        @Override
        public void addBook(IBook book)
        {
            String QUERY = "INSERT INTO Books (Title, Author, ISBN, Publisher, Genre, PublicationDate, BorrowedBy) VALUES (?, ?, ?, ?, ?, ?, NULL)";

            try (PreparedStatement pstmt = conn.prepareStatement(QUERY))
            {
                pstmt.setString(1, book.getTitle());
                pstmt.setString(2, book.getAuthor());
                pstmt.setString(3, book.getISBN());
                pstmt.setString(4, book.getPublisher());
                pstmt.setString(5, book.getGenre());

                // Check if the publication date is valid
                java.util.Date pubDate = book.getPublicationDate();
                if (pubDate == null)
                {
                    pubDate = new java.util.Date(); // Set to current date if null
                }
                pstmt.setDate(6, new java.sql.Date(pubDate.getTime()));

                pstmt.executeUpdate();
                this.libBooks.add(book);
                System.out.println("Book added successfully!");
            } catch (SQLException e)
            {
                System.out.println("Could not add book: " + e.getMessage());
            }
            clearTerminal(true);
        }

        @Override
        public void removeBook(String isbn)
        {
            // Delete from DB
            String QUERY = "DELETE FROM BOOKS WHERE ISBN = " + isbn;

            try
            {
                stmt.executeUpdate(QUERY);
            } catch (Exception e)
            {
                System.out.println("No book with this ISBN found!");
                clearTerminal(true);
                return;
            }

            // Delete from IBooks list.
            for (int i = 0; i < libBooks.size(); i++)
            {
                if (libBooks.get(i).getISBN() == isbn)
                {
                    libBooks.remove(i);
                }
            }
            System.out.println("Book Deleted From Library System!");
            clearTerminal(true);
        }

        @Override
        public void searchBook(String username, String field)
        {
            clearTerminal(false);
            String searchTerm;
            String searchResult;
            int choice;
            boolean isTitleSearch = false;
            boolean bookFound = false;
            Scanner inputScanner = new Scanner(System.in);

            System.out.print("Enter Book " + field + ": ");
            searchTerm = inputScanner.nextLine();

            if (field.contains("Title"))
            {
                isTitleSearch = true;
            }

            for (int i = 0; i < libBooks.size(); i++)
            {
                clearTerminal(false);
                if (isTitleSearch)
                {
                    searchResult = libBooks.get(i).getTitle();
                } else
                {
                    searchResult = libBooks.get(i).getISBN();
                }

                if (searchResult.contains(searchTerm))
                {
                    bookFound = true;
                    libBooks.get(i).displayInfo();

                    // Check if book has been borrowed by user
                    if (libBooks.get(i).getBorrowedBy() == username)
                    {
                        System.out.print("Book has Already Been Borrowed by You!");
                        clearTerminal(true);
                        searchBook(username, field);
                        return;
                    }
                    // Check if book has been borrowed by someone
                    else if (libBooks.get(i).getBorrowedBy() != null)
                    {
                        System.out.print("Book has Already Been Borrowed by Someone Else!");
                        clearTerminal(true);
                        searchBook(username, field);
                        return;
                    }
                    System.out.print("Would you like to borrow this book? \n [1] Yes \n [2] No \nEnter your choice: ");
                    choice = takeInput();

                    switch (choice)
                    {
                    case 1:
                    {
                        // Update DB
                        String QUERY = "SELECT * FROM Books where " + field + "= '" + searchResult + "'";

                        // Check if book records already exist
                        try (ResultSet rs = stmt.executeQuery(QUERY))
                        {
                            if (rs.next())
                            {
                                // Book exists, update the tUsername field
                                String updateQuery = "UPDATE Books SET BorrowedBy = ? WHERE " + field + "= ?";
                                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery))
                                {
                                    updateStmt.setString(1, username);
                                    updateStmt.setString(2, searchTerm);
                                    updateStmt.executeUpdate();
                                    System.out.println("Book Borrowed Successfully!");
                                    libBooks.get(i).setBorrowedBy(username);
                                    return;
                                } catch (Exception e)
                                {
                                    System.out.println("Failed");
                                }

                            }
                        } catch (Exception e)
                        {
                            System.out.println("Failed");
                        }
                    }
                    case 2:
                    {
                        continue;
                    }
                    default:
                    {
                    }
                    }
                }
            }
            if (bookFound)
            {
                System.out.println("No More Books With This " + field + "!");

            } else
            {
                System.out.println("No Books Found!");
            }
            clearTerminal(true);
            return;
        }

        @Override
        public void displayAllBooks()
        {
            for (IBook IBook : libBooks)
            {
                IBook.displayInfo();
            }
        }

        public List<IBook> getBooksOwned(String username)
        {
            List<IBook> books = new ArrayList<>();

            for (int i = 0; i < libBooks.size(); i++)
            {
                if (libBooks.get(i).getBorrowedBy() != null && libBooks.get(i).getBorrowedBy().contentEquals(username))
                {
                    books.add(libBooks.get(i));
                }
            }
            return books;
        }
    }

    public interface Users
    {
        void borrowBook();

        void returnBook();
    }

    public interface Admins
    {
        void addBook();

        void removeBook();
    }

    public abstract class AuthHandler
    {
        private String userType;
        private String username;
        private boolean isAdmin;
        LibraryImp1 libObj;

        public AuthHandler(LibraryImp1 libObj)
        {
            this.libObj = libObj;
        }

        public void setUserType(String userType)
        {
            this.userType = userType;
        }

        public void setisAdmin(boolean isAdmin)
        {
            this.isAdmin = isAdmin;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }

        public String getUsername()
        {
            return this.username;
        }

        public void mainMenu()
        {
            clearTerminal(false);

            boolean run = true;
            int choice;

            do
            {
                Scanner inputScanner = new Scanner(System.in);
                System.out.println("============================================== \n\t\t" + userType
                        + " Main Menu \n==============================================");

                System.out.print("[1] Login \n[2] Signup \n[3] Back \nEnter Your Choice: ");
                choice = takeInput();
                switch (choice)
                {
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

        private void loginMenu()
        {
            clearTerminal(false);
            Scanner inputScanner = new Scanner(System.in);

            boolean run = true;
            String password;

            boolean success;
            do
            {
                System.out.println(
                        "========================================= \n\t\tLogin \n=========================================");

                System.out.print("Enter Username: ");
                username = inputScanner.nextLine();
                success = validateInput(username, "tUsername");
                if (success)
                {
                    System.out.print("Enter Password: ");
                    password = inputScanner.nextLine();
                    success = validateInput(password, "tPassword");
                    if (success)
                    {
                        dashboard();
                        run = false;
                    } else
                    {
                        System.out.print("Incorrect Password!");
                        clearTerminal(true);
                    }
                } else
                {
                    System.out.print("No Such User Exists!");
                    clearTerminal(true);
                }
            } while (run);
        }

        private void signupMenu()
        {
            clearTerminal(false);
            System.out.println(
                    "========================================= \n\t\tSign Up \n=========================================");

            // Variables
            Scanner inputScanner = new Scanner(System.in);
            String password;

            // Getting Username + Password
            System.out.print("Enter Username: ");
            username = inputScanner.nextLine();
            if (validateInput(username, "tUsername"))
            {
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

        private boolean validateInput(String value, String field)
        {
            String QUERY = "SELECT * FROM users where (" + field + ", bisAdmin) IN (('" + value + "'," + isAdmin + "))";

            try (ResultSet rs = stmt.executeQuery(QUERY);)
            {
                while (rs.next())
                {
                    return true; // Success
                }
            } catch (Exception e)
            {
                return false; // Failed
            }
            return false;
        }

        private void addUsers(String username, String password)
        {
            String QUERY = "INSERT INTO users (tUsername, tPassword, bisAdmin) VALUES ('" + username + "', '" + password
                    + "', " + isAdmin + ")";

            try
            {
                stmt.executeUpdate(QUERY);
            } catch (Exception e)
            {
                System.out.println("Failed");
            }
        }

        // Abstract method to be implemented by child classes
        protected abstract void dashboard();
    }

    public class Admin extends AuthHandler implements Admins
    {
        private String userType = "Admin";
        private boolean isAdmin = true;

        // Constructor to set variable values to reflect admin
        public Admin(LibraryImp1 libObj)
        {
            super(libObj);
            this.setUserType(userType);
            this.setisAdmin(isAdmin);
        }

        @Override
        protected void dashboard()
        {
            clearTerminal(false);

            Scanner inputScanner = new Scanner(System.in);

            boolean run = true;
            int choice = 0;

            do
            {
                System.out.println(
                        "========================================= \n\t\tMain Menu \n========================================= ");

                System.out.print("[1] Add Book \n[2] Remove Book \n[3] Logout \nEnter Your Choice: ");
                choice = takeInput();
                switch (choice)
                {
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

        public void addBook()
        {
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
            try
            {
                publicationDate = format.parse(inputScanner.nextLine());

            } catch (Exception e)
            {
                System.out.print("Invalid date input! Setting publishing date to today...");
                publicationDate = new java.sql.Date(System.currentTimeMillis());
            }
            bookImpl newbook = new bookImpl(title, author, isbn, publisher, genre, publicationDate, genre);
            libObj.addBook(newbook);
        }

        public void removeBook()
        {
            clearTerminal(false);

            String isbn;
            Scanner inputScanner = new Scanner(System.in);

            System.out.print("Enter ISBN of book you'd like to remove: ");
            isbn = inputScanner.nextLine();

            libObj.removeBook(isbn);
        }

    }

    public class User extends AuthHandler implements Users
    {
        private String userType = "User";
        private boolean isAdmin = false;

        // Constructor to set variable values to reflect admin
        public User(LibraryImp1 libObj)
        {
            super(libObj);
            this.setUserType(userType);
            this.setisAdmin(isAdmin);
        }

        @Override
        protected void dashboard()
        {
            clearTerminal(false);

            Scanner inputScanner = new Scanner(System.in);

            boolean run = true;
            int choice = 0;

            do
            {
                System.out.println(
                        "========================================= \n\t\tMain Menu \n========================================= ");

                System.out.print("[1] Borrow Book \n[2] Return Book \n[3] Logout \nEnter Your Choice: ");
                choice = takeInput();
                switch (choice)
                {
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
        public void borrowBook()
        {
            clearTerminal(false);

            Scanner inputScanner = new Scanner(System.in);

            boolean run = true;
            int choice = 0;

            do
            {
                System.out.println(
                        "========================================= \n\t\tBorrow Book \n========================================= ");

                System.out.print(
                        "[1] Display All Books \n[2] Search by Title \n[3] Search By ISBN \n[4] Exit \nEnter Your Choice: ");
                choice = takeInput();
                switch (choice)
                {
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
        public void returnBook()
        {
            int j = 1;
            int choice;
            Scanner inputScanner = new Scanner(System.in);
            List<IBook> booksOwned = libObj.getBooksOwned(getUsername());

            System.out.println("Books Borrowed:");

            for (var iterable_element : booksOwned)
            {
                System.out.print("[" + j + "] ");
                iterable_element.displayInfo();
                j++;
            }

            if (j == 1)
            {
                System.out.println("You have no books borrowed!");
                clearTerminal(true);
                return;
            }

            do
            {
                System.out.print("Which Book Would You Like To Return: ");
                choice = takeInput() - 1;

                if (j < choice)
                {
                    System.out.print("Incorrect choice!");
                    System.out.println("");
                }
            } while (j < choice);

            String isbn = booksOwned.get(choice).getISBN();
            // Update DB
            String QUERY = "SELECT * FROM Books where ISBN = '" + isbn + "'";

            // Check if book records already exist
            try (ResultSet rs = stmt.executeQuery(QUERY))
            {
                if (rs.next())
                {
                    String updateQuery = "UPDATE Books SET BorrowedBy = NULL WHERE ISBN = '" + isbn + "'";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery))
                    {
                        updateStmt.executeUpdate();
                        System.out.println("Book Returned Successfully!");
                        // Delete from IBooks list.
                        for (int i = 0; i < libObj.libBooks.size(); i++)
                        {
                            if (libObj.libBooks.get(i).getISBN() == isbn)
                            {
                                libObj.libBooks.remove(i);
                            }
                        }
                        clearTerminal(true);
                        return;
                    } catch (Exception e)
                    {
                        System.out.println("Failed");
                    }

                }
            } catch (Exception e)
            {
                System.out.println("Failed");
            }

            clearTerminal(true);
        }
    }

    // Function to randomly generate 100 books in MySQL DB
    public int bookGenerator(int num)
    {
        String QUERY = "SELECT COUNT(*) AS TotCount FROM Books";

        // Check if book records already exist
        try (ResultSet rs = stmt.executeQuery(QUERY))
        {
            if (rs.next())
            {
                int value = rs.getInt("TotCount"); // Use the alias name "TotCount"
                if (value == 100)
                {
                    return 100;
                }
            }
        } catch (Exception e)
        {
            System.out.println("Failed");
        }

        // If they don't, then add books to the DB
        QUERY = "INSERT INTO Books (Title, Author, ISBN, Publisher, Genre, PublicationDate, BorrowedBy) VALUES ('Name"
                + num + "', 'Author" + num + "', '" + String.valueOf(num) + "', 'Publisher" + num + "', 'Genre" + num
                + "', '" + new java.sql.Date(System.currentTimeMillis()) + "', NULL)";

        try
        {
            stmt.executeUpdate(QUERY);
        } catch (Exception e)
        {
            System.out.println("Failed");
        }
        return num;
    }

    public static void clearTerminal(boolean waitInput)
    {
        // Wait for user input so that user can see the error
        if (waitInput)
        {
            System.out.println("");
            System.out.println("Press any key to continue...");

            try
            {
                new ProcessBuilder("cmd", "/c", "pause > nul").inheritIO().start().waitFor();
            } catch (IOException | InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        // Clear terminal
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void testConnection()
    {
        String QUERY = "SELECT * FROM users";

        try (ResultSet rs = stmt.executeQuery(QUERY);)
        {
            while (rs.next())
            {
                // Retrieve by column name
                System.out.println("Connection Successful!");
                break;
            }
        } catch (Exception e)
        {
            System.out.println("Failed");
        }
    }

    public static int takeInput()
    {
        Scanner inputScanner = new Scanner(System.in);
        String input = inputScanner.nextLine();
        int num = -1;

        try
        {
            num = Integer.parseInt(input);
        } catch (Exception e)
        {
            System.out.println("Invalid Input! Please try again.");
            clearTerminal(true);
        }
        return num;
    }

    public static void main(String[] args)
    {
        LibrarySystem librarySystem = new LibrarySystem();
        LibraryImp1 libObj = librarySystem.new LibraryImp1();

        testConnection();
        clearTerminal(false);

        Scanner inputScanner = new Scanner(System.in);

        boolean run = true;
        int choice = 0;
        User userObj = librarySystem.new User(libObj);
        Admin adminObj = librarySystem.new Admin(libObj);

        do
        {
            System.out.println(
                    "========================================= \n\t\tMain Menu \n========================================= ");

            System.out.print("[1] User \n[2] Admin \n[3] Exit \nEnter Your Choice: ");
            choice = takeInput();
            switch (choice)
            {
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