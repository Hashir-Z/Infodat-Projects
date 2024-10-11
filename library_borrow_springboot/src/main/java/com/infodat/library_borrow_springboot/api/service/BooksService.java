// This file interacts directly with the DB
// A service has business logic etc. to handle the specifics of the requests.
// The service may need to fetch or update data in a database. The raw querying/updating of the data is the responsibility of the repository.

package com.infodat.library_borrow_springboot.api.service;

import com.infodat.library_borrow_springboot.api.entity.BooksEntity;
import com.infodat.library_borrow_springboot.api.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public Optional<BooksEntity> findISBN(String isbn) {
        try {
            Optional<BooksEntity> booksEntityOptional = booksRepository.findBooksEntitiesByISBN(isbn);

            if (booksEntityOptional.isEmpty()) {
                // No book found
                throw new IllegalStateException("Incorrect ISBN or Book Not in Library");
            }
            return booksEntityOptional;
        } catch (IllegalStateException e) {
            // Re-throw the specific exception for no book found
            throw e;
        } catch (Exception e) {
            // Handle other exceptions
            throw new IllegalStateException("An error occurred while fetching the book", e);
        }
    }
}