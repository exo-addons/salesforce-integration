package org.exoplatform.salesforce.integ.connector.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;


/**
 *  * based on generated DTO objects using camel-salesforce maven plugin 
 * @author dev.zaouiahmed@gmail.com 
 */
public enum OriginEnum
{
    H("H"),
    // H
    C("C");

  final String value;

  private OriginEnum(String value) {
    this.value = value;
  }

  @JsonValue
  public String value() {
    return this.value;
  }

  @JsonCreator
  public static OriginEnum fromValue(String value) {
    for (OriginEnum e : values()) {
      if (e.value.equals(value)) {
        return e;
      }
    }
    throw new IllegalArgumentException(value);
  }
}
