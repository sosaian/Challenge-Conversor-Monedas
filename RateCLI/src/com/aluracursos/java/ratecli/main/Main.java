package com.aluracursos.java.ratecli.main;

import com.aluracursos.java.ratecli.http.RequestHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Preparando todo para iniciar el programa...");

        // Carga de API KEY desde variables de entorno del SO
        String codesListURL =
                "https://v6.exchangerate-api.com/v6/"
                + System.getenv("RATECLI_API_KEY")
                + "/codes";

        // Solicitud de todas las divisas disponibles para operar
        String codesListResponse;

        try {
            codesListResponse = RequestHandler.makeRequest(codesListURL);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(codesListResponse);
    }
}
