package com.tandf.casestudy.banking.model;

import java.util.Objects;

public final class TransactionType {
    public static final TransactionType DEPOSIT = new TransactionType("DEPOSIT");
    public static final TransactionType WITHDRAWAL = new TransactionType("WITHDRAWAL");
    public static final TransactionType TRANSFER = new TransactionType("TRANSFER");

    private final String name;

    private TransactionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionType)) return false;
        TransactionType that = (TransactionType) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static TransactionType fromString(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Transaction type name must not be null");
        }
        switch (name.toUpperCase()) {
            case "DEPOSIT":
                return DEPOSIT;
            case "WITHDRAWAL":
                return WITHDRAWAL;
            case "TRANSFER":
                return TRANSFER;
            default:
                throw new IllegalArgumentException("Unknown transaction type: " + name);
        }
    }
}
