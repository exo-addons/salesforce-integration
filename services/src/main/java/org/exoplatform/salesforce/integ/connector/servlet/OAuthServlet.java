
package org.exoplatform.salesforce.integ.connector.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.exoplatform.salesforce.integ.util.RequestKeysConstants;
import org.exoplatform.salesforce.integ.util.ResourcePath;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.force.api.ApiConfig;
import com.force.api.ApiSession;
import com.force.api.ApiVersion;
import com.force.api.ForceApi;



public class OAuthServlet extends HttpServlet {
    public class OauthToken {
    	

	}

	private static final long serialVersionUID = 1L;

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final Log LOG = ExoLogger.getLogger(OAuthServlet.class);
    //public static final String OPP_ID = "oppID";
   //public static final String INSTANCE_URL = "INSTANCE_URL";

    private String clientId     = null;
    private String clientSecret = null;
    private String redirectUri  = null;
    private String authUrl      = null;
    private String tokenUrl     = null;
    private String tempOppID =null;
    private String refreshtoken  = null;
    public static String tk_url;
    public static String tk_tok;

    public void init() throws ServletException {

        String environment;

       /* will be set from portlet configuration*/
        
        
	    clientId = "3MVG9Rd3qC6oMalU6h9PyWK_07UERoFPmtwsBeSODWWKmNlNBBCBtpnnvHS3XD3dpIB7AXRLwl8iwMAPfzStu";
	    clientSecret ="9140283238048111603";
	    redirectUri = "https://zaoui:8443/salesforce-extension/oauth/_callback";
	    
	    
	  //  environment = "https://login.salesforce.com";
	    environment =RequestKeysConstants.SF_PROD;
	    //resourcePath
        
        try {
            authUrl = environment
                    +ResourcePath.AUTHORIZE.getPath() +"?response_type=code&client_id="
                    + clientId + "&redirect_uri="
                    + URLEncoder.encode(redirectUri, "UTF-8");

            // Nots: &scope=email,read_chatter,... would be added here for oauth scope

        } catch (UnsupportedEncodingException e) {
            throw new ServletException(e);
        }

        tokenUrl = environment + ResourcePath.TOKEN.getPath();
        //tokenUrl = environment + "/services/oauth2/token";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Cookie[] cookies = request.getCookies();
		
    	LOG.info("Begin OAuth");
        tempOppID = (tempOppID==null) ?   request.getParameter("oppID"):tempOppID;
        String accessToken = (String) request.getSession().getAttribute(ACCESS_TOKEN);
        //request.getSession().setAttribute(RequestKeysConstants.OPPORTUNITY_ID, request.getParameter("oppID"));

        if (accessToken == null) {

            String instanceUrl = null;
            //request.getSession().setAttribute(RequestKeysConstants.OPPORTUNITY_ID, request.getParameter("oppID"));
            
            /* get the request query oppID=00624000003M6ac */
            request.getQueryString();
            request.getParameter("oppID");

            if (request.getRequestURI().endsWith("oauth")) {
                // we need to send the user to authorize
            	request.getSession().setAttribute(RequestKeysConstants.OPPORTUNITY_ID,tempOppID);
            	
                response.sendRedirect(authUrl+"&id="+tempOppID);
                return;
            } else {
            	LOG.info("Auth successful - got callback");
               //request.getSession().setAttribute(RequestKeysConstants.OPPORTUNITY_ID,tempOppID);

                String code = request.getParameter("code");

                HttpClient httpclient = new HttpClient();

                PostMethod post = new PostMethod(tokenUrl);

                
                request.getSession().setAttribute(RequestKeysConstants.OPPORTUNITY_ID,tempOppID);
                
                post.addParameter(RequestKeysConstants.CODE_KEY, code);
                post.addParameter(RequestKeysConstants.GRANT_TYPE_KEY, RequestKeysConstants.AUTHORIZATION_CODE);
                post.addParameter(RequestKeysConstants.CLIENT_ID_KEY, clientId);
                post.addParameter(RequestKeysConstants.CLIENT_SECRET_KEY, clientSecret);
                post.addParameter(RequestKeysConstants.REDIRECT_URI_KEY, redirectUri);
               // post.addParameter(RequestKeysConstants.REDIRECT_URI_KEY, redirectUri);
                
                
                

                try {
                    httpclient.executeMethod(post);

                    try {

						InputStream rstream = post.getResponseBodyAsStream();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(rstream, "UTF-8"));
						String json = reader.readLine();
						JSONTokener tokener = new JSONTokener(json);
						JSONObject authResponse = new JSONObject(tokener);
                    	 


						accessToken = authResponse.getString(RequestKeysConstants.ACCESS_TOKEN);


						instanceUrl = authResponse.getString(RequestKeysConstants.INSTANCE_URL);
						boolean ist = false,tk=false;
						 for(int i = 0; i < cookies.length; i++) { 
					            Cookie cookie1 = cookies[i];
					            if (cookie1.getName().equals("tk_ck_")) {
					                
					            	ist = true;
					            }

							if (cookie1.getName().equals("inst_ck_")) {

								tk = true;
							}
					            
					        } 
						if (!tk) {
							Cookie tk_cookie = new Cookie("tk_ck_", accessToken);
							tk_cookie.setMaxAge(60 * 60); // 1 hour
							tk_cookie.setPath("/portal");
							response.addCookie(tk_cookie);
						}
						if (!ist) {
						Cookie inst_cookie = new Cookie("inst_ck_",instanceUrl);
						inst_cookie.setMaxAge(60*60); //1 hour
						inst_cookie.setPath("/portal");
						response.addCookie(inst_cookie);
						}
						

						initApi(request, accessToken, accessToken);
						
						
						//QueryResult<Account> iniResult =

						
						
						
						
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new ServletException(e);
                    }
                } catch (HttpException e) {
                    e.printStackTrace();
                    throw new ServletException(e);
                }
                finally {
                    post.releaseConnection();
                }
            }


            request.getSession().setAttribute(ACCESS_TOKEN, accessToken);
            //request.getSession().setAttribute(RequestKeysConstants.OPPORTUNITY_ID, request.getParameter("oppID"));


            request.getSession().setAttribute(RequestKeysConstants.INSTANCE_URL, instanceUrl);
        }
        request.getSession().setAttribute(RequestKeysConstants.OPPORTUNITY_ID, request.getParameter("oppID"));
        
        String s =tempOppID;
        tempOppID=null;
        response.sendRedirect("/salesforce-extension/opp"+"?id="+s);
    }

	public static ForceApi initApi(HttpServletRequest request, String accessToken,
			String instanceUrl) {
		ApiVersion apiVersion = ApiVersion.DEFAULT_VERSION;
		ApiConfig c = new ApiConfig()
		.setClientId("3MVG9Rd3qC6oMalU6h9PyWK_07UERoFPmtwsBeSODWWKmNlNBBCBtpnnvHS3XD3dpIB7AXRLwl8iwMAPfzStu")
		.setClientSecret("9140283238048111603")
		.setRedirectURI("https://zaoui:8443/salesforce-extension/oauth/_callback")
		.setLoginEndpoint("https://login.salesforce.com")

		.setApiVersion(apiVersion);
		
		
		ApiSession s =  new ApiSession(accessToken,instanceUrl);
		ForceApi api = new ForceApi(c,s);
		/*api.getIdentity();
		;
		Opportunity opp = api.getSObject("Opportunity", request.getParameter("oppID")).as(Opportunity.class);
		//ContentDocument doc = api.getSObject("Account", "00124000006Wqqe").as(ContentDocument.class);
		String query=
"SELECT OwnerId, SystemModstamp, Title, PublishStatus FROM ContentDocument";
		QueryResult<ContentDocument> doc = api.query(query, ContentDocument.class);
		doc.getTotalSize();
		*/
		return api;
	}
	
	public static ForceApi initApiFromCookies(String accessToken,String instanceUrl) {
		ApiVersion apiVersion = ApiVersion.DEFAULT_VERSION;
		ApiConfig c = new ApiConfig()
		.setClientId("3MVG9Rd3qC6oMalU6h9PyWK_07UERoFPmtwsBeSODWWKmNlNBBCBtpnnvHS3XD3dpIB7AXRLwl8iwMAPfzStu")
		.setClientSecret("9140283238048111603")
		.setRedirectURI("https://zaoui:8443/salesforce-extension/oauth/_callback")
		.setLoginEndpoint("https://login.salesforce.com")
		.setApiVersion(apiVersion);
		ApiSession s =  new ApiSession(accessToken,instanceUrl);
		ForceApi api = new ForceApi(c,s);
		return api;
	}
	
}
