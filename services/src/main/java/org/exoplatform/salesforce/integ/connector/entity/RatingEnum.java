/*
 * Salesforce DTO generated by camel-salesforce-maven-plugin
 * Generated on: Tue May 17 15:09:19 CET 2016
 */
package org.exoplatform.salesforce.integ.connector.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Salesforce Enumeration DTO for picklist Rating
 */
public enum RatingEnum {

    // A
    A("A"),
    // B
    B("B"),
    // C
    C("C"),
    // Cold
    COLD("Cold"),
    // D
    D("D"),
    // E
    E("E"),
    // Hot
    HOT("Hot"),
    // Warm
    WARM("Warm");

    final String value;

    private RatingEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static RatingEnum fromValue(String value) {
        for (RatingEnum e : RatingEnum.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException(value);
    }

}