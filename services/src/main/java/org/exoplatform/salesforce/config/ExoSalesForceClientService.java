package org.exoplatform.salesforce.config;

import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.salesforce.VariablesUtil;
import org.exoplatform.salesforce.integ.util.RequestKeysConstants;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.picocontainer.Startable;

import com.force.api.ApiConfig;
import com.force.api.ApiVersion;

public class ExoSalesForceClientService implements VariablesUtil {
	private static final Log log = ExoLogger.getLogger(ExoSalesForceClientService.class);
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	protected ApiConfig apiconfig = null;

	public ExoSalesForceClientService(InitParams initParams) {
		
		
		
		clientId =initParams.getValueParam("clientId").getValue(); 
				
		clientSecret =  initParams.getValueParam("clientSecret").getValue();
				
		redirectUri =initParams.getValueParam("redirectUri").getValue();
				
		
		
		if (clientId == null || clientId.length() == 0 || clientId.trim().equals("<<to be replaced>>")) {
			log.warn("Property 'clientId' needs to be provided. The value should be " +
                    "clientId of your Salesforce application");
			return;
        }

        if (clientSecret == null || clientSecret.length() == 0 || clientSecret.trim().equals("<<to be replaced>>")) {
        	log.warn("Property 'clientSecret' needs to be provided. The value should be " +
                    "clientSecret of your Salesforce application");
        	return;
        }
        
        if (redirectUri == null || redirectUri.length() == 0 || redirectUri.trim().equals("<<to be replaced>>")) {
        	log.warn("Property 'redirectUri' needs to be provided. The value should be " +
                    "redirectUri of your Salesforce application");
        	return;
        }
        

		try {
			
			ApiVersion apiVersion = ApiVersion.DEFAULT_VERSION;
			apiconfig = new ApiConfig().setClientId(clientId)
					.setClientSecret(clientSecret).setRedirectURI(redirectUri)
					.setLoginEndpoint(SF_INSTANCE_URL)
					.setApiVersion(apiVersion);
			log.info(" ================================= Sales force init API ================");
		} catch (Exception e) {
			log.info(" ================= ERR init salesforce api ================");
		}
	}
	
	public ApiConfig getClient(){
		  return apiconfig;
		}


}
