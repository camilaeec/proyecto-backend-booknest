package com.example.booknest.transaction.application;

import com.example.booknest.transaction.domain.Transaction;
import com.example.booknest.transaction.domain.TransactionService;
import com.example.booknest.transaction.dto.TransactionRequestDTO;
import com.example.booknest.transaction.dto.TransactionResponseDTO;
import com.example.booknest.transaction.dto.TransactionStatsDTO;
import com.example.booknest.user.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PreAuthorize("hasRole('COMMON_USER')")
    @GetMapping("/stats")
    public ResponseEntity<TransactionStatsDTO> getTransactionStats() {
        return ResponseEntity.ok(transactionService.getTransactionStats());
    }

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PreAuthorize("hasRole('COMMON_USER')")
    @PostMapping("/exchange")
    public ResponseEntity<TransactionResponseDTO> createExchangeTransaction(
            @RequestBody TransactionRequestDTO transactionRequestDTO) {
        if (transactionRequestDTO.getDate() == null) {
            transactionRequestDTO.setDate(new Date());
        }
        transactionRequestDTO.setAccepted(null);

        return ResponseEntity.ok(
                transactionService.convertToResponseDTO(
                        transactionService.createExchangeTransaction(transactionRequestDTO)
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @PreAuthorize("hasRole('ADMIN') or @transactionService.isTransactionParticipant(authentication.name, #id)")
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PreAuthorize("@transactionService.isTransactionSeller(authentication.name, #id)")
    @PatchMapping("/{id}/acceptance")
    public ResponseEntity<TransactionResponseDTO> updateAcceptance(
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
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBook(bookId));
    }

    @PreAuthorize("hasRole('ADMIN') or #buyerId == @userService.getUserByEmail(authentication.name).id")
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByBuyer(@PathVariable Long buyerId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBuyer(buyerId));
    }

    @PreAuthorize("hasRole('ADMIN') or #sellerId == @userService.getUserByEmail(authentication.name).id")
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsBySeller(@PathVariable Long sellerId) {
        return ResponseEntity.ok(transactionService.getTransactionsBySeller(sellerId));
    }
}
