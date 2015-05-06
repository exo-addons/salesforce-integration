/**
 * 
 */
package org.exoplatform.salesforce.integ.connector.entity;

/**
 * @author dev.zaouiahmed@gmail.com
 * 
 */
public class UserConfig {
	
	private String accesstoken;
	private String instanceUrl;
	public UserConfig(String accesstoken, String endPoint) {
		super();
		this.accesstoken = accesstoken;
		this.instanceUrl = endPoint;
	}
	public String getAccesstoken() {
		return accesstoken;
	}
	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
	public String getInstanceUrl() {
		return instanceUrl;
	}
	public void setInstanceUrl(String endPoint) {
		this.instanceUrl = endPoint;
	}
}