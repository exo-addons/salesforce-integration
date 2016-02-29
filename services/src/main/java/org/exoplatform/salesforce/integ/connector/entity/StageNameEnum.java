package org.exoplatform.salesforce.integ.connector.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author dev.zaouiahmed@gmail.com
 *
 */
public enum StageNameEnum {

    // Cancelled Cloud
    CANCELLED_CLOUD("Cancelled Cloud"),
    // Cancelled Community
    CANCELLED_COMMUNITY("Cancelled Community"),
    // Champion
    CHAMPION("Champion"),
    // Closed Lost
    CLOSED_LOST("Closed Lost"),
    // Closed Won
    CLOSED_WON("Closed Won"),
    // Duplicate
    DUPLICATE("Duplicate"),
    // Evaluation
    EVALUATION("Evaluation"),
    // Goal
    GOAL("Goal"),
    // Negotiation
    NEGOTIATION("Negotiation"),
    // No Decision
    NO_DECISION("No Decision"),
    // Walk away
    WALK_AWAY("Walk away");

    final String value;

    private StageNameEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static StageNameEnum fromValue(String value) {
        for (StageNameEnum e : StageNameEnum.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException(value);
    }

}
