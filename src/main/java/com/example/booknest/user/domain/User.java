package com.example.booknest.user.domain;

import com.example.booknest.book.domain.Book;
import com.example.booknest.review.domain.Review;
import com.example.booknest.transaction.domain.Transaction;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_user")
    private Long idUser;

    @Column(name="email", nullable=false)
    private String email;

    @Column(name="username", nullable=false, unique=true)
    private String username;

    @Column(name="password", nullable=false)
    private String password;

    @Column(name="location", nullable=false)
    private String location;

    @Column(name="phone_number", nullable=false)
    private String phoneNumber;

    @Column(name="date_of_registration")
    private LocalDate dateOfRegistration=LocalDate.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Book> books;

    @OneToMany(mappedBy= "buyer")
    @JsonManagedReference
    private List<Transaction> purchases;

    @OneToMany(mappedBy = "seller")
    @JsonManagedReference
    private List<Transaction> sales;

    @OneToMany(mappedBy = "reviewer")
    @JsonManagedReference
    private List<> reviews;

    @OneToMany(mappedBy = "reviewed")
    @JsonManagedReference
    private List<Review> reviewsReceived;
    }
