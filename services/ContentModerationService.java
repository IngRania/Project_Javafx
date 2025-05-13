package tn.services;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ContentModerationService {
    // Using a multilingual model for toxic content detection
    private static final String HUGGINGFACE_API_URL = "https://api-inference.huggingface.co/models/facebook/roberta-hate-speech-dynabench-r4-target";
    private static final String API_KEY = "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";
    
    // Common profanity and offensive words in multiple languages
    private static final Set<String> PROFANITY_WORDS = new HashSet<>(Arrays.asList(
        // English
        "fuck", "shit", "ass", "bitch", "damn", "hell", "crap", "dick", "pussy", "cock",
        // French
        "merde", "putain", "con", "salope", "enculé", "nique", "bite", "chatte", "couille", "cul"
        // Arabic (transliterated)

    ));

    // Arabic offensive words (in Arabic script)
    private static final Set<String> ARABIC_PROFANITY = new HashSet<>(Arrays.asList(

    ));

    public boolean containsInappropriateContent(String text) throws IOException {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        System.out.println("Analyzing text for inappropriate content: " + text);

        // Check for explicit profanity in all languages
        String[] words = text.toLowerCase().split("\\s+");
        for (String word : words) {
            // Check transliterated profanity
            if (PROFANITY_WORDS.contains(word)) {
                System.out.println("Found explicit profanity: " + word);
                return true;
            }
            // Check Arabic profanity
            if (ARABIC_PROFANITY.contains(word)) {
                System.out.println("Found Arabic profanity: " + word);
                return true;
            }
        }

        // Check for Arabic text
        boolean containsArabic = text.matches(".*[\\u0600-\\u06FF].*");
        if (containsArabic) {
            System.out.println("Text contains Arabic characters");

        }

        // Then check with AI model
        JSONObject requestBody = new JSONObject();
        requestBody.put("inputs", text);
        // Specify language for better detection
        if (containsArabic) {
            requestBody.put("language", "ar");
        } else if (text.matches(".*[éèêëàâçîïôöûüùÿ].*")) {
            requestBody.put("language", "fr");
        } else {
            requestBody.put("language", "en");
        }

        URL url = new URL(HUGGINGFACE_API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    errorResponse.append(line);
                }
                System.out.println("Error response: " + errorResponse.toString());
            }
            throw new IOException("API request failed with response code: " + responseCode);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            System.out.println("API Response: " + response.toString());

            JSONArray jsonArray = new JSONArray(response.toString());
            JSONArray firstResult = jsonArray.getJSONArray(0);
            
            double hateScore = 0.0;
            for (int i = 0; i < firstResult.length(); i++) {
                JSONObject result = firstResult.getJSONObject(i);
                if (result.getString("label").equals("hate")) {
                    hateScore = result.getDouble("score");
                    break;
                }
            }
            
            System.out.println("Hate speech score: " + hateScore);
            
            // Adjust threshold based on language
            double threshold = containsArabic ? 0.2 : 0.3; // Lower threshold for Arabic
            boolean isInappropriate = hateScore > threshold;
            System.out.println("Content is inappropriate: " + isInappropriate);
            
            return isInappropriate;
        }
    }

    public JSONObject getDetailedAnalysis(String text) throws IOException {
        if (text == null || text.trim().isEmpty()) {
            return new JSONObject().put("error", "Empty text");
        }

        JSONObject result = new JSONObject();
        
        // Check for explicit profanity
        String[] words = text.toLowerCase().split("\\s+");
        Set<String> foundProfanity = new HashSet<>();
        for (String word : words) {
            if (PROFANITY_WORDS.contains(word)) {
                foundProfanity.add(word);
            }
            if (ARABIC_PROFANITY.contains(word)) {
                foundProfanity.add(word);
            }
        }
        if (!foundProfanity.isEmpty()) {
            result.put("explicit_profanity", true);
            result.put("profanity_words", new JSONArray(foundProfanity));
        }

        // Detect language
        boolean containsArabic = text.matches(".*[\\u0600-\\u06FF].*");
        boolean containsFrench = text.matches(".*[éèêëàâçîïôöûüùÿ].*");
        String detectedLanguage = containsArabic ? "Arabic" : (containsFrench ? "French" : "English");
        result.put("detected_language", detectedLanguage);

        // Get AI analysis
        JSONObject requestBody = new JSONObject();
        requestBody.put("inputs", text);
        requestBody.put("language", containsArabic ? "ar" : (containsFrench ? "fr" : "en"));

        URL url = new URL(HUGGINGFACE_API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            JSONArray jsonArray = new JSONArray(response.toString());
            JSONArray firstResult = jsonArray.getJSONArray(0);
            
            for (int i = 0; i < firstResult.length(); i++) {
                JSONObject score = firstResult.getJSONObject(i);
                result.put(score.getString("label"), score.getDouble("score"));
            }
        }
        
        return result;
    }
} 