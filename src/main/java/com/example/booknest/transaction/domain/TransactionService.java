package com.example.booknest.transaction.domain;

import com.example.booknest.book.domain.Book;
import com.example.booknest.book.domain.BookService;
import com.example.booknest.transaction.infraestructure.TransactionRepository;
import com.example.booknest.user.domain.User;
import com.example.booknest.user.domain.UserService;
import com.example.booknest.user.dto.UseResponseForOtherUsersDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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

    public UseResponseForOtherUsersDTO getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByEmail(username);
    }

    @Transactional
    public Transaction createMoneyTransaction(Long bookId, Integer offeredPrice) {
        UseResponseForOtherUsersDTO buyerDTO = getCurrentUser();
        Book book = bookService.getById(bookId);

        if (book == null) {
            throw new RuntimeException("Libro no encontrado");
        }

        if (book.getUser() == null) {
            throw new RuntimeException("El libro no tiene un vendedor asociado");
        }

        User buyer = new User();
        buyer.setId(buyerDTO.getId());

        Transaction transaction = new Transaction();
        transaction.setBuyer(buyer);
        transaction.setSeller(book.getUser());
        transaction.setBook(book);
        transaction.setCost(offeredPrice);
        transaction.setDate(new Date());
        transaction.setAccepted(null);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction createExchangeTransaction(Long bookIdWanted, Long bookIdOffered) {
        UseResponseForOtherUsersDTO buyerDTO = getCurrentUser();
        Book bookWanted = bookService.getById(bookIdWanted);
        Book bookOffered = bookService.getById(bookIdOffered);

        if (bookWanted == null || bookOffered == null) {
            throw new RuntimeException("Libro no encontrado");
        }

        if (!bookOffered.getUser().getId().equals(buyerDTO.getId())) {
            throw new RuntimeException("El libro ofrecido debe pertenecer al comprador");
        }

        User buyer = new User();
        buyer.setId(buyerDTO.getId());

        Transaction transaction = new Transaction();
        transaction.setBuyer(buyer);
        transaction.setSeller(bookWanted.getUser());
        transaction.setBook(bookWanted);
        transaction.setCost(null);
        transaction.setDate(new Date());
        transaction.setAccepted(null);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(long id) {
        Transaction transaction = transactionRepository.findById(id);
        if (transaction == null) {
            throw new RuntimeException("Transacción no encontrada");
        }
        return transaction;
    }
    @Transactional
    public Transaction updateTransactionAcceptance(Long transactionId, Boolean accepted) {
        UseResponseForOtherUsersDTO sellerDTO = getCurrentUser();
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transacción no encontrada"));;

        if (transaction == null) {
            throw new RuntimeException("Transacción no encontrada");
        }

        if (transaction.getSeller() == null || !transaction.getSeller().getId().equals(sellerDTO.getId())) {
            throw new RuntimeException("Solo el vendedor puede aceptar/rechazar la transacción");
        }

        transaction.setAccepted(accepted);

        if (accepted) {
            transactionRepository.deleteOtherTransactionsForBook(
                    transaction.getBook().getIdBook(),
                    transactionId
            );
        }

        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long transactionId) {
        UseResponseForOtherUsersDTO sellerDTO = getCurrentUser();
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

        if (transaction == null) {
            throw new RuntimeException("Transacción no encontrada");
        }

        if (transaction.getSeller() == null || !transaction.getSeller().getId().equals(sellerDTO.getId())) {
            throw new RuntimeException("Solo el vendedor puede eliminar la transacción");
        }

        if (transaction.getAccepted() == null || transaction.getAccepted()) {
            throw new RuntimeException("Solo se pueden eliminar transacciones rechazadas explícitamente");
        }

        transactionRepository.delete(transaction);
    }

    public List<Transaction> getTransactionsByBook(Long bookId) {
        return transactionRepository.findByBookId(bookId);
    }

    public List<Transaction> getTransactionsByBuyer(Long buyerId) {
        return transactionRepository.findByBuyerId(buyerId);
    }

    public List<Transaction> getTransactionsBySeller(Long sellerId) {
        return transactionRepository.findBySellerId(sellerId);
    }

    public boolean isTransactionParticipant(String username, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
        if (transaction == null) return false;

        UseResponseForOtherUsersDTO user = userService.getUserByEmail(username);

        return (transaction.getBuyer() != null && transaction.getBuyer().getId().equals(user.getId())) ||
                (transaction.getSeller() != null && transaction.getSeller().getId().equals(user.getId()));
    }

    public boolean isTransactionSeller(String username, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
        if (transaction == null) return false;

        UseResponseForOtherUsersDTO user = userService.getUserByEmail(username);

        return transaction.getSeller() != null && transaction.getSeller().getId().equals(user.getId());
    }

    public boolean isBookOwner(String username, Long bookId) {
        Book book = bookService.getById(bookId);
        if (book == null) return false;

        UseResponseForOtherUsersDTO user = userService.getUserByEmail(username);

        return book.getUser() != null && book.getUser().getId().equals(user.getId());
    }
    }
}