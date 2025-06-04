package com.aluracursos.java.ratecli.io;

import com.aluracursos.java.ratecli.models.Currency;
import com.aluracursos.java.ratecli.models.ExchangeRateAPICurrencyCodesListResponse;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class CurrencyCodesDeserializer {
    public static HashMap<String, Currency> parseCurrencyCodeListResponse(String json) {
        Gson gson = new Gson();
        ExchangeRateAPICurrencyCodesListResponse parsedResponse = gson.fromJson(json, ExchangeRateAPICurrencyCodesListResponse.class);

        HashMap<String, Currency> currencyHashMap = new HashMap<>();
        for (List<String> codePair : parsedResponse.supported_codes()) {
            String code = codePair.get(0);
            String name = codePair.get(1);
            // Este hashmap está pensado para comparar acorde al código de la divisa.
            // Si el día de mañana Currency tiene más atributos, este código es más fácil de mantener.
            currencyHashMap.put(code, new Currency(code, name));
        }

        return currencyHashMap;
    }
}
