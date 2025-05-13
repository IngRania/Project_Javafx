package tn.services;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class TraductionService {

    public String traduireTexte(String texteOriginal, String sourceLangue, String cibleLangue) throws IOException {
        System.out.println("Début de la traduction:");
        System.out.println("Texte original: " + texteOriginal);
        System.out.println("Langue source: " + sourceLangue);
        System.out.println("Langue cible: " + cibleLangue);

        // Using MyMemory Translation API
        String encodedText = URLEncoder.encode(texteOriginal, StandardCharsets.UTF_8.toString());
        String apiUrl = String.format("https://api.mymemory.translated.net/get?q=%s&langpair=%s|%s",
                encodedText, sourceLangue, cibleLangue);

        System.out.println("URL de l'API: " + apiUrl);

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setConnectTimeout(5000); // 5 seconds
        conn.setReadTimeout(10000);   // 10 seconds

        int responseCode = conn.getResponseCode();
        System.out.println("Code de réponse HTTP: " + responseCode);

        if (responseCode != HttpURLConnection.HTTP_OK) {
            // Read error stream if available
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    errorResponse.append(line);
                }
                System.out.println("Réponse d'erreur: " + errorResponse.toString());
            }
            throw new IOException("Erreur HTTP " + responseCode);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            String json = response.toString();
            System.out.println("Réponse complète: " + json);

            // Parse the JSON response using JSONObject
            JSONObject jsonResponse = new JSONObject(json);
            JSONObject responseData = jsonResponse.getJSONObject("responseData");
            String translatedText = responseData.getString("translatedText");
            
            // Handle Arabic text specifically
            if (cibleLangue.equals("ar")) {
                // Convert Unicode escape sequences to actual Arabic characters
                StringBuilder arabicText = new StringBuilder();
                for (int i = 0; i < translatedText.length(); i++) {
                    if (translatedText.charAt(i) == '\\' && i + 1 < translatedText.length() && translatedText.charAt(i + 1) == 'u') {
                        // Found a Unicode escape sequence
                        String hex = translatedText.substring(i + 2, i + 6);
                        try {
                            int unicode = Integer.parseInt(hex, 16);
                            arabicText.append((char) unicode);
                            i += 5; // Skip the processed escape sequence
                        } catch (NumberFormatException e) {
                            arabicText.append(translatedText.charAt(i));
                        }
                    } else {
                        arabicText.append(translatedText.charAt(i));
                    }
                }
                translatedText = arabicText.toString();
                
                // Add RTL mark at the beginning
                translatedText = "\u202B" + translatedText;
            }

            System.out.println("Texte traduit: " + translatedText);
            return translatedText;
        } catch (Exception e) {
            System.out.println("Erreur lors du parsing de la réponse: " + e.getMessage());
            throw new IOException("Erreur lors du parsing de la réponse: " + e.getMessage());
        } finally {
            conn.disconnect();
        }
    }
}