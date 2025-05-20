package com.example.booknest.book.application;

import com.example.booknest.book.domain.Book;
import com.example.booknest.book.domain.BookService;
import com.example.booknest.book.dto.BookBasicDTO;
import com.example.booknest.book.infraestructure.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookRepository bookRepository;
    private final BookService bookService;

    @PreAuthorize("hasAnyRole('COMMON_USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody BookBasicDTO dto) {
        return ResponseEntity.ok(bookService.createBook(dto));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<Book>> getBooksByTitle(@PathVariable String title) {
        return ResponseEntity.ok(bookService.getByTitle(title));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) { //para denunciar publicacion
        Optional<Book> book = bookRepository.findById(id);
        return ResponseEntity.ok(book.orElse(null));
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(bookService.getByAuthor(author));
    }

    @GetMapping("tag/{tag}")
    public ResponseEntity<List<Book>> getBooksByTag(@PathVariable String tag) {
        return ResponseEntity.ok(bookService.getByTag(tag));
    }

    @GetMapping("/price/{price}")
    public ResponseEntity<List<Book>> getBooksByPrice(@PathVariable Double price) {
        return ResponseEntity.ok(bookService.getByPrice(price));
    }

    @PreAuthorize("hasRole('ADMIN') or @bookService.isBookOwner(authentication.name, #id)")
    @PatchMapping("{id}/price")
    public ResponseEntity<Book> updateBookPrice(@PathVariable Long id, @RequestBody Double newPrice) {
        return ResponseEntity.ok(bookService.updatePrice(id, newPrice));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
