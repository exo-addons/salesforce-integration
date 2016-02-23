package org.exoplatform.salesforce.integ.connector.servlet;

import com.force.api.ApiConfig;
import com.force.api.ApiSession;
import com.force.api.ApiVersion;
import com.force.api.ForceApi;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.exoplatform.commons.utils.PropertyManager;
import org.exoplatform.salesforce.VariablesUtil;
import org.exoplatform.salesforce.config.ApiProvider;
import org.exoplatform.salesforce.integ.connector.entity.UserConfig;
import org.exoplatform.salesforce.integ.rest.UserService;
import org.exoplatform.salesforce.integ.util.RequestKeysConstants;
import org.exoplatform.salesforce.integ.util.ResourcePath;
import org.exoplatform.salesforce.integ.util.Utils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLEncoder;

public class OAuthServlet extends HttpServlet implements VariablesUtil{
	public class OauthToken {

	}

	private static final long serialVersionUID = 1L;

	public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	private static final Log LOG = ExoLogger.getLogger(OAuthServlet.class);
	// public static final String OPP_ID = "oppID";
	// public static final String INSTANCE_URL = "INSTANCE_URL";

	private String clientId = null;
	private String clientSecret = null;
	private String redirectUri = null;
	private String authUrl = null;
	private String tokenUrl = null;
	public static String environment;

	

	public void init() throws ServletException {
		try {
			clientId=PropertyManager.getProperty(CLIENT_ID);
			clientSecret =PropertyManager.getProperty(REDIRECT_URI) ;
			redirectUri=PropertyManager.getProperty(CLIENT_SECRET);

			environment = RequestKeysConstants.SF_PROD;

			authUrl = environment + ResourcePath.AUTHORIZE.getPath()
					+ "?response_type=code&client_id=" + clientId
					+ "&redirect_uri="
					+ URLEncoder.encode(redirectUri, "UTF-8");
			tokenUrl = environment + ResourcePath.TOKEN.getPath();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		clientId = PropertyManager.getProperty(CLIENT_ID);;
		redirectUri = PropertyManager.getProperty(REDIRECT_URI);
		clientSecret = PropertyManager.getProperty(CLIENT_SECRET);
		// use the session to store initial param to be re-used after callback
		// from SF
		String initialURI = (String) (request.getSession().getAttribute(
				"initialURI") != null ? request.getSession().getAttribute(
				"initialURI") : request.getParameter("initialURI"));
		String tempOppID = (String) (request.getSession().getAttribute("oppID") != null ? request
				.getSession().getAttribute("oppID") : request
				.getParameter("oppID"));
		if (request.getParameter("initialURI") != null)
			request.getSession().setAttribute("initialURI",
					request.getParameter("initialURI"));
		if (request.getParameter("oppID") != null)
			request.getSession().setAttribute("oppID",
					request.getParameter("oppID"));
		LOG.info("Begin OAuth");
		String accessToken = (String) request.getSession().getAttribute(
				ACCESS_TOKEN);
		String instanceUrl = (String) request.getSession().getAttribute(
				RequestKeysConstants.INSTANCE_URL);
		if (accessToken == null || instanceUrl == "") {
			if (request.getRequestURI().endsWith("oauth")) {
				// we need to send the user to authorize
				response.sendRedirect(authUrl + "&id=" + tempOppID);
				return;
			} else {
				LOG.info("Auth successful - got callback");
				// request.getSession().setAttribute(RequestKeysConstants.OPPORTUNITY_ID,tempOppID);

				String code = request.getParameter("code");

				HttpClient httpclient = new HttpClient();

				PostMethod post = new PostMethod(tokenUrl);

				post.addParameter(RequestKeysConstants.CODE_KEY, code);
				post.addParameter(RequestKeysConstants.GRANT_TYPE_KEY,
						RequestKeysConstants.AUTHORIZATION_CODE);
				post.addParameter(RequestKeysConstants.CLIENT_ID_KEY, clientId);
				post.addParameter(RequestKeysConstants.CLIENT_SECRET_KEY,
						clientSecret);
				post.addParameter(RequestKeysConstants.REDIRECT_URI_KEY,
						redirectUri);

				try {
					httpclient.executeMethod(post);

					try {

						InputStream rstream = post.getResponseBodyAsStream();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(rstream, "UTF-8"));
						String json = reader.readLine();
						JSONTokener tokener = new JSONTokener(json);
						JSONObject authResponse = new JSONObject(tokener);

						accessToken = authResponse
								.getString(RequestKeysConstants.ACCESS_TOKEN);

						instanceUrl = authResponse
								.getString(RequestKeysConstants.INSTANCE_URL);
					} catch (JSONException e) {
						e.printStackTrace();
						throw new ServletException(e);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (HttpException e) {
					e.printStackTrace();
					throw new ServletException(e);
				} finally {
					post.releaseConnection();
				}
			}

			request.getSession().setAttribute(ACCESS_TOKEN, accessToken);
			request.getSession().setAttribute(
					RequestKeysConstants.INSTANCE_URL, instanceUrl);
		}

		Cookie tk_cookie = Utils.getCookie(request, "tk_ck_");
		// update cookie with the new access token
		if (tk_cookie != null) {
			tk_cookie.setValue(accessToken);
			tk_cookie.setMaxAge(60 * 60);
			tk_cookie.setPath("/");
			response.addCookie(tk_cookie);
		} else {//
			tk_cookie = new Cookie("tk_ck_", accessToken);
			tk_cookie.setMaxAge(60 * 60); // 1 hour
			tk_cookie.setPath("/");
			response.addCookie(tk_cookie);
		}
		Cookie inst_cookie = Utils.getCookie(request, "inst_ck_");
		if (inst_cookie != null) {
			inst_cookie.setValue(instanceUrl);
			inst_cookie.setMaxAge(60 * 60); // 1 hour
			inst_cookie.setPath("/");
			response.addCookie(inst_cookie);
		} else {//
			inst_cookie = new Cookie("inst_ck_", instanceUrl);
			inst_cookie.setMaxAge(60 * 60); // 1 hour
			inst_cookie.setPath("/");
			response.addCookie(inst_cookie);
		}

		if (initialURI != null) {
			request.getSession().removeAttribute("initialURI");// clean the
																// session
			response.sendRedirect(initialURI);
			return; // <-- avoid IllegalStateException , ensures that no content
					// is adedd to the response further

		}
		if (tempOppID != null) {
			request.getSession().removeAttribute("oppID");
			response.sendRedirect("/salesforce-extension/opp" + "?oppID="
					+ tempOppID);
			return;
		} else
			response.sendRedirect("/portal");
		return;

	}

}
