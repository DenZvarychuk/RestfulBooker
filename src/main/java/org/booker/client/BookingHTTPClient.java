package org.booker.client;

import org.booker.model.*;

import java.util.List;

public interface BookingHTTPClient {
    AuthToken getToken(Credentials credentials);
    List<BookingId> getBookingList();
    List<BookingId> getBookingList(BookingFilter filter);
    BookingResponse getBooking(BookingId id);
    // BookingResponse book(BookingRequest request);
}
