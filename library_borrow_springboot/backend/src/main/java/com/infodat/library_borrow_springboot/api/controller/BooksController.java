// This provides the user with access to backend. The user will be interacting with this layer
// Controller is solely responsible for receiving a request and routing it to the appropriate service for processing.

package com.infodat.library_borrow_springboot.api.controller;

import com.infodat.library_borrow_springboot.api.dto.BooksDTO;
import com.infodat.library_borrow_springboot.api.entity.BooksEntity;
import com.infodat.library_borrow_springboot.api.service.BooksService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/books")
public class BooksController {
    // Variable
    private final BooksService booksService;

    @GetMapping("/get-all-books")
    public ResponseEntity<List<BooksDTO>> getAllBooks() {
        return booksService.getAllBooks();
    }

    @PostMapping("/find-isbn")
    public Map<String, Object> findBook(@RequestParam String isbn) {
        return booksService.findISBN(isbn);
    }

    @PostMapping("/add-book")
    public String addBook(@RequestBody BooksDTO bookDTO) {
        return booksService.addBook(bookDTO);
    }

    @PostMapping("/delete-book")
    public String deleteBook(@RequestParam String isbn) {
        return booksService.deleteBook(isbn);
    }
}