package com.aluracursos.java.ratecli.io;

import com.aluracursos.java.ratecli.models.Currency;
import com.aluracursos.java.ratecli.models.ExchangeRateAPICurrencyCodesListResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CurrencyCodesDeserializer {
    public static List<Currency> parseCurrencyCodeListResponse(String json) {
        Gson gson = new Gson();
        ExchangeRateAPICurrencyCodesListResponse response = gson.fromJson(json, ExchangeRateAPICurrencyCodesListResponse.class);

        List<Currency> currencyList = new ArrayList<>();
        for (List<String> codePair : response.getSupported_codes()) {
            String code = codePair.get(0);
            String name = codePair.get(1);
            currencyList.add(new Currency(code, name));
        }

        return currencyList;
    }
}
