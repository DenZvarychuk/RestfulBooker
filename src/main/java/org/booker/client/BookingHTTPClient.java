package org.booker.client;

import org.booker.model.*;

import java.util.List;

public interface BookingHTTPClient {

    void ping();
    AuthToken getToken(Credentials credentials);
    List<BookingId> getBookingList();
    List<BookingId> getBookingList(BookingFilter filter);
    BookingResponse getBookingById(BookingId id);
    // BookingResponse book(BookingRequest request);


}
