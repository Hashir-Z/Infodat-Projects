package com.infodat.library_borrow_springboot.backend;

import java.util.List;

public interface ILibrary {
    void addBook(IBook book);

    void removeBook(String isbn);

    void searchBook(String username, String field);

    void displayAllBooks();
}