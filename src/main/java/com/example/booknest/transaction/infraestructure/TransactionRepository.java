package com.example.booknest.transaction.infraestructure;

import com.example.booknest.transaction.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Buscar transacciones por libro
    List<Transaction> findByBookIdBook(Long bookId);

    // Buscar transacciones por libro excluyendo una específica
    List<Transaction> findByBookIdBookAndIdTransactionNot(Long bookId, Long transactionId);

    // Eliminar transacciones por libro (excepto una específica)
    @Modifying
    @Transactional
    @Query("DELETE FROM Transaction t WHERE t.book.idBook = :bookId AND t.idTransaction != :transactionId")
    void deleteOtherTransactionsForBook(Long bookId, Long transactionId);

    // Buscar transacciones por comprador
    List<Transaction> findByBuyerId(Long buyerId);

    // Buscar transacciones por vendedor
    List<Transaction> findBySellerId(Long sellerId);
}