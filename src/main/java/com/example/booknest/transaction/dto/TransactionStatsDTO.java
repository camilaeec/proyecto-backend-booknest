package com.example.booknest.transaction.dto;

public record TransactionStatsDTO(
        long totalTransactions,
        long pendingTransactions,
        long completedTransactions,
        long rejectedTransactions
) {}