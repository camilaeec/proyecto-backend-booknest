package com.example.booknest.review.domain;

import com.example.booknest.user.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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

    private LocalDate dateOfRating=LocalDate.now();

    @ManyToOne
    @JoinColumn(name="id_reviewer_user")
    @JsonBackReference
    private User reviewerUser;

    @ManyToOne
    @JoinColumn(name="id_reviewed_user")
    @JsonManagedReference
    private User reviewedUser;

}
