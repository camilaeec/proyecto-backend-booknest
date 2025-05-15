package com.example.booknest.transaction.application;

import com.example.booknest.transaction.domain.Transaction;
import com.example.booknest.transaction.domain.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/money")
    public ResponseEntity<Transaction> createMoneyTransaction(
            @RequestParam Long buyerId,
            @RequestParam Long bookId,
            @RequestParam Integer offeredPrice) {
        return ResponseEntity.ok(transactionService.createMoneyTransaction(buyerId, bookId, offeredPrice));
    }

    @PostMapping("/exchange")
    public ResponseEntity<Transaction> createExchangeTransaction(
            @RequestParam Long buyerId,
            @RequestParam Long bookIdWanted,
            @RequestParam Long bookIdOffered) {
        return ResponseEntity.ok(transactionService.createExchangeTransaction(buyerId, bookIdWanted, bookIdOffered));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/acceptance")
    public ResponseEntity<Transaction> updateAcceptance(
            @PathVariable Long id,
            @RequestParam Long sellerId,
            @RequestParam Boolean accepted) {
        return ResponseEntity.ok(transactionService.updateTransactionAcceptance(id, sellerId, accepted));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id,
            @RequestParam Long sellerId) {
        transactionService.deleteTransaction(id, sellerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Transaction>> getTransactionsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBook(bookId));
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Transaction>> getTransactionsByBuyer(@PathVariable Long buyerId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBuyer(buyerId));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Transaction>> getTransactionsBySeller(@PathVariable Long sellerId) {
        return ResponseEntity.ok(transactionService.getTransactionsBySeller(sellerId));
    }
}
