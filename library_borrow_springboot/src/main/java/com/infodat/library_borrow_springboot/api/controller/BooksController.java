// This provides the user with access to backend. The user will be interacting with this layer
// Controller is solely responsible for receiving a request and routing it to the appropriate service for processing.

package com.infodat.library_borrow_springboot.api.controller;

import com.infodat.library_borrow_springboot.api.entity.BooksEntity;
import com.infodat.library_borrow_springboot.api.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/books")
public class BooksController {
GI
    // Variable
    private final BooksService booksService;

    // Constructor
    @Autowired
    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @PostMapping("/find-isbn")
    public Optional<BooksEntity> findBook(String isbn) {
        return booksService.findISBN(isbn);
    }
}
