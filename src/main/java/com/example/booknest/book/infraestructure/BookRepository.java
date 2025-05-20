package com.example.booknest.book.infraestructure;

import com.example.booknest.book.domain.Book;
import com.example.booknest.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String titulo);

    List<Book> findByTagsContaining(String tag);

    List<Book> findByPrice(Double price);

    List<Book> findByUser(User user);

    @Query(value = """
        SELECT * FROM book b
        WHERE EXISTS (
            SELECT 1 FROM unnest(b.authors) AS author
            WHERE LOWER(author) LIKE LOWER(concat('%', :author, '%'))
        """, nativeQuery = true)
    List<Book> findByAuthorsContaining(@Param("author") String author);
}
