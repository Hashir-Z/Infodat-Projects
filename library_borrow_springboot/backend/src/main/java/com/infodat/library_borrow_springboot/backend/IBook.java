package com.infodat.library_borrow_springboot.backend;

import java.util.Date;

public interface IBook {
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
