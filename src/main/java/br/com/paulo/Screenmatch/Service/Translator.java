package br.com.paulo.Screenmatch.Service;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import io.github.cdimascio.dotenv.Dotenv;

public class Translator {
    private static final String API_URL = "https://api-free.deepl.com/v2/translate";


    static Dotenv dotenv = Dotenv.load();
//    String apiKey = dotenv.get("API_KEY");
    private static final String AUTH_KEY = dotenv.get("API_KEY");


    public String traduzir(String texto, String idiomaAlvo) {
        try {
            String urlParams = "auth_key=" + URLEncoder.encode(AUTH_KEY, "UTF-8") +
                    "&text=" + URLEncoder.encode(texto, "UTF-8") +
                    "&target_lang=" + URLEncoder.encode(idiomaAlvo, "UTF-8");

            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Envia os dados
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = urlParams.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Lê a resposta
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = br.readLine()) != null) {
                resposta.append(linha.trim());
            }
            JSONObject json = new JSONObject(resposta.toString());
            JSONArray Atraduzir = json.getJSONArray("translations");
            String traducao = Atraduzir.getJSONObject(0).getString("text");

            return traducao;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao traduzir com DeepL: " + e.getMessage(), e);
        }
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
