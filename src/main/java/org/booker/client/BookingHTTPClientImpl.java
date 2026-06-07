package org.booker.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.booker.Main;
import org.booker.model.AuthToken;
import org.booker.model.Credentials;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BookingHTTPClientImpl implements BookingHTTPClient {

    private static final Logger Log = LogManager.getLogger(BookingHTTPClientImpl.class);

    private final HttpClient client;
    private final BookingHTTPClientConfig config;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BookingHTTPClientImpl(HttpClient client, BookingHTTPClientConfig config) {
        this.client = client;
        this.config = config;
    }

    @Override
    public AuthToken getToken(Credentials credentials) {
        try {

            String requestBody = objectMapper.writeValueAsString(credentials);

            HttpRequest authRequest = HttpRequest.newBuilder()
                    .uri(URI.create(config.getHost() + "/auth"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> authResponse
                    = client.send(authRequest, HttpResponse.BodyHandlers.ofString());

            System.out.println("response: " + authResponse.body());

            return objectMapper.readValue(authResponse.body(), AuthToken.class);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to authenticate with Booker API", e);
        }


    }
}
