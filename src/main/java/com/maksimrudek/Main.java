package com.maksimrudek;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws URISyntaxException {
        String prompt = "Hello, how are you?";
        int maxTokens = 10;
        String apiKey = "<your-api-key>";
        String engineId = "<your-engine-id>";

        String requestBody = String.format("{\"prompt\": \"%s\", \"max_tokens\": %d, \"temperature\": 0.5}", prompt, maxTokens);

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("https://api.openai.com/v1/engines/%s/completions", engineId)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response != null && response.statusCode() < 400) {
            String responseBody = response.body();
            String[] completionParts = responseBody.split("\"text\":\"");
            if (completionParts.length > 1) {
                String generatedText = completionParts[1].split("\"")[0].replace("\\n", "");
                System.out.println(generatedText);
            } else {
                System.err.println(responseBody);
            }
        } else {
            System.err.println("Error: " + response);
        }
    }
}