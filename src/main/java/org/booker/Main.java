package org.booker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.booker.client.BookingHTTPClient;
import org.booker.client.BookingHTTPClientConfig;
import org.booker.client.BookingHTTPClientImpl;
import org.booker.model.AuthToken;
import org.booker.model.Credentials;

import java.io.IOException;
import java.net.http.HttpClient;

public class Main {

    private static final Logger Log = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException, InterruptedException {

        BookingHTTPClientConfig config = new BookingHTTPClientConfig("https://restful-booker.herokuapp.com");
        BookingHTTPClient client = new BookingHTTPClientImpl(HttpClient.newHttpClient(), config);

        Credentials creds = new Credentials("admin", "password123");
        AuthToken token = client.getToken(creds);

        Log.info("Token received: {}", token.token());

        /*
        Credentials creds = new Credentials("admin", "password123");

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        //GET
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://restful-booker.herokuapp.com/booking"))
                .GET()
                .build();

        HttpResponse<String> response
                = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
        System.out.println(response.body());


        //POST /auth
        BookingHTTPClient clientB = new BookingHTTPClientImpl(client, "https://restful-booker.herokuapp.com");

        System.out.println(clientB.getToken(creds));



         */
    }
}
