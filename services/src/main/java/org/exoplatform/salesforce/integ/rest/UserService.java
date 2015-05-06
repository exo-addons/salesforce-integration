package org.exoplatform.salesforce.integ.rest;

import java.util.HashMap;
import java.util.Map;

import org.exoplatform.salesforce.integ.connector.entity.UserConfig;

public class UserService {

	public static Map<String, UserConfig> userMap = new HashMap<String, UserConfig>();

	public static UserConfig isInto(String userid) throws Exception {

		UserConfig user = userMap.get(userid);
		// could return a NULL element
		return user;

	}

	public static boolean hasNewToken(String userid, String token)
			throws Exception {

		UserConfig user = userMap.get(userid);
		if (user != null && !user.getAccesstoken().equals(token))
			return true;
		return false;

	}

}
