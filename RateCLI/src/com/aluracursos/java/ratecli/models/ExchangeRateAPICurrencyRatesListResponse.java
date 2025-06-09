package com.aluracursos.java.ratecli.models;

import java.util.Map;

public record ExchangeRateAPICurrencyRatesListResponse(
        Map<String,Double> conversion_rates,
        String time_last_update_utc) {
/*
    Listado de atributos recibidos por ExchangeRate API a la fecha de este commit.
    De momento solo necesito conversion_rates y time_next_update_utc, dejando los demás comentados.

    private String result;
    private String documentation;
    private String terms_of_use;
    private long time_last_update_unix;
    private long time_next_update_unix;
    private String time_next_update_utc;
    private String base_code;
*/
}
