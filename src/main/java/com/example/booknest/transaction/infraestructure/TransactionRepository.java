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
    Transaction findById(long id);

    List<Transaction> findByBook_IdBook(Long bookId);
    List<Transaction> findByBook_IdBookAndIdTransactionNot(Long bookId, Long transactionId);
    List<Transaction> findByBuyer_Id(Long buyerId);
    List<Transaction> findBySeller_Id(Long sellerId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Transaction t WHERE t.book.idBook = :bookId AND t.idTransaction != :transactionId")
    void deleteOtherTransactionsForBook(Long bookId, Long transactionId);
}