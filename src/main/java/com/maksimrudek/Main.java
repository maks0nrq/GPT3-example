package com.maksimrudek;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        String prompt = "Hi, how are you doing today?";
        String response = getAnswer(prompt);
        System.out.println(response);
    }

    public static String getAnswer(String prompt) {
        try {
            URL url = new URL("https://api.openai.com/v1/engines/text-davinci-003/completions");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + "");
            connection.setDoOutput(true);

            String body = "{\"prompt\": \"" + prompt + "\", \"max_tokens\": 150, \"temperature\": 0.7}";

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            writer.write(body);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            int startIndex = response.indexOf("\"text\": \"") + 9;
            int endIndex = response.indexOf("\"", startIndex);
            String answer = response.substring(startIndex, endIndex);

            return answer;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}