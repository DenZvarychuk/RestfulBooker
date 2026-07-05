package org.booker.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.booker.exception.BookerApiException;
import org.booker.exception.BookerClientException;
import org.booker.model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.NoSuchElementException;

public class BookingHTTPClientImpl implements BookingHTTPClient {

    private static final Logger Log = LogManager.getLogger(BookingHTTPClientImpl.class);
    private static final String AUTH_ENDPOINT = "/auth";
    private static final String PING_ENDPOINT = "/ping";
    private static final String BOOKING_ENDPOINT = "/booking";
    private static final int SUCCESS = 200;
    private static final int CREATED = 201;

    private final HttpClient client;
    private final BookingHTTPClientConfig config;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BookingHTTPClientImpl(HttpClient client, BookingHTTPClientConfig config) {
        this.client = client;
        this.config = config;
    }

    @Override
    public void ping() {
        Log.info("Attempting to ping RestfullBooker");
        try {
            HttpRequest pingRequest = HttpRequest.newBuilder()
                    .uri(URI.create(config.getHost() + PING_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> pingResponse
                    = client.send(pingRequest, HttpResponse.BodyHandlers.ofString());

            if (pingResponse.statusCode() != CREATED) {
                Log.error("Ping failed with status: {}", pingResponse.statusCode());
                throw new BookerApiException(pingResponse.statusCode(), pingResponse.body());
            }

        }catch (IOException | InterruptedException e) {
            Log.error("Failed to ping with Booker API due to error: ", e);
            throw new BookerClientException("Failed to ping with Booker API", e);
        }

    }

    @Override
    public AuthToken getToken(Credentials credentials) {
        Log.info("Attempting to retrieve token for user: {}", credentials.getUsername());

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
        Log.info("Retrieving full booking list");

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getHost() + BOOKING_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            List<BookingId> bookingIds = objectMapper.readValue(response.body(), new TypeReference<List<BookingId>>() {
            });

            if (response.statusCode() != SUCCESS) {
                Log.warn("Failed to retrieve booking list with status {}: {}", response.statusCode(), response.body());
                throw new BookerApiException(response.statusCode(), response.body());
            }

            if (blankOrEmpty(response)) {
                Log.warn("Received empty response from Booker API");
                return List.of();
            }

            Log.info("Received booking list with {} entries", bookingIds.size());
            return bookingIds;

        } catch (IOException | InterruptedException e) {
            Log.error("Failed to retrieve booking list due to error: ", e);
            throw new BookerClientException("Failed to retrieve booking list", e);
        }
    }

    public List<BookingId> getBookingList(BookingFilter filter) {
        Log.info("Retrieving booking list with filter: {}", filter);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getHost() + BOOKING_ENDPOINT + "?" + filter.toQueryString()))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Log.info("Body: {}", response.body());

            List<BookingId> bookingIds = objectMapper.readValue(response.body(), new TypeReference<List<BookingId>>() {
            });

            if (response.statusCode() != SUCCESS) {
                Log.warn("Failed to retrieve booking list with status {}: {}", response.statusCode(), response.body());
                throw new BookerApiException(response.statusCode(), response.body());
            }

            if (blankOrEmpty(response)) {
                Log.warn("Received empty response from Booker API");
                return List.of();
            }

            Log.info("Received booking list with {} entries", bookingIds.size());
            return bookingIds;

        } catch (IOException | InterruptedException e) {
            Log.error("Failed to retrieve booking list due to error: ", e);
            throw new BookerClientException("Failed to retrieve booking list", e);
        }
    }

    @Override
    public BookingResponse getBookingById(BookingId id) {
        Log.info("Retrieving booking with id:{}", id.bookingId());

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getHost() + BOOKING_ENDPOINT + "/" + id.bookingId()))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != SUCCESS) {
                Log.warn("Failed to retrieve booking list with status {}: {}", response.statusCode(), response.body());
                throw new BookerApiException(response.statusCode(), response.body());
            }

            if (blankOrEmpty(response)) {
                Log.warn("Received empty response from Booker API");
                return null;
            }

            return objectMapper.readValue(response.body(), new TypeReference<BookingResponse>() {
            });

        } catch (IOException | InterruptedException e) {
            Log.error("Failed to retrieve booking list due to error: ", e);
            throw new BookerClientException("Failed to retrieve booking list", e);
        }
    }


    private boolean blankOrEmpty(HttpResponse<String> response) {
        return response.body() == null || response.body().isBlank() || response.body().isEmpty() || response.body().equals("[]");
    }
}
