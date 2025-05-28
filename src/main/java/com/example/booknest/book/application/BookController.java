package com.example.booknest.book.application;

import com.example.booknest.book.domain.BookService;
import com.example.booknest.book.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    // ====================== CREAR LIBRO ======================
    @PostMapping
    @PreAuthorize("hasAnyRole('COMMON_USER')")
    public ResponseEntity<BookResponse> createBook(
            @RequestBody @Valid CreateBookRequest request
    ) {
        BookResponse response = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ====================== BUSCAR LIBROS ======================
    @GetMapping("/title/{title}")
    public ResponseEntity<List<BookResponse>> getBooksByTitle(
            @PathVariable String title
    ) {
        return ResponseEntity.ok(bookService.getByTitle(title));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookResponse>> getBooksByAuthor(
            @PathVariable String author
    ) {
        return ResponseEntity.ok(bookService.getByAuthor(author));
    }

    @GetMapping("/tag/{tag}") // Corregido: Añadido "/" antes de "tag"
    public ResponseEntity<List<BookResponse>> getBooksByTag(
            @PathVariable String tag
    ) {
        return ResponseEntity.ok(bookService.getByTag(tag));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}