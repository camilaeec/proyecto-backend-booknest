package com.example.booknest.book.application;

import com.example.booknest.book.domain.Book;
import com.example.booknest.book.domain.BookService;
import com.example.booknest.book.dto.*;
import com.example.booknest.book.infraestructure.BookRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/recent")
    public ResponseEntity<List<BookResponse>> getRecentBooks() {
        Pageable topFive = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Book> recentBooks = bookRepository.findAll(topFive).getContent();

        return ResponseEntity.ok(recentBooks.stream()
                .map(book -> modelMapper.map(book, BookResponse.class))
                .collect(Collectors.toList()));
    }

    // ====================== CREAR LIBRO ======================

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMMON_USER')")
    public ResponseEntity<BookResponse> createBook(
            @RequestBody @Valid CreateBookRequest request
    ) {
        // —– DEBUG: imprime usuario y roles —–
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("=== CREATE BOOK: user=" + auth.getName());
        for (GrantedAuthority authority : auth.getAuthorities()) {
            System.out.println("   authority=" + authority.getAuthority());
        }
        // —– FIN DEBUG —–

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