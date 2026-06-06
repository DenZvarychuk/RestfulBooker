package org.booker;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class BookerHTTPClientImpl implements BookerHTTPClient{

    private final HttpClient client;
    private final String host;

    public BookerHTTPClientImpl(HttpClient client, String host) {
        this.client = client;
        this.host = host;
    }

    @Override
    public String getToken(Credentials credentials) {
        try {

            String requestBody = "{\n" +
                    "    \"username\" : \"" + credentials.getUsername() + "\",\n" +
                    "    \"password\" : \"" + credentials.getPassword() + "\"\n" +
                    "}";

            HttpRequest authRequest = HttpRequest.newBuilder()
                    .uri(URI.create(host + "/auth"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // ecafc8bdd74b940

            HttpResponse<String> authResponse
                    = client.send(authRequest, HttpResponse.BodyHandlers.ofString());

            return authResponse.body();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to authenticate with Booker API", e);
        }


    }

    @Override
    public List<BookId> getBookList() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<BookId> getBookList(BookFilter filter) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public BookResponse book(BookRequest request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
