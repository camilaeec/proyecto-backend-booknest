package com.example.booknest.review.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReviewRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setReviewerNickname("usuario1");
        request.setReviewedNickname("usuario2");
        request.setTransactionId(1L);
        request.setRating(5);
        request.setComment("Excelente servicio");

        Set<ConstraintViolation<ReviewRequestDTO>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenReviewerNicknameNull_thenViolation() {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setReviewedNickname("usuario2");
        request.setTransactionId(1L);
        request.setRating(5);
        request.setComment("Excelente servicio");

        Set<ConstraintViolation<ReviewRequestDTO>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("El nickname del revisor es obligatorio", violations.iterator().next().getMessage());
    }

    @Test
    void whenReviewedNicknameNull_thenViolation() {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setReviewerNickname("usuario1");
        request.setTransactionId(1L);
        request.setRating(5);
        request.setComment("Excelente servicio");

        Set<ConstraintViolation<ReviewRequestDTO>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("El nickname del reseñado es obligatorio", violations.iterator().next().getMessage());
    }

    @Test
    void whenTransactionIdNull_thenViolation() {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setReviewerNickname("usuario1");
        request.setReviewedNickname("usuario2");
        request.setRating(5);
        request.setComment("Excelente servicio");

        Set<ConstraintViolation<ReviewRequestDTO>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("El ID de transacción es obligatorio", violations.iterator().next().getMessage());
    }

    @Test
    void whenRatingTooLow_thenViolation() {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setReviewerNickname("usuario1");
        request.setReviewedNickname("usuario2");
        request.setTransactionId(1L);
        request.setRating(0);
        request.setComment("Excelente servicio");

        Set<ConstraintViolation<ReviewRequestDTO>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("mayor que o igual a 1"));
    }

    @Test
    void whenRatingTooHigh_thenViolation() {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setReviewerNickname("usuario1");
        request.setReviewedNickname("usuario2");
        request.setTransactionId(1L);
        request.setRating(6);
        request.setComment("Excelente servicio");

        Set<ConstraintViolation<ReviewRequestDTO>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("menor que o igual a 5"));
    }

    @Test
    void whenCommentTooLong_thenViolation() {
        String longComment = "a".repeat(501);
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setReviewerNickname("usuario1");
        request.setReviewedNickname("usuario2");
        request.setTransactionId(1L);
        request.setRating(5);
        request.setComment(longComment);

        Set<ConstraintViolation<ReviewRequestDTO>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("tamaño debe estar entre"));
    }
}