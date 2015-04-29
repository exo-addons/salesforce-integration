package org.exoplatform.salesforce.integ.connector.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;


/**
 * @author dev.zaouiahmed@gmail.com
 *
 */

/*
 * https://www.salesforce.com/developer/docs/api/Content/sforce_api_objects_contentdocument.htm
 * P—The document is published to a public library and is visible to other users. Label is Public.
 * R—The document is published to a personal library and is not visible to other users. Label is Personal Library.
 * U—The document is not published because publishing was interrupted. Label is Upload Interrupted.
 * 
 * */

public enum PublishStatusEnum {

    // U
    U("U"),
    // P
    P("P"),
    // R
    R("R");

    final String value;

    private PublishStatusEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static PublishStatusEnum fromValue(String value) {
        for (PublishStatusEnum e : PublishStatusEnum.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException(value);
    }

}
