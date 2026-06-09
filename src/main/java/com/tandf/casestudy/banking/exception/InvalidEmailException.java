package com.tandf.casestudy.banking.exception;
public class InvalidEmailException extends Exception{
    public InvalidEmailException(String message) {
        super(message);
    }
}