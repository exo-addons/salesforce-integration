package org.exoplatform.salesforce.integ.connector.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Salesforce Enumeration DTO for picklist ProductType__c
 */
public enum ProductTypeEnum {

    // Consulting
    CONSULTING("Consulting"),
    // Partnership
    PARTNERSHIP("Partnership"),
    // Redundant
    REDUNDANT("Redundant"),
    // Runtime/License
    RUNTIME_LICENSE("Runtime/License"),
    // Subscription
    SUBSCRIPTION("Subscription"),
    // Training
    TRAINING("Training");

    final String value;

    private ProductTypeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static ProductTypeEnum fromValue(String value) {
        for (ProductTypeEnum e : ProductTypeEnum.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException(value);
    }

}
