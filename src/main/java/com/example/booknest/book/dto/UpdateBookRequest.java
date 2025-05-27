package com.example.booknest.book.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class UpdateBookRequest {
    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotEmpty(message = "Debe tener al menos un autor")
    private List<@NotBlank String> authors;

    private List<String> tags;

    @NotBlank(message = "La editorial es obligatoria")
    private String publisher;

    @NotBlank(message = "El año de publicación es obligatorio")
    private String yearOfPublication;

    @NotBlank(message = "El estado del libro es obligatorio")
    private String state;

    @NotEmpty(message = "Debe subir al menos una foto")
    private List<@NotBlank String> bookPhotos;
}