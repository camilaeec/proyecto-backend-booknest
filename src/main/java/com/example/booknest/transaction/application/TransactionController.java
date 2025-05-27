package com.example.booknest.transaction.application;

import com.example.booknest.transaction.domain.Transaction;
import com.example.booknest.transaction.domain.TransactionService;
import com.example.booknest.user.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /*
    @PreAuthorize("hasRole('COMMON_USER')")
    @PostMapping("/money")
    public ResponseEntity<Transaction> createMoneyTransaction(
            @RequestParam Long bookId,
            @RequestParam Integer offeredPrice) {
        return ResponseEntity.ok(transactionService.createMoneyTransaction(bookId, offeredPrice));
    }
    */
    

    @PreAuthorize("hasRole('COMMON_USER')")
    @PostMapping("/exchange")
    public ResponseEntity<Transaction> createExchangeTransaction(
            @RequestParam Long bookIdWanted,
            @RequestParam Long bookIdOffered) {
        return ResponseEntity.ok(transactionService.createExchangeTransaction(bookIdWanted, bookIdOffered));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @PreAuthorize("hasRole('ADMIN') or @transactionService.isTransactionParticipant(authentication.name, #id)")
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @PreAuthorize("@transactionService.isTransactionSeller(authentication.name, #id)")
    @PatchMapping("/{id}/acceptance")
    public ResponseEntity<Transaction> updateAcceptance(
            @PathVariable Long id,
            @RequestParam Boolean accepted) {
        return ResponseEntity.ok(transactionService.updateTransactionAcceptance(id, accepted));
    }

    @PreAuthorize("@transactionService.isTransactionSeller(authentication.name, #id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or @transactionService.isBookOwner(authentication.name, #bookId)")
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Transaction>> getTransactionsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBook(bookId));
    }

    @PreAuthorize("hasRole('ADMIN') or #buyerId == @transactionService.getCurrentUser().id")
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Transaction>> getTransactionsByBuyer(@PathVariable Long buyerId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBuyer(buyerId));
    }

    @PreAuthorize("hasRole('ADMIN') or #sellerId == @transactionService.getCurrentUser().id")
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Transaction>> getTransactionsBySeller(@PathVariable Long sellerId) {
        return ResponseEntity.ok(transactionService.getTransactionsBySeller(sellerId));
    }
}
