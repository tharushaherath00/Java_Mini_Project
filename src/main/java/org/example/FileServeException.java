package org.example;

public class FileServeException extends Exception {
    public FileServeException(String message) {
        super(message);
    }

    public FileServeException(String message, Throwable cause) {
        super(message, cause);
    }
}
