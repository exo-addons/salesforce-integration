package org.exoplatform.salesforce.integ.connector.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;



/*
 * Origin of the document. Valid values are:
 * S—This is a document located within Salesforce. Label is Salesforce.
 * E—This is a document located outside of Salesforce. Label is External
 * https://www.salesforce.com/developer/docs/api/Content/sforce_api_objects_contentdocument.htm
 * 
 */


/**
 *  * based on generated DTO objects using camel-salesforce maven plugin 
 * @author dev.zaouiahmed@gmail.com 
 */

public enum ContentLocationEnum
{
  S("S"), 

  E("E");

  final String value;

  private ContentLocationEnum(String value) {
    this.value = value;
  }

  @JsonValue
  public String value() {
    return this.value;
  }

  @JsonCreator
  public static ContentLocationEnum fromValue(String value) {
    for (ContentLocationEnum e : values()) {
      if (e.value.equals(value)) {
        return e;
      }
    }
    throw new IllegalArgumentException(value);
  }
}
