package com.infodat.library_borrow_springboot.api.dto;

import com.infodat.library_borrow_springboot.api.entity.BooksEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.awt.print.Book;

@Mapper
public interface BooksMapper{

    BooksEntity bookDtoToBook(BooksDTO booksDTO);
}