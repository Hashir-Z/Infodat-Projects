package com.library;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import com.library.LibrarySystem.User;

import java.io.IOException;
import java.sql.*;

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

        IBook searchBookByTitle(String title);

        IBook searchBookByISBN(String isbn);

        void displayAllBooks();
    }

    public class libraryImp1 implements ILibrary
    {
        List<IBook> libBooks = new ArrayList<>();

        // Constructor to add 100 books right from the get-go
        public libraryImp1()
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
            String QUERY = "INSERT INTO Book (Title, Author, ISBN, Publisher, Genre, PublicationDate, BorrowedBy) VALUES ("
                    + book.getTitle() + "," + book.getAuthor() + "," + book.getISBN() + "," + book.getPublisher() + ","
                    + book.getGenre() + "," + book.getPublicationDate() + ", NULL)";

            try (ResultSet rs = stmt.executeQuery(QUERY);)
            {
            } catch (Exception e)
            {
                System.out.println("Failed");
            }

            this.libBooks.add(book);
        }

        @Override
        public void removeBook(String isbn)
        {
            // Delete from DB
            String QUERY = "DELETE FROM BOOKS WHERE ISBN = '" + isbn + "')";

            try (ResultSet rs = stmt.executeQuery(QUERY);)
            {
                while (rs.next())
                {
                }
            } catch (Exception e)
            {
                System.out.println("Failed");
            }

            // Delete from IBooks list.
            for (int i = 0; i < libBooks.size(); i++)
            {
                if (libBooks.get(i).getISBN() == isbn)
                {
                    libBooks.remove(i);
                }
            }
        }

        @Override
        public IBook searchBookByTitle(String title)
        {
            for (int i = 0; i < libBooks.size(); i++)
            {
                if (libBooks.get(i).getTitle() == title)
                {
                    return (libBooks.get(i));
                }
            }
            return libBooks.get(libBooks.size() - 1);
        }

        @Override
        public IBook searchBookByISBN(String isbn)
        {
            for (int i = 0; i < libBooks.size(); i++)
            {
                if (libBooks.get(i).getISBN() == isbn)
                {
                    return libBooks.get(i);
                }
            }

            return libBooks.get(libBooks.size());
        }

        @Override
        public void displayAllBooks()
        {
            for (IBook IBook : libBooks)
            {
                IBook.displayInfo();
            }
        }
    }

    public interface Users
    {
        void borrowBook();

        void returnBook();
    }

    public abstract class UserAuthHandler
    {
        private String userType;
        private boolean isAdmin;

        libraryImp1 libObj;

        public UserAuthHandler(libraryImp1 libObj)
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
                choice = inputScanner.nextInt();
                switch (choice)
                {
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
                    System.out.println("Invalid Input!");
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
            String username;
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
            String username;
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

        private void addUsers(String Username, String Password)
        {
            String QUERY = "INSERT INTO users (tUsername, tPassword, bisAdmin) VALUES ('" + Username + "', '" + Password
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

    public class Admin extends UserAuthHandler
    {
        private String userType = "Admin";
        private boolean isAdmin = true;

        // Constructor to set variable values to reflect admin
        public Admin(libraryImp1 libObj)
        {
            super(libObj);
            this.setUserType(userType);
            this.setisAdmin(isAdmin);
        }

        @Override
        protected void dashboard()
        {
            libObj.displayAllBooks();
        }
    }

    public class User extends UserAuthHandler implements Users
    {
        private String userType = "User";
        private boolean isAdmin = false;

        // Constructor to set variable values to reflect admin
        public User(libraryImp1 libObj)
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

                System.out.print("[1] Borrow Book \n[2] Return Book \n[3] Exit \nEnter Your Choice: ");
                choice = inputScanner.nextInt();
                switch (choice)
                {
                case 1 -> // Display All Books
                {
                    libObj.displayAllBooks();
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
                    System.out.println("Invalid Input!");
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
                        "========================================= \n\t\tMain Menu \n========================================= ");

                System.out.print("[1] Borrow Book \n[2] Return Book \n[3] Exit \nEnter Your Choice: ");
                choice = inputScanner.nextInt();
                switch (choice)
                {
                case 1 -> // Display All Books
                {
                    libObj.displayAllBooks();
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
                    System.out.println("Invalid Input!");
                    clearTerminal(true);
                }

                }
            } while (run);
        }

        @Override
        public void returnBook()
        {
            System.out.println("Returning book: " + book.getTitle() + " by " + book.getAuthor());
            // Add logic to return the book
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

    public static void main(String[] args)
    {
        LibrarySystem librarySystem = new LibrarySystem();
        libraryImp1 libObj = librarySystem.new libraryImp1();

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
            choice = inputScanner.nextInt();
            switch (choice)
            {
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
                System.out.println("Invalid Input!");
                clearTerminal(true);
            }

            }
        } while (run);

        inputScanner.close();
    }

}