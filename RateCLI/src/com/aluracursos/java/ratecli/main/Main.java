package com.aluracursos.java.ratecli.main;

import com.aluracursos.java.ratecli.http.RequestHandler;
import com.aluracursos.java.ratecli.io.CurrencyCodesDeserializer;
import com.aluracursos.java.ratecli.models.Currency;
import com.aluracursos.java.ratecli.models.CurrencyExchangeRateListResult;

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
        System.out.println("La idea de este programa es hacer conversiones del tipo: \"1 USD a ARS\""
                +" por lo que voy a ir pidiéndote ingresar cada valor de esta conversión.");

        Scanner scanner = new Scanner(System.in);
        Double conversionAmount;
        String baseCurrencyCode;
        String targetCurrencyCode;

        do {
            // Solicitar el monto de la divisa a convertir
            conversionAmount = promptForAmount(scanner, codesListParsedResponse);

            if (conversionAmount == null) {
                System.out.println("👋🏿 Gracias por usar RateCLI, cerrando programa...");
                return;
            }

            // Solicitar la divisa base de la conversión.
            baseCurrencyCode = promptForBaseCurrencyCode(scanner, codesListParsedResponse);

            if (baseCurrencyCode == null) {
                System.out.println("👋🏿 Gracias por usar RateCLI, cerrando programa...");
                return;
            }

            // Solicitar la divisa final de la conversión.
            targetCurrencyCode = promptForTargetCurrencyCode(scanner, codesListParsedResponse);

            if (targetCurrencyCode == null) {
                System.out.println("👋🏿 Gracias por usar RateCLI, cerrando programa...");
                return;
            }

            // Carga de API KEY desde variables de entorno del SO
            String currencyExchangeRateListURL =
                    "https://v6.exchangerate-api.com/v6/"
                            + System.getenv("RATECLI_API_KEY")
                            + "/latest/"
                            + baseCurrencyCode;

            // Solicitud de todas las divisas disponibles para operar
            String currencyExchangeRateListResponse;

            try {
                currencyExchangeRateListResponse = RequestHandler.makeRequest(currencyExchangeRateListURL);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Conversión de la respuesta recibida (JSON) a un objeto que contiene el timestamp y el HashMap
            CurrencyExchangeRateListResult currencyExchangeRateListParsedResponse = CurrencyCodesDeserializer.parseCurrencyExchangeRateListResponse(currencyExchangeRateListResponse);

            if (currencyExchangeRateListParsedResponse.currencyRates().isEmpty()) {
                System.out.println("️⚠️ Ha ocurrido un error cargando la información. Intente de nuevo más tarde.");
                return;
            }

            String conversionText = + conversionAmount + " " + baseCurrencyCode + " = "
                    + currencyExchangeRateListParsedResponse.currencyRates().get(targetCurrencyCode).rate() * conversionAmount
                    + " " + targetCurrencyCode;

            System.out.println("La conversión elegida es de: " + conversionText);

            System.out.println("\n" + "*".repeat(30) + "\n");
            System.out.print("Presione la tecla Enter para continuar...");
            scanner.nextLine();
            System.out.println("\n" + "*".repeat(30) + "\n");
            System.out.println("Por favor, ingresa el código de divisa desde la que quieras hacer conversiones.");
        } while(true);
    }

    public static Double promptForAmount(Scanner scanner, HashMap<String, Currency> codesListParsedResponse) {
        boolean invalidUserResponse = true;
        String userResponse;
        Double conversionAmount = null;

        System.out.println("\n" + "*".repeat(30));
        System.out.println("\nIngresa el monto a convertir. ");
        System.out.println("\nEscribe \"A\" para listar todas las divisas disponibles.");
        System.out.println("\nEscribe \"Q\" para salir.");
        System.out.println("\n" + "*".repeat(30));

        do {
            System.out.print("\n🔢 Monto a convertir: ");
            userResponse = scanner.nextLine().trim().toUpperCase();

            if (userResponse.equals("Q")) {
                return null;
            } else if (userResponse.equals("A")) {
                System.out.println("\nListado de divisas disponibles:\n");
                codesListParsedResponse.forEach(
                        (key, value) -> System.out.println(key + " - " + value.name())
                );
                System.out.println("\n" + "*".repeat(30) + "\n");
            } else {
                try {
                    conversionAmount = Double.valueOf(userResponse);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Por favor ingrese un número válido.");
                }
                invalidUserResponse = conversionAmount == null;
            }
        } while (invalidUserResponse);

        return conversionAmount;
    }

    public static String promptForBaseCurrencyCode(Scanner scanner, HashMap<String, Currency> codesListParsedResponse) {
        boolean invalidUserResponse = true;
        String userResponse = "0";

        System.out.println("\n" + "*".repeat(30));
        System.out.println("\nAhora ingresa el código de divisa desde la que quieras hacer conversiones.");
        System.out.println("\nEscribe \"A\" para listar todas las divisas disponibles.");
        System.out.println("\nEscribe \"Q\" para salir.");
        System.out.println("\n" + "*".repeat(30));

        do {
            try {
                System.out.print("\n💵 Divisa a comparar: ");
                userResponse = scanner.nextLine().trim().toUpperCase();

                if (userResponse.equals("Q")) return null;

                if (userResponse.equals("A")) {
                    System.out.println("\nListado de divisas disponibles:\n");
                    codesListParsedResponse.forEach(
                            (key, value) -> System.out.println(key + " - " + value.name())
                    );
                    System.out.println("\nEscribe \"A\" para listar todas las divisas disponibles.");
                    System.out.println("\nEscribe \"Q\" para salir.");
                    System.out.println("\n" + "*".repeat(30));

                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Código inválido. " +
                        "Por favor, ingresa un código de divisa (por ejemplo, ARS).");
            }

            invalidUserResponse = codesListParsedResponse.get(userResponse) == null;

            if (invalidUserResponse)
                System.out.println("⚠️ El valor ingresado no es válido. Debe ser un código del listado.");
        } while (invalidUserResponse);

        return userResponse;
    }

    public static String promptForTargetCurrencyCode(Scanner scanner, HashMap<String, Currency> codesListParsedResponse) {
        boolean invalidUserResponse = true;
        String userResponse = "0";

        System.out.println("\n" + "*".repeat(30));
        System.out.println("\nAhora ingresa el código de divisa desde a la que quieras cambiar.");
        System.out.println("\nEscribe \"A\" para listar todas las divisas disponibles.");
        System.out.println("\nEscribe \"Q\" para salir.");
        System.out.println("\n" + "*".repeat(30));

        do {
            try {
                System.out.print("\n💵 Divisa a recibir: ");
                userResponse = scanner.nextLine().trim().toUpperCase();

                if (userResponse.equals("Q")) return null;

                if (userResponse.equals("A")) {
                    System.out.println("\nListado de divisas disponibles:\n");
                    codesListParsedResponse.forEach(
                            (key, value) -> System.out.println(key + " - " + value.name())
                    );
                    System.out.println("\nEscribe \"A\" para listar todas las divisas disponibles.");
                    System.out.println("\nEscribe \"Q\" para salir.");
                    System.out.println("\n" + "*".repeat(30));

                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Código inválido. " +
                        "Por favor, ingresa un código de divisa (por ejemplo, ARS).");
            }

            invalidUserResponse = codesListParsedResponse.get(userResponse) == null;

            if (invalidUserResponse)
                System.out.println("⚠️ El valor ingresado no es válido. Debe ser un código del listado.");
        } while (invalidUserResponse);

        return userResponse;
    }
}
