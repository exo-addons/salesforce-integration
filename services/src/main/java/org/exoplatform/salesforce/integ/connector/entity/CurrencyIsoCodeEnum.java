package org.exoplatform.salesforce.integ.connector.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Salesforce Enumeration DTO for picklist CurrencyIsoCode
 */
public enum CurrencyIsoCodeEnum {

    // EUR
    EUR("EUR"),
    // TND
    TND("TND"),
    // USD
    USD("USD");

    final String value;

    private CurrencyIsoCodeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static CurrencyIsoCodeEnum fromValue(String value) {
        for (CurrencyIsoCodeEnum e : CurrencyIsoCodeEnum.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException(value);
    }

}
