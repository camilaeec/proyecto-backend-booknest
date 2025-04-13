package com.example.booknest.book.domain;

import com.example.booknest.transaction.domain.Transaction;
import com.example.booknest.user.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name="users")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBook;
    private String title;
    private String author;
    private String publisher;
    private String yearOfPublication;
    private String state;
    private String price;
    private Boolean exchange;

    @ManyToOne
    @JoinColumn(name= "id_user")
    @JsonBackReference
    private User user;

    @OneToOne(mappedBy = "book")
    @JsonBackReference
    private Transaction transaction;
}
