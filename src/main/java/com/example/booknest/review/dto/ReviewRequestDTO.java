package com.example.booknest.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {
    @NotNull(message = "El nickname del revisor es obligatorio")
    private String reviewerNickname;

    @NotNull(message = "El nickname del reseñado es obligatorio")
    private String reviewedNickname;

    @NotNull(message = "El ID de transacción es obligatorio")
    private Long transactionId;

    @Min(1)
    @Max(5)
    private Integer rating;

    @Size(max = 500)
    private String comment;
}