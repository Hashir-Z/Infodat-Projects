package com.infodat.library_borrow_springboot.api.dto;

import com.infodat.library_borrow_springboot.api.entity.BooksEntity;
import org.mapstruct.Mapper;

@Mapper
public interface BooksMapper{
    BooksEntity bookDtoToBook(BooksDTO booksDTO);
}