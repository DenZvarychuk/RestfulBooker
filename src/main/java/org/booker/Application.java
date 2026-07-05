package org.booker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.booker.client.BookingHTTPClient;
import org.booker.client.BookingHTTPClientConfig;
import org.booker.client.BookingHTTPClientImpl;
import org.booker.exception.BookerApiException;
import org.booker.model.*;
import org.booker.repository.Keys;

import java.net.http.HttpClient;
import java.util.List;

public class Application {

    private static final Logger Log = LogManager.getLogger(Application.class);

    private final BookingHTTPClientConfig config = new BookingHTTPClientConfig("https://restful-booker.herokuapp.com");
    private final BookingHTTPClient client = new BookingHTTPClientImpl(HttpClient.newHttpClient(), config);
    private Credentials creds;
    private AuthToken token;

    public void run() throws InterruptedException {

        System.out.println(Keys.greetings);

        try {
            client.ping();
            System.out.println(Keys.connected);
        } catch (BookerApiException e) {
            System.out.println(Keys.unavailable);
        }

        System.out.println(Keys.select_option);

        creds = new Credentials("admin", "password123");

        // get Token
        token = client.getToken(creds);
        Log.debug("Token received: {}", token.token());
        Thread.sleep(500);

        // get full booking list
        List<BookingId> bookingList = client.getBookingList();
        Thread.sleep(500);

        // get list by params
        List<BookingId> paramBookingList = client.getBookingList(setFilter());

        Thread.sleep(500);

        BookingResponse booking = client.getBookingById(paramBookingList.getFirst());

        if (booking != null) {
            booking.print();
        } else {
            Log.warn("Booking not found");
        }

    }

    private static BookingFilter setFilter() {
        BookingFilter filter = new BookingFilter();
        filter.setFirstname("Eric");
        filter.setLastname("Jones");
        //filter.setCheckin(LocalDate.of(2017, 01, 31));
        //filter.setCheckout(LocalDate.of(2019, 12, 23));

        return filter;
    }

}
