package com.example.booknest.transaction.domain;

import com.example.booknest.book.domain.Book;
import com.example.booknest.user.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaction;

    @Column(name="Exchange", nullable=false)
    private Boolean isExchange;

    @Column(name="Cost")
    private Integer cost;

    @Column(name="Date", nullable=false)
    private Date date;

    @Column(name="Accepted")
    private Boolean Accepted;

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
