package com.tandf.casestudy.exception;

/*
 * Custom checked exception thrown when the wallet does not have
 * enough balance to complete a bill payment or a transfer.
 *
 * Example: balance = ₹500, user tries to pay a ₹1000 bill.
 */
public class InsufficientBalanceException extends Exception {

    // Required because Exception implements Serializable.
    private static final long serialVersionUID = 1L;

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
