package com.example.booknest.transaction.domain;

import com.example.booknest.book.domain.Book;
import com.example.booknest.user.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaction;

    private Boolean isExchange;

    @ManyToOne
    @JoinColumn(name="id_buyer")
    @JsonBackReference
    private User buyer;

    @ManyToOne
    @JoinColumn(name="id_seller")
    @JsonBackReference
    private User seller;

    @OneToOne
    @JsonManagedReference
    private Book book;
}
