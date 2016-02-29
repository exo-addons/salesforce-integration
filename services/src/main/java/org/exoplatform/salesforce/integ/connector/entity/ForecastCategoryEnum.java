package org.exoplatform.salesforce.integ.connector.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Salesforce Enumeration DTO for picklist ForecastCategory
 */
public enum ForecastCategoryEnum {

    // BestCase
    BESTCASE("BestCase"),
    // Closed
    CLOSED("Closed"),
    // Forecast
    FORECAST("Forecast"),
    // Omitted
    OMITTED("Omitted"),
    // Pipeline
    PIPELINE("Pipeline");

    final String value;

    private ForecastCategoryEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static ForecastCategoryEnum fromValue(String value) {
        for (ForecastCategoryEnum e : ForecastCategoryEnum.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException(value);
    }

}
