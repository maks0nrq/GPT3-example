package com.maksimrudek;

import java.io.IOException;
import okhttp3.*;
import com.google.gson.*;

public class Main {

    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/engines/davinci-codex/completions";

    private static final OkHttpClient client = new OkHttpClient();

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws IOException {
        String prompt = "Hello, how are you?";
        int maxTokens = 50;
        double temperature = 0.7;
        String response = generateText(prompt, maxTokens, temperature);
        System.out.println(response);
    }

    private static String generateText(String prompt, int maxTokens, double temperature) throws IOException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("prompt", prompt);
        requestBody.addProperty("max_tokens", maxTokens);
        requestBody.addProperty("temperature", temperature);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(MediaType.parse("application/json"), gson.toJson(requestBody)))
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonArray choices = json.getAsJsonArray("choices");
        JsonObject firstChoice = choices.get(0).getAsJsonObject();
        String text = firstChoice.get("text").getAsString();

        return text;
    }
}
