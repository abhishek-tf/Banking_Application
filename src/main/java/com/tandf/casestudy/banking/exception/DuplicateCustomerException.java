package com.tandf.casestudy.banking.exception;
public class DuplicateCustomerException extends Exception{
    public DuplicateCustomerException(String message) {
        super(message);
    }
}