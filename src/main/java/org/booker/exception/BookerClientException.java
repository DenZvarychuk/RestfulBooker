package org.booker.exception;

public class BookerClientException extends RuntimeException {
    public BookerClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookerClientException(String message) {
        super(message);
    }
}
