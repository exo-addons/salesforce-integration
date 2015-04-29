package org.exoplatform.salesforce.integ.connector.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author dev.zaouiahmed@gmail.com
 *
 */
public enum StageNameEnum {

//	enum for the different stage on opportunity Prospecting Qualification Needs Analysis Id. Decision Makers Perception Analysis Proposal/Price Quote Negotiation/Review
	
    // Prospecting
    PROSPECTING("Prospecting"),
    // Qualification
    QUALIFICATION("Qualification"),
    // Needs Analysis
    NEEDS_ANALYSIS("Needs Analysis"),
    // Value Proposition
    VALUE_PROPOSITION("Value Proposition"),
    // Id. Decision Makers
    ID__DECISION_MAKERS("Id. Decision Makers"),
    // Perception Analysis
    PERCEPTION_ANALYSIS("Perception Analysis"),
    // Proposal/Price Quote
    PROPOSAL_PRICE_QUOTE("Proposal/Price Quote"),
    // Negotiation/Review
    NEGOTIATION_REVIEW("Negotiation/Review"),
    // Closed Won
    CLOSED_WON("Closed Won"),
    // Closed Lost
    CLOSED_LOST("Closed Lost");

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
