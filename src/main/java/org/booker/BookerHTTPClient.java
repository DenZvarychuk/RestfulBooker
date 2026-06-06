package org.booker;

import java.util.List;

public interface BookerHTTPClient {
    String getToken(Credentials credentials);
    List<BookId> getBookList();
    List<BookId> getBookList(BookFilter filter);
    BookResponse book(BookRequest request);
    public record BookId(Integer bookId) {}
}
