package com.infodat.library_borrow_springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infodat.library_borrow_springboot.api.controller.BooksController;
import com.infodat.library_borrow_springboot.api.entity.BooksEntity;
import com.infodat.library_borrow_springboot.api.service.BooksService;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.api.PodamFactory;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebMvcTest(BooksController.class)
public class BooksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BooksService booksService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PodamFactory podamFactory;

    @Test
    void contextLoads() {

    }

    @Test
    public void getAllBooks_ShouldReturnBooksList() throws Exception {
        List<BooksEntity> books = List.of(new BooksEntity());
        //when(booksService.getAllBooks()).thenReturn(Optional.of(books));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/get-all-books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$[0].title").value("Book1"));
    }

    @Test
    public void findBook_ShouldReturnBook() throws Exception {
        BooksEntity book = new BooksEntity();
        //when(booksService.findISBN("101")).thenReturn(Optional.of(book));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books/find-isbn")
                        .param("isbn", "101")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.title").value("Book1"));
    }

    @Test
    public void addBook_ShouldAddBook() throws Exception {
        BooksEntity book = new BooksEntity();
        //when(booksService.addBook(any(BooksEntity.class))).thenReturn("Book added successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books/add-book")
                        .content(new ObjectMapper().writeValueAsString(book))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Book added successfully"));
    }

    @Test
    public void deleteBook_ShouldDeleteBook() throws Exception {
        when(booksService.deleteBook("101")).thenReturn("Book deleted successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books/delete-book")
                        .param("isbn", "101")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Book deleted successfully"));

        verify(booksService, times(1)).deleteBook("101");
    }
}