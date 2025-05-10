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
    @NotNull(message = "El ID del revisor es obligatorio")
    private Long reviewerUserId;

    @NotNull(message = "El ID del calificado es obligatorio")
    private Long reviewedUserId;

    @Min(1)
    @Max(5)
    private Integer rating;

    @Size(max = 500)
    private String comment;
}