package com.aluracursos.java.ratecli.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestHandler {
    public static String makeRequest(String apiURL) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL) // Habilita el seguimiento de redirecciones
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException e) {
            System.out.println("⛔ El formato de la URL no es el esperado.");
            throw e; // Lanza la excepción para que el llamador la maneje
        } catch (InterruptedException e) {
            System.out.println("⛔ Hubo una interrupción inesperada.");
            throw e; // Lanza la excepción para que el llamador la maneje
        }
    }
}
