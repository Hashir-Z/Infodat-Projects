// This file interacts directly with the DB
// A service has business logic etc. to handle the specifics of the requests.
// The service may need to fetch or update data in a database. The raw querying/updating of the data is the responsibility of the repository.

package com.infodat.library_borrow_springboot.api.service;

import com.infodat.library_borrow_springboot.api.dto.BooksDTO;
import com.infodat.library_borrow_springboot.api.dto.BooksMapper;
import com.infodat.library_borrow_springboot.api.entity.BooksEntity;
import com.infodat.library_borrow_springboot.api.repository.BooksRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.awt.print.Book;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BooksService {
    private final BooksRepository booksRepository;
    private final ModelMapper modelMapper;
    private final BooksMapper booksMapper;

    public Map<String, Object> findISBN(String isbn) {
        try {
            Optional<BooksEntity> booksEntityOptional = booksRepository.findBooksEntitiesByISBN(isbn);

            if (booksEntityOptional.isEmpty()) {
                throw new IllegalStateException("Incorrect ISBN or Book Not in Library");
            }

            BooksDTO booksDTO = modelMapper.map(booksEntityOptional.get(), BooksDTO.class);
            Map<String, Object> bookDetails = new HashMap<>();
            bookDetails.put("title", booksDTO.getTitle());
            bookDetails.put("author", booksDTO.getAuthor());
            bookDetails.put("publisher", booksDTO.getPublisher());
            bookDetails.put("genre", booksDTO.getGenre());
            bookDetails.put("isbn", booksDTO.getIsbn());

            return bookDetails;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("An error occurred while fetching the book", e);
        }
    }

    public ResponseEntity<List<BooksDTO>> getAllBooks() {
        List<BooksEntity> booksEntities = booksRepository.findAll();
        List<BooksDTO> booksDTOs = booksEntities.stream()
                .map(book -> modelMapper.map(book, BooksDTO.class))
                .toList();
        return ResponseEntity.ok(booksDTOs);
    }

    public String addBook(BooksDTO bookDTO) {
        try {
            BooksEntity book = booksMapper.bookDtoToBook(bookDTO);
            booksRepository.save(book);
            return "Successfully added Book";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String deleteBook(String isbn) {
        try {
            Optional<BooksEntity> book = booksRepository.findBooksEntitiesByISBN(isbn);
            if (book.isEmpty()) {
                throw new IllegalStateException("Incorrect ISBN or Book Not in Library");
            }
            booksRepository.delete(book.get());
            return "Successfully deleted Book";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}