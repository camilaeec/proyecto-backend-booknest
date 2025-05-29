package com.example.booknest.review.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReviewResponseDtoValidationTest {

    @Test
    void testReviewResponseDtoStructure() {
        ReviewResponseDTO response = new ReviewResponseDTO();
        response.setIdReview(1L);
        response.setReviewerNickname("usuario1");
        response.setReviewedNickname("usuario2");
        response.setTransactionId(1L);
        response.setRating(5);
        response.setComment("Excelente servicio");
        response.setReviewDate(LocalDate.now());

        assertNotNull(response);
        assertEquals(1L, response.getIdReview());
        assertEquals("usuario1", response.getReviewerNickname());
        assertEquals("usuario2", response.getReviewedNickname());
        assertEquals(1L, response.getTransactionId());
        assertEquals(5, response.getRating());
        assertEquals("Excelente servicio", response.getComment());
        assertNotNull(response.getReviewDate());
    }
}