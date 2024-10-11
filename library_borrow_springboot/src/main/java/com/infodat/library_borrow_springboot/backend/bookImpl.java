package com.infodat.library_borrow_springboot.backend;

import java.util.Date;

public class bookImpl implements IBook {
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String genre;
    private Date publicationDate;
    private String borrowedBy;

    // Constructor to set values of books
    public bookImpl(String title, String author, String isbn, String publisher, String genre, Date publicationDate,
                    String borrowedBy) {
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
    public String getTitle() {
        return title;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getISBN() {
        return isbn;
    }

    @Override
    public String getPublisher() {
        return publisher;
    }

    @Override
    public String getGenre() {
        return genre;
    }

    @Override
    public Date getPublicationDate() {
        return publicationDate;
    }

    @Override
    public String getBorrowedBy() {
        return borrowedBy;
    }

    @Override
    public void setBorrowedBy(String username) {
        this.borrowedBy = username;
    }

    @Override
    public void displayInfo() {
        System.out.println("Name: " + title);
        System.out.println("Author: " + author);
        System.out.println("ISBN: " + isbn);
        System.out.println("Publisher: " + publisher);
        System.out.println("Genre: " + genre);
        System.out.println("Publication Date: " + publicationDate);
        System.out.println();
    }
}

