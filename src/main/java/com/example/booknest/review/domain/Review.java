package com.example.booknest.review.domain;

import com.example.booknest.transaction.domain.Transaction;
import com.example.booknest.user.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReview;

    @Min(1)
    @Max(5)
    private Integer rating;

    @Column(length = 500)
    private String comment;

    private LocalDate reviewDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "reviewer_user_id")
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "reviewed_user_id")
    private User reviewed;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;
}
