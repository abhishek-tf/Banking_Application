package com.tandf.casestudy.banking.exception;

import java.io.FileNotFoundException;

/*
 * Custom CHECKED exception for the File Logging module.
 * Thrown by FileLogger when the log file path is missing or cannot
 * be created. Extends FileNotFoundException so callers may catch it
 * as either the custom type or the standard FileNotFoundException.
 */
public class LogFileNotFoundException extends FileNotFoundException {

    public LogFileNotFoundException(String message) {
        super(message);
    }
}
