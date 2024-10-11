package com.infodat.library_borrow_springboot.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "books")
public class BooksEntity {
    @Column(name = "title", length = 400)
    private String title;

    @Column(name = "author", length = 400)
    private String author;

    @Column(name = "publisher", length = 400)
    private String publisher;

    @Column(name = "genre", length = 400)
    private String genre;

    @Column(name = "publicationdate")
    private LocalDate publicationdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowedby")
    private UsersEntity borrowedby;

    @Id
    private String ISBN;
}