package com.tandf.casestudy.banking.exception;

import java.io.IOException;


public class LogIOException extends IOException {

    public LogIOException(String message) {
        super(message);
    }

    public LogIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
