package org.booker.client;

import org.booker.model.AuthToken;
import org.booker.model.Credentials;

public interface BookingHTTPClient {
    AuthToken getToken(Credentials credentials);
    // List<BookId> getBookList();
    // List<BookId> getBookList(BookingFilter filter);
    // BookingResponse book(BookingRequest request);
    // public record BookId(Integer bookId) {}
}
