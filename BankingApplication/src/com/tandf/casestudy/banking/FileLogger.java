package com.tandf.casestudy.banking;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class FileLogger {

    private static final String DEFAULT_LOG_PATH = "transaction_errors.log";

    private final String logFilePath;

    public FileLogger() {
        this.logFilePath = DEFAULT_LOG_PATH;
    }

    public FileLogger(String logFilePath) {
        if (logFilePath == null || logFilePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Log file path cannot be null or blank");
        }
        this.logFilePath = logFilePath;
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    
    public void logError(String message) throws IOException {
        if (message == null) {
            throw new NullPointerException("Log message cannot be null");
        }
        PrintWriter pw = null;
        try {
            
            pw = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
            String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String entry = "[ERROR] " + timestamp + " - " + message;
            pw.println(entry);
            System.out.println("Logged -> " + entry);
        } catch (FileNotFoundException fnf) {
            
            throw new LogFileNotFoundException(
                "Log file not found or cannot be created: " + logFilePath);
        } catch (IOException io) {
            throw new LogIOException(
                "Failed to write log entry to " + logFilePath + " : " + io.getMessage(), io);
        } finally {
            
            if (pw != null) {
                pw.close();
            }
        }
    }

    
    public void logError(Throwable ex) throws IOException {
        if (ex == null) {
            throw new NullPointerException("Exception cannot be null");
        }
        logError(ex.getClass().getSimpleName() + ": " + ex.getMessage());
    }
}
