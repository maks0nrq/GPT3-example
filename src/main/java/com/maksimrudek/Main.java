package com.maksimrudek;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String API_KEY = "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";

    public static void main(String[] args) throws IOException {
        String prompt = "Enter your prompt here.";

        URL url = new URL("https://api.openai.com/v1/engines/davinci-codex/completions");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setDoOutput(true);

        String json = "{\"prompt\": \"" + prompt + "\", \"max_tokens\": 100}";
        OutputStream os = conn.getOutputStream();
        os.write(json.getBytes());
        os.flush();

        InputStream is = conn.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        StringBuilder responseBuilder = new StringBuilder();

        while ((bytesRead = is.read(buffer)) != -1) {
            responseBuilder.append(new String(buffer, 0, bytesRead));
        }

        String response = responseBuilder.toString();
        List<String> textList = new ArrayList<String>();

        int startIndex = response.indexOf("\"text\": \"") + 9;
        int endIndex = response.indexOf("\"", startIndex);

        while (startIndex > 8 && endIndex > startIndex) {
            String text = response.substring(startIndex, endIndex);
            textList.add(text);

            startIndex = response.indexOf("\"text\": \"", endIndex) + 9;
            endIndex = response.indexOf("\"", startIndex);
        }

        String responseText = String.join("", textList);
        System.out.println(responseText);
    }
}


//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