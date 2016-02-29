package org.exoplatform.salesforce.integ.connector.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Salesforce Enumeration DTO for picklist ForecastCategoryName
 */
public enum ForecastCategoryNameEnum {

    // Best Case
    BEST_CASE("Best Case"),
    // Closed
    CLOSED("Closed"),
    // Commit
    COMMIT("Commit"),
    // Omitted
    OMITTED("Omitted"),
    // Pipeline
    PIPELINE("Pipeline");

    final String value;

    private ForecastCategoryNameEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static ForecastCategoryNameEnum fromValue(String value) {
        for (ForecastCategoryNameEnum e : ForecastCategoryNameEnum.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException(value);
    }

}
