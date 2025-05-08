package com.example.booknest.book.infraestructure;

import com.example.booknest.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String titulo);

    List<Book> findByAuthorContaining(String author);

    List<Book> findByTagContaining(String category);

    List<Book> findByPrice(Double price);
}
