package org.booker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.booker.client.BookingHTTPClient;
import org.booker.client.BookingHTTPClientConfig;
import org.booker.client.BookingHTTPClientImpl;
import org.booker.model.AuthToken;
import org.booker.model.BookingFilter;
import org.booker.model.BookingId;
import org.booker.model.Credentials;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.LocalDate;
import java.util.List;

public class Main {

    private static final Logger Log = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException, InterruptedException {

        BookingHTTPClientConfig config = new BookingHTTPClientConfig("https://restful-booker.herokuapp.com");
        BookingHTTPClient client = new BookingHTTPClientImpl(HttpClient.newHttpClient(), config);

        Credentials creds = new Credentials("admin", "password123");
        Log.info("Attempting to retrieve token for user: {}", creds.getUsername());
        AuthToken token = client.getToken(creds);
        Log.debug("Token received: {}", token.token());

        Log.info("Retrieving full booking list");
        List<BookingId> bookingList = client.getBookingList();

        Log.info("Retrieving booking list with filter");
        BookingFilter filter = new BookingFilter();
        filter.setFirstname("salldddy");
        filter.setLastname("brown");
        //filter.setCheckin(LocalDate.of(2014, 03, 13));
        //filter.setCheckout(LocalDate.of(2014, 05, 21));

        List<BookingId> paramBookingList = client.getBookingList(filter);


    }
}
