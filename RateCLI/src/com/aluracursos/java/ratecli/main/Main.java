package com.aluracursos.java.ratecli.main;

import com.aluracursos.java.ratecli.http.RequestHandler;
import com.aluracursos.java.ratecli.io.CurrencyCodesDeserializer;
import com.aluracursos.java.ratecli.models.Currency;

import java.io.IOException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        System.out.println("Preparando todo para iniciar el programa...");

        // Carga de API KEY desde variables de entorno del SO
        String currencyCodesListURL =
                "https://v6.exchangerate-api.com/v6/"
                + System.getenv("RATECLI_API_KEY")
                + "/codes";

        // Solicitud de todas las divisas disponibles para operar
        String codesListResponse;

        try {
            codesListResponse = RequestHandler.makeRequest(currencyCodesListURL);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Conversión de la respuesta recibida (JSON) a un HashMap
        HashMap<String, Currency> codesListParsedResponse = CurrencyCodesDeserializer.parseCurrencyCodeListResponse(codesListResponse);

        if (codesListParsedResponse.isEmpty()) {
            System.out.println("️⚠️ Ha ocurrido un error cargando la información. Intente de nuevo más tarde.");
            return;
        }

        // Listar las divisas disponibles al usuario
        System.out.println("Listado de divisas disponibles:\n");
        codesListParsedResponse.forEach(
                (key, value) -> System.out.println(key + " - " + value.name())
        );
    }
}
