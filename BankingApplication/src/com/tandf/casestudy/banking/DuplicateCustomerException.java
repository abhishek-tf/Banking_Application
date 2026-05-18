package com.tandf.casestudy.banking;
class DuplicateCustomerException extends Exception{
    public DuplicateCustomerException(String message) {
        super(message);
    }
}