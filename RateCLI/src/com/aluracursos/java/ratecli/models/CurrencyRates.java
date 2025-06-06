package com.aluracursos.java.ratecli.models;

public record CurrencyRates(String code, Double rate) {
    @Override
    public String toString() {
        // Usando ExchangeRate API, "code" es provisto acorde al código de tres letras del ISO 4217
        return code + " - " + rate;
    }
}
