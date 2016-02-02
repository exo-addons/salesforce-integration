package org.exoplatform.salesforce.config;

import javax.servlet.http.HttpSession;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.salesforce.integ.connector.entity.Opportunity;
import org.exoplatform.salesforce.integ.connector.entity.UserConfig;
import org.exoplatform.salesforce.integ.rest.UserService;
import org.exoplatform.salesforce.integ.util.RequestKeysConstants;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import com.force.api.ApiConfig;
import com.force.api.ApiSession;
import com.force.api.ApiTokenException;
import com.force.api.ForceApi;

public class ApiProvider {

	static ExoSalesForceClientService exoSalesForceClientService = (ExoSalesForceClientService) ExoContainerContext
			.getCurrentContainer().getComponentInstanceOfType(
					ExoSalesForceClientService.class);
	private static final Log log = ExoLogger.getLogger(ApiProvider.class);

	public static ForceApi intApi(HttpSession session,String accessToken, String instanceUrl,
			String userID) {

		ApiConfig c = exoSalesForceClientService.getClient();

		ApiSession s = new ApiSession(accessToken, instanceUrl);

		ForceApi api = new ForceApi(c, s);

		if (isValidApiSession(api, userID)) {
			UserService.userMap.put(userID, new UserConfig(accessToken,
					instanceUrl));
			session.setAttribute(RequestKeysConstants.ACCESS_TOKEN, accessToken);
			session.setAttribute(RequestKeysConstants.INSTANCE_URL, instanceUrl);
			return api;
		}
		return null;

	}

	private static boolean isValidApiSession(ForceApi api, String userID) {
		try {
			// check if access token is valid sample ping first sobject
			api.query("select Name from Opportunity LIMIT 1", Opportunity.class);
		} catch (Exception e) {
			if (e instanceof ApiTokenException) {
				// [{"message":"Session expired or invalid","errorCode":"INVALID_SESSION_ID"}]
				log.warn("Session expired or invalid INVALID_SESSION_ID");
				UserService.userMap.remove(userID);
				return false;

			} else {
				//remove entry as not valid
				UserService.userMap.remove(userID);
				log.warn("Cannot Access Data");
				return false;

			}
		}
		return true;

	}

	public static boolean hasValidApiSession(String userID) {

		ApiConfig config = exoSalesForceClientService.getClient();
		String accesstoken = null;
		String endPoint = null;
		try {
			if (UserService.isInto(userID) != null) {
				accesstoken = UserService.userMap.get(userID).getAccesstoken();
				endPoint = UserService.userMap.get(userID).getInstanceUrl();
				ApiSession session = new ApiSession(accesstoken, endPoint);
				ForceApi api = new ForceApi(config, session);
				if (isValidApiSession(api, userID))
					return true;

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("an error occured", e.getCause());
			return false;
		}
		return false;

	}
}
