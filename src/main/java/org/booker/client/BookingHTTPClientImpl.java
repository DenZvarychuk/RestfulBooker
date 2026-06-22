package org.booker.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.booker.exception.BookerApiException;
import org.booker.exception.BookerClientException;
import org.booker.model.AuthToken;
import org.booker.model.BookingFilter;
import org.booker.model.BookingId;
import org.booker.model.Credentials;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class BookingHTTPClientImpl implements BookingHTTPClient {

    private static final Logger Log = LogManager.getLogger(BookingHTTPClientImpl.class);
    private static final String AUTH_ENDPOINT = "/auth";
    private static final String BOOKING_ENDPOINT = "/booking";
    private static final int SUCCESS = 200;

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
                    .uri(URI.create(config.getHost() + AUTH_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> authResponse
                    = client.send(authRequest, HttpResponse.BodyHandlers.ofString());

            if (authResponse.statusCode() != SUCCESS) {
                Log.error("Authentication failed with status: {}", authResponse.statusCode());
                throw new BookerApiException(authResponse.statusCode(), authResponse.body());
            }

            if (blankOrEmpty(authResponse)) {
                Log.warn("Authentication failed with status {}: {}", authResponse.statusCode(), authResponse.body());
                throw new BookerApiException(authResponse.statusCode(), authResponse.body());
            } else {
                Log.info("Successfully authenticated");
                return objectMapper.readValue(authResponse.body(), AuthToken.class);
            }

        } catch (IOException | InterruptedException e) {
            Log.error("Failed to authenticate with Booker API due to error: ", e);
            throw new BookerClientException("Failed to authenticate with Booker API", e);
        }

    }

    @Override
    public List<BookingId> getBookingList() {
        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getHost() + BOOKING_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != SUCCESS) {
                Log.warn("Failed to retrieve booking list with status {}: {}", response.statusCode(), response.body());
                throw new BookerApiException(response.statusCode(), response.body());
            }

            if (blankOrEmpty(response)) {
                Log.warn("Received empty response from Booker API");
                return List.of();
            }

            Log.info("Received booking list with {} entries", response.body().length());
            return objectMapper.readValue(response.body(), new TypeReference<List<BookingId>>() {});

        } catch (IOException | InterruptedException e) {
            Log.error("Failed to retrieve booking list due to error: ", e);
            throw new BookerClientException("Failed to retrieve booking list", e);
        }
    }

    public List<BookingId> getBookingList(BookingFilter filter) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getHost() + BOOKING_ENDPOINT + "?" + filter.toQueryString()))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != SUCCESS) {
                Log.warn("Failed to retrieve booking list with status {}: {}", response.statusCode(), response.body());
                throw new BookerApiException(response.statusCode(), response.body());
            }

            if (blankOrEmpty(response)) {
                Log.warn("Received empty response from Booker API");
                return List.of();
            }

            Log.info("Received booking list with {} entries", response.body().length());
            return objectMapper.readValue(response.body(), new TypeReference<List<BookingId>>() {});

        } catch (IOException | InterruptedException e) {
            Log.error("Failed to retrieve booking list due to error: ", e);
            throw new BookerClientException("Failed to retrieve booking list", e);
        }
    }



    private boolean blankOrEmpty(HttpResponse<String> response) {
        return response.body() == null || response.body().isBlank() || response.body().isEmpty();
    }
}
