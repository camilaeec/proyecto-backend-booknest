package com.example.booknest.book.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBook;
    private String title;
    private String author;
    private String publisher;
    private String year_of_publication;
    private String state;
    private String price;
    private Boolean exchange;
    //falta agregar el id del usuario que publica el libro
}
