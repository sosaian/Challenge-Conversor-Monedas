package com.aluracursos.java.ratecli.models;

import java.util.List;

public class ExchangeRateAPICurrencyCodesListResponse {
    /**
     * Listado de atributos recibidos por ExchangeRate API a la fecha de este commit.
     * De momento solo necesito supported_codes, dejando los demás comentados.<p>
     * {@code private String result;}<p>
     * {@code private String documentation;}<p>
     * {@code private String terms_of_use;}
     */
    private List<List<String>> supported_codes;

    public List<List<String>> getSupported_codes() {
        return supported_codes;
    }
}
