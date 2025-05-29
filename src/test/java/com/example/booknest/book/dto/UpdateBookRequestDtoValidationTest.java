package com.example.booknest.book.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateBookRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        UpdateBookRequest request = new UpdateBookRequest();
        request.setTitle("Effective Java");
        request.setAuthors(List.of("Joshua Bloch"));
        request.setPublisher("Addison-Wesley");
        request.setYearOfPublication("2008");
        request.setState("Usado - Buen estado");
        request.setBookPhotos(List.of("photo1.jpg"));

        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenYearOfPublicationBlank_thenViolation() {
        UpdateBookRequest request = new UpdateBookRequest();
        request.setTitle("Effective Java");
        request.setAuthors(List.of("Joshua Bloch"));
        request.setPublisher("Addison-Wesley");
        request.setYearOfPublication(""); // Blank
        request.setState("Nuevo");
        request.setBookPhotos(List.of("photo1.jpg"));

        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("El año de publicación es obligatorio", violations.iterator().next().getMessage());
    }

    @Test
    void whenStateBlank_thenViolation() {
        UpdateBookRequest request = new UpdateBookRequest();
        request.setTitle("Effective Java");
        request.setAuthors(List.of("Joshua Bloch"));
        request.setPublisher("Addison-Wesley");
        request.setYearOfPublication("2008");
        request.setState(""); // Blank
        request.setBookPhotos(List.of("photo1.jpg"));

        Set<ConstraintViolation<UpdateBookRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("El estado del libro es obligatorio", violations.iterator().next().getMessage());
    }
}