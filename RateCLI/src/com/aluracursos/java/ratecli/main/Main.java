package com.aluracursos.java.ratecli.main;

import com.aluracursos.java.ratecli.http.RequestHandler;
import com.aluracursos.java.ratecli.io.CurrencyCodesDeserializer;
import com.aluracursos.java.ratecli.models.Currency;

import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

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

        // Inicio del UX
        System.out.println("🛍️ Bienvenido a RateCLI!");
        System.out.println("Por favor, ingresa el código de divisa desde la que quieras hacer conversiones.");
        System.out.println("\n" + "*".repeat(30) + "\n");

        Scanner scanner = new Scanner(System.in);
        String userResponse;
        boolean invalidUserResponse;
        boolean firstLoop = true;
        boolean restartLoop;

        // Listar las divisas disponibles al usuario
        System.out.println("Listado de divisas disponibles:\n");
        codesListParsedResponse.forEach(
                (key, value) -> System.out.println(key + " - " + value.name())
        );

        do {
            userResponse = "0";
            invalidUserResponse = true;
            restartLoop = false;

            if (!firstLoop) System.out.println("\nEscribe 1 para listar todas las divisas disponibles.");
            System.out.println("\nEscribe 0 para salir.");
            System.out.println("\n" + "*".repeat(30));

            // Solicitar la divisa a referenciar para las conversiones
            while (invalidUserResponse) {
                try {
                    System.out.print("\n💵 Divisa a comparar: ");
                    userResponse = scanner.nextLine().trim().toUpperCase();

                    if (userResponse.equals("0")) {
                        System.out.println("👋🏿 Gracias por usar RateCLI, cerrando programa...");
                        return;
                    } else if (!firstLoop && userResponse.equals("1")) {
                        System.out.println("\nListado de divisas disponibles:\n");
                        codesListParsedResponse.forEach(
                                (key, value) -> System.out.println(key + " - " + value.name())
                        );
                        System.out.println("\nEscribe 1 para listar todas las divisas disponibles.");
                        System.out.println("\nEscribe 0 para salir.");
                        System.out.println("\n" + "*".repeat(30));
                        restartLoop = true;
                    } else if (codesListParsedResponse.get(userResponse) == null) {
                        System.out.println("⚠️ El valor ingresado no es válido. Debe ser un código del listado.");
                    } else {
                        invalidUserResponse = false; // Cambiar el estado a válido si el código es válido.
                    }
                } catch (InputMismatchException e) {
                    System.out.println("❌ Código inválido. " +
                            "Por favor, ingresa un código de divisa (por ejemplo, ARS).");
                }
            }

            if (restartLoop) continue;

            System.out.println("Genial! has elegido la divisa: "
                    + codesListParsedResponse.get(userResponse.toUpperCase()));
            firstLoop = false;

            System.out.println("\n" + "*".repeat(30) + "\n");
            System.out.print("Presione la tecla Enter para continuar...");
            scanner.nextLine();
            System.out.println("\n" + "*".repeat(30) + "\n");
            System.out.println("Por favor, ingresa el código de divisa desde la que quieras hacer conversiones.");
        } while(true);
    }
}
