package org.exoplatform.salesforce.integ.connector.storage.api;

import org.exoplatform.salesforce.integ.connector.entity.ConfigurationInfo;

public interface ConfigurationInfoStorage {
	public abstract void saveConfigurationInfo(String clientId,
			String clientSecret, String redirectUri) throws Exception;

	public abstract ConfigurationInfo getConfigurationInfo() throws Exception;

}


