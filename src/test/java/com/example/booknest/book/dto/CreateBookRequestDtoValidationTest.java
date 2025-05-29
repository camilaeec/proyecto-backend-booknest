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

class CreateBookRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Effective Java");
        request.setAuthors(List.of("Joshua Bloch"));
        request.setPublisher("Addison-Wesley");
        request.setYearOfPublication("2008");
        request.setState("Nuevo");
        request.setBookPhotos(List.of("photo1.jpg"));

        Set<ConstraintViolation<CreateBookRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenTitleBlank_thenViolation() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle(""); // Blank
        request.setAuthors(List.of("Joshua Bloch"));
        request.setPublisher("Addison-Wesley");
        request.setYearOfPublication("2008");
        request.setState("Nuevo");
        request.setBookPhotos(List.of("photo1.jpg"));

        Set<ConstraintViolation<CreateBookRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("El título es obligatorio", violations.iterator().next().getMessage());
    }

    @Test
    void whenAuthorsEmpty_thenViolation() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Effective Java");
        request.setAuthors(List.of()); // Empty
        request.setPublisher("Addison-Wesley");
        request.setYearOfPublication("2008");
        request.setState("Nuevo");
        request.setBookPhotos(List.of("photo1.jpg"));

        Set<ConstraintViolation<CreateBookRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Debe tener al menos un autor", violations.iterator().next().getMessage());
    }

    @Test
    void whenAuthorElementBlank_thenViolation() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Effective Java");
        request.setAuthors(List.of("")); // Blank author
        request.setPublisher("Addison-Wesley");
        request.setYearOfPublication("2008");
        request.setState("Nuevo");
        request.setBookPhotos(List.of("photo1.jpg"));

        Set<ConstraintViolation<CreateBookRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("no debe estar vacío"));
    }

    @Test
    void whenPublisherBlank_thenViolation() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Effective Java");
        request.setAuthors(List.of("Joshua Bloch"));
        request.setPublisher(""); // Blank
        request.setYearOfPublication("2008");
        request.setState("Nuevo");
        request.setBookPhotos(List.of("photo1.jpg"));

        Set<ConstraintViolation<CreateBookRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("La editorial es obligatoria", violations.iterator().next().getMessage());
    }

    @Test
    void whenBookPhotosEmpty_thenViolation() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Effective Java");
        request.setAuthors(List.of("Joshua Bloch"));
        request.setPublisher("Addison-Wesley");
        request.setYearOfPublication("2008");
        request.setState("Nuevo");
        request.setBookPhotos(List.of()); // Empty

        Set<ConstraintViolation<CreateBookRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Debe subir al menos una foto", violations.iterator().next().getMessage());
    }
}