package com.tandf.casestudy.banking;

import java.io.IOException;

/*
 * Custom CHECKED exception for the File Logging module.
 * Thrown by FileLogger when writing to the log file fails for any
 * IO reason other than the file being missing.
 */
public class LogIOException extends IOException {

    public LogIOException(String message) {
        super(message);
    }

    public LogIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
