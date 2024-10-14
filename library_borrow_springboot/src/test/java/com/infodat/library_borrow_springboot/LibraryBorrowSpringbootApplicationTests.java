package com.infodat.library_borrow_springboot;

import com.infodat.library_borrow_springboot.api.dto.BooksDTO;
import com.infodat.library_borrow_springboot.api.dto.ModelMapperConfig;
import com.infodat.library_borrow_springboot.api.entity.BooksEntity;
import com.infodat.library_borrow_springboot.api.service.BooksService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class LibraryBorrowSpringbootApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BooksService booksService;

	@Autowired
	private ModelMapper modelMapper;

	private PodamFactory podamFactory = new PodamFactoryImpl();

	@Test
	void contextLoads() throws Exception {
		getAllBooks_ShouldReturnBooksList();
		findBook_ShouldReturnBook();
		addBook_ShouldAddBook();
		deleteBook_ShouldDeleteBook();
	}

	@Test
	public void getAllBooks_ShouldReturnBooksList() throws Exception {
		BooksEntity book = podamFactory.manufacturePojo(BooksEntity.class);
		List<BooksEntity> books = List.of(book);
		List<BooksDTO> booksDTOs = books.stream()
				.map(b -> modelMapper.map(b, BooksDTO.class))
				.toList();

		when(booksService.getAllBooks()).thenReturn(ResponseEntity.ok(booksDTOs));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/get-all-books")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void findBook_ShouldReturnBook() throws Exception {
		BooksEntity book = podamFactory.manufacturePojo(BooksEntity.class);
		BooksDTO bookDTO = modelMapper.map(book, BooksDTO.class);

		// Manually set ISBN because it is not being set for some reason
		book.setISBN("1000");
		bookDTO.setIsbn("1000");

		Map<String, Object> bookDetails = Map.of(
				"title", bookDTO.getTitle(),
				"author", bookDTO.getAuthor(),
				"isbn", bookDTO.getIsbn(),
				"genre", bookDTO.getGenre(),
				"publisher", bookDTO.getPublisher()
		);

		when(booksService.findISBN(book.getISBN())).thenReturn(bookDetails);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books/find-isbn")
						.param("isbn", book.getISBN())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getISBN()))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void addBook_ShouldAddBook() throws Exception {
		BooksDTO bookDTO = podamFactory.manufacturePojo(BooksDTO.class);

		when(booksService.addBook(any(BooksDTO.class))).thenReturn("Successfully added Book");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books/add-book")
						.content(new ObjectMapper().writeValueAsString(bookDTO))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Successfully added Book"))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void deleteBook_ShouldDeleteBook() throws Exception {
		String isbn = podamFactory.manufacturePojo(String.class);

		when(booksService.deleteBook(isbn)).thenReturn("Successfully deleted Book");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books/delete-book")
						.param("isbn", isbn)
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Successfully deleted Book"))
				.andDo(MockMvcResultHandlers.print());

		verify(booksService, times(1)).deleteBook(isbn);
	}
}