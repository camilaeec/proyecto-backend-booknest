package com.example.booknest.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReviewResponseDTO {
    private Long idReview;
    //private UserResponseDTO reviewerUser;
    //private UserResponseDTO reviewedUser;
    private Integer rating;
    private String comment;
    private LocalDate reviewDate;
}
