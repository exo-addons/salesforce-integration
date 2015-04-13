package org.exoplatform.salesforce.integ.util;

public enum ResourcePath
{
  TOKEN("/services/oauth2/token"), 
  REVOKE("/services/oauth2/revoke"), 
  AUTHORIZE("/services/oauth2/authorize");

  private final String path;

  private ResourcePath(String path) {
    this.path = path;
  }

  public String getPath() {
    return this.path;
  }
}
