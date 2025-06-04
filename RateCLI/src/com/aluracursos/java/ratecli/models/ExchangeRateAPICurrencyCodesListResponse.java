package com.aluracursos.java.ratecli.models;

import java.util.List;

public record ExchangeRateAPICurrencyCodesListResponse (List<List<String>> supported_codes) {
/*
    Listado de atributos recibidos por ExchangeRate API a la fecha de este commit.
    De momento solo necesito supported_codes, dejando los demás comentados.

    private String result;
    private String documentation;
    private String terms_of_use;
 */
}
