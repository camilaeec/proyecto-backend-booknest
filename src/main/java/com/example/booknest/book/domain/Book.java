package com.example.booknest.book.domain;

import com.example.booknest.transaction.domain.Transaction;
import com.example.booknest.user.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name="book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBook;

    private String title;

    @ElementCollection
    private List<String> authors;

    @ElementCollection
    private List<String> tags;

    private String publisher;

    private String yearOfPublication;

    private String state;

    @ElementCollection
    private List<String> bookPhotos; //guarda las rutas de las fotos

    @ManyToOne
    @JoinColumn(name= "id_user")
    private User user;

    @OneToOne(mappedBy = "book")
    private Transaction transaction;
}
