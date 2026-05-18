package com.tandf.casestudy.exception;

/*
 * Custom checked exception thrown when a user supplies an amount
 * that is zero or negative for any wallet operation
 * (add money, pay bill, or transfer).
 *
 * Extends Exception (not RuntimeException) so that the compiler
 * forces callers to handle / declare it -- this is good practice
 * for "business rule" validations the user must be made aware of.
 */
public class InvalidAmountException extends Exception {

    // Required because Exception implements Serializable.
    // A fixed value keeps the class compatible across JVM versions.
    private static final long serialVersionUID = 1L;

    // Constructor that accepts a meaningful message and forwards
    // it to the parent Exception class.
    public InvalidAmountException(String message) {
        super(message);
    }
}
