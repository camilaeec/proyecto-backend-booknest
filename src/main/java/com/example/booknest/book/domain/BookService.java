package com.example.booknest.book.domain;

import com.example.booknest.book.dto.BookBasicDTO;
import com.example.booknest.book.infraestructure.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Book createBook(BookBasicDTO dto) {
        //user .get(id)
        Book book = new Book();
        book.setTitle(dto.title);
        book.setAuthors(dto.authors!=null?dto.authors:new ArrayList<>());
        book.setTags(dto.tags!=null?dto.tags:new ArrayList<>());
        //book.setPrice(dto.price!=null?dto.price:new Double(0));
        book.setBookPhotos(dto.bookPhotos!=null?dto.bookPhotos:new ArrayList<>());
        return bookRepository.save(book);
    }

    public Book updatePrice(Long id, Double price) {
        Book book = bookRepository.findById(id).orElseThrow(()->new RuntimeException("Book not found"));
        book.setPrice(price);
        return bookRepository.save(book);
    }

    public Book getById(Long id) {
        return bookRepository.findById(id).orElseThrow(()->new RuntimeException("Book not found"));
    }

    public List<Book> getByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> getByAuthor(String author) {
        return bookRepository.findByAuthorContaining(author);
    }

    public List<Book> getByTag(String tag) {
        return bookRepository.findByTagContaining(tag);
    }

    public List<Book> getByPrice(Double price) {
        return bookRepository.findByPrice(price);
    }

    public void deleteBook(Long id) {
        if(!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found");
        }
        bookRepository.deleteById(id);
    }
}
