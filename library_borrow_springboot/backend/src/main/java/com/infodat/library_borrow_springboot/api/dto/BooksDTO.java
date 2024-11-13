package com.infodat.library_borrow_springboot.api.dto;

import lombok.Data;

@Data
public class BooksDTO {
    private String title;
    private String author;
    private String publisher;
    private String genre;
    private String isbn;
}