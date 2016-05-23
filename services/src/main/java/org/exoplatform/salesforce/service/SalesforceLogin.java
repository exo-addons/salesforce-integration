package org.exoplatform.salesforce.service;

import com.force.api.ApiConfig;
import com.force.api.ForceApi;
import org.exoplatform.commons.utils.PropertyManager;
import org.exoplatform.salesforce.VariablesUtil;

/**
 * Created by Bechir on 18/05/16.
 */

public class SalesforceLogin implements VariablesUtil{
    private static final String username = PropertyManager.getProperty(SF_ADMIN_USERNAME);
    private static final String password = PropertyManager.getProperty(SF_ADMIN_PASSWORD);
    private static final String loginEndPoint = PropertyManager.getProperty(SF_INSTANCE_URL);

    public ForceApi loginToSalesforce()
    {
        if(username != null && password != null && loginEndPoint != null) {
            return new ForceApi(new ApiConfig()
                    .setLoginEndpoint(loginEndPoint)
                    .setUsername(username)
                    .setPassword(password));
        }
        return null;
    }

    public String getLoginEndPoint() {
        return loginEndPoint;
    }
}
