package com.aluracursos.java.ratecli.models;

import java.util.HashMap;

public record CurrencyExchangeRateListResult (
        String timestampUTC,
        HashMap<String, CurrencyRates> currencyRates){
}
