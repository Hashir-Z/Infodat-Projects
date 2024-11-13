package com.infodat.library_borrow_springboot.api.repository;

import com.infodat.library_borrow_springboot.api.entity.BooksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BooksRepository extends
        JpaRepository<BooksEntity, String> {
    Optional<BooksEntity> findBooksEntitiesByISBN(String isbn);
}