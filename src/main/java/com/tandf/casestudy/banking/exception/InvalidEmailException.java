package com.tandf.casestudy.banking;
public class InvalidEmailException extends Exception{
    public InvalidEmailException(String message) {
        super(message);
    }
}