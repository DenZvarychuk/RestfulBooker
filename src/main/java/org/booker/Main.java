package org.booker;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
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
        BookerHTTPClient clientB = new BookerHTTPClientImpl(client, "https://restful-booker.herokuapp.com");

        System.out.println(clientB.getToken(creds));

    }
}
