package com.sameh.onlinebookstore.exception;

public class NoUpdateFoundException extends RuntimeException{
    public NoUpdateFoundException() {
        super();
    }

    public NoUpdateFoundException(String message) {
        super(message);
    }
}
