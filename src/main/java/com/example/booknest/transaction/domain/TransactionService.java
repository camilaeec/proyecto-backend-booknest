package com.example.booknest.transaction.domain;

import com.example.booknest.book.domain.Book;
import com.example.booknest.book.domain.BookService;
import com.example.booknest.transaction.infraestructure.TransactionRepository;
import com.example.booknest.user.domain.User;
import com.example.booknest.user.domain.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final BookService bookService;

    public TransactionService(TransactionRepository transactionRepository,
                              UserService userService,
                              BookService bookService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    @Transactional
    public Transaction createMoneyTransaction(Long buyerId, Long bookId, Integer offeredPrice) {
        /*
        User buyer = userService.getUserById(buyerId)
                .orElseThrow(() -> new RuntimeException("Comprador no encontrado"));
        Book book = bookService.getBookById(bookId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        if (book.getUser() == null) {
            throw new RuntimeException("El libro no tiene un vendedor asociado");
        }
        */

        Transaction transaction = new Transaction();
        //transaction.setBuyer(buyer);
        // transaction.setSeller(book.getUser()); // Comentado hasta integrar User
        //transaction.setBook(book);
        transaction.setCost(offeredPrice);
        transaction.setDate(new Date());
        transaction.setAccepted(null);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction createExchangeTransaction(Long buyerId, Long bookIdWanted, Long bookIdOffered) {
        /*
        User buyer = userService.getUserById(buyerId)
                .orElseThrow(() -> new RuntimeException("Comprador no encontrado"));
        Book bookWanted = bookService.getBookById(bookIdWanted)
                .orElseThrow(() -> new RuntimeException("Libro deseado no encontrado"));
        Book bookOffered = bookService.getBookById(bookIdOffered)
                .orElseThrow(() -> new RuntimeException("Libro ofrecido no encontrado"));

        if (!bookOffered.getUser().getIdUser().equals(buyerId)) {
            throw new RuntimeException("El libro ofrecido debe pertenecer al comprador");
        }
        */

        Transaction transaction = new Transaction();
        //transaction.setBuyer(buyer);
        // transaction.setSeller(bookWanted.getUser());
        //transaction.setBook(bookWanted);
        transaction.setCost(null);
        transaction.setDate(new Date());
        transaction.setAccepted(null);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Transactional
    public Transaction updateTransactionAcceptance(Long transactionId, Long sellerId, Boolean accepted) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

        if (!transaction.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Solo el vendedor puede aceptar/rechazar la transacción");
        }

        transaction.setAccepted(accepted);

        if (Boolean.TRUE.equals(accepted)) {
            // Eliminar otras transacciones para el mismo libro
            transactionRepository.deleteOtherTransactionsForBook(
                    transaction.getBook().getIdBook(),
                    transactionId
            );
        }

        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long transactionId, Long sellerId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

        if (!transaction.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Solo el vendedor puede eliminar la transacción");
        }

        if (transaction.getAccepted() != Boolean.FALSE) {
            throw new RuntimeException("Solo se pueden eliminar transacciones rechazadas explícitamente");
        }

        transactionRepository.delete(transaction);
    }

    public List<Transaction> getTransactionsByBook(Long bookId) {
        return transactionRepository.findByBookIdBook(bookId);
    }

    public List<Transaction> getTransactionsByBuyer(Long buyerId) {
        return transactionRepository.findByBuyerId(buyerId);
    }

    public List<Transaction> getTransactionsBySeller(Long sellerId) {
        return transactionRepository.findBySellerId(sellerId);
    }
}