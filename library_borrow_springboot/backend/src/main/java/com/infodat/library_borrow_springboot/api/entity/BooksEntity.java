package com.infodat.library_borrow_springboot.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
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

    @JsonFormat(pattern = "MM/dd/yyyy")
    @Column(name = "publicationdate")
    private LocalDate publicationdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowedby")
    private UsersEntity borrowedby;

    @Id
    @JsonProperty("ISBN")
    private String ISBN;
}