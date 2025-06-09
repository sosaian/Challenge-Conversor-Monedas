package com.aluracursos.java.ratecli.models;

public record ConversionEntry (
        String timestamp,
        double conversionAmount,
        String baseCurrencyCode,
        String targetCurrencyCode,
        double convertedAmount) {
}
