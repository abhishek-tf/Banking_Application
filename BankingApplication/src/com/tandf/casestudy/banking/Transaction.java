package com.tandf.casestudy.banking;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final String transactionId;
    private final String accountNumber;
    private final double amount;
    private final TransactionType transactionType;
    private final LocalDateTime timestamp;

    public Transaction(String accountNumber, double amount, TransactionType transactionType) {
        if (amount <= 0) {
            throw new InvalidAmountException("Transaction amount must be greater than zero: " + amount);
        }
        this.transactionId = UUID.randomUUID().toString();
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = LocalDateTime.now();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                ", transactionType=" + transactionType +
                ", timestamp=" + timestamp +
                '}';
}
}
