package org.exoplatform.salesforce.integ.connector.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * https://www.salesforce.com/developer/docs/api/Content/sforce_api_objects_contentdocumentlink.htm
 * based on generated DTO objects using camel-salesforce maven plugin 
 * @author dev.zaouiahmed@gmail.com 
 * API version 26.0 and later.
 * Visibility can have the following values.
 * AllUsers—The feed item is available to all users who have permission to see the feed item.
 * InternalUsers—The feed item is available to internal users only.
 * SharedUsers—The feed item is available to all users who can see the feed to which the feed item is posted. The SharedUsers value is available in API version 32.0 and later.
*/

public enum VisibilityEnum {

    // AllUsers
    ALLUSERS("AllUsers"),
    // InternalUsers
    INTERNALUSERS("InternalUsers"),
    // SharedUsers
    SHAREDUSERS("SharedUsers");

    final String value;

    private VisibilityEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static VisibilityEnum fromValue(String value) {
        for (VisibilityEnum e : VisibilityEnum.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException(value);
    }

}

