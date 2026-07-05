package org.booker.exception;

public class BookerApiException extends RuntimeException {

    private final int statusCode;
    private final String responseBody;

    public BookerApiException(int statusCode, String responseBody) {
        super("Booker API returned error: " + statusCode);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    @Override
    public String getMessage() {
        return "Booker API returned error: " + statusCode + " - " + responseBody;
    }
}
