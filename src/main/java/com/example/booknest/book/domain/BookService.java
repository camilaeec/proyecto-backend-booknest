package com.example.booknest.book.domain;

import com.example.booknest.book.dto.*;
import com.example.booknest.book.infraestructure.BookRepository;
import com.example.booknest.exception.ResourceNotFoundException;
import com.example.booknest.user.domain.User;
import com.example.booknest.user.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public BookResponse createBook(CreateBookRequest request) {
        User owner = userService.getMeLocal(); //hola, el error está acá

        Book book = modelMapper.map(request, Book.class);
        book.setUser(owner);


        Book savedBook = bookRepository.save(book);
        return modelMapper.map(savedBook, BookResponse.class);
    }

    // ====================== OBTENER LIBRO POR ID ======================
    public Optional<BookResponse> getBookById(Long id) {
        return bookRepository.findById(id)
                .map(book -> modelMapper.map(book, BookResponse.class));
    }

    public List<BookResponse> getByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(book -> modelMapper.map(book, BookResponse.class))
                .collect(Collectors.toList());
    }

    public List<BookResponse> getByAuthor(String author) {
        return bookRepository.findByAuthorsContaining(author).stream()
                .map(book -> modelMapper.map(book, BookResponse.class))
                .collect(Collectors.toList());
    }

    public List<BookResponse> getByTag(String tag) {
        return bookRepository.findByTagsContaining(tag).stream()
                .map(book -> modelMapper.map(book, BookResponse.class))
                .collect(Collectors.toList());
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Libro no encontrado");
        }
        bookRepository.deleteById(id);
    }

    public boolean isBookOwner(String userEmail, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));
        return book.getUser().getEmail().equals(userEmail);
    }
}