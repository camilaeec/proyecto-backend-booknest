package com.example.booknest.book.domain;

import com.example.booknest.book.dto.BookBasicDTO;
import com.example.booknest.book.infraestructure.BookRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public Book createBook(BookBasicDTO dto) {
        Book book = modelMapper.map(dto, Book.class);

        book.setPrice(dto.getPrice() != null ? dto.getPrice() : 0.0);

        /*
        if (dto.getUserId() != null) {
            User user = userService.getUserById(dto.getUserId());
            book.setUser(user);
        }
        */

        return bookRepository.save(book);
    }

    public Book updatePrice(Long id, Double price) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setPrice(price);
        return bookRepository.save(book);
    }

    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public List<Book> getByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> getByAuthor(String author) {
        return bookRepository.findByAuthorsContaining(author);
    }

    public List<Book> getByTag(String tag) {
        return bookRepository.findByTagsContaining(tag);
    }

    public List<Book> getByPrice(Double price) {
        return bookRepository.findByPrice(price);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found");
        }
        bookRepository.deleteById(id);
    }
}