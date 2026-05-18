package com.tandf.casestudy.exception;

/*
 * Custom checked exception thrown when a wallet operation would
 * push the balance beyond the allowed maximum (₹50,000) or when
 * a transfer amount exceeds the per-transaction cap (₹20,000).
 */
public class WalletLimitExceededException extends Exception {

    // Required because Exception implements Serializable.
    private static final long serialVersionUID = 1L;

    public WalletLimitExceededException(String message) {
        super(message);
    }
}
