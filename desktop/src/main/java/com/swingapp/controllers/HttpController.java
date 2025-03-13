package com.swingapp.controllers;

import com.swingapp.models.HttpResponseData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class HttpController {

    public HttpResponseData fetchDataFromApi(String route) {
        String apiUrl = "http://localhost:8080" + route;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Map<String, String> headers = new HashMap<>();
            response.headers().map().forEach((k, v) -> headers.put(k, String.join(", ", v)));
            return new HttpResponseData(response.statusCode(), headers, response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}