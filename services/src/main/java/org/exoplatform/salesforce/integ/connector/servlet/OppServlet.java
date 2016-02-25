package org.exoplatform.salesforce.integ.connector.servlet;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.exoplatform.salesforce.integ.util.RequestKeysConstants;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class OppServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	private static final Log LOG = ExoLogger.getLogger(OppServlet.class);
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		StringBuffer encodedUrl = new StringBuffer();
			String accessToken = (String) request.getSession().getAttribute(
					ACCESS_TOKEN);
			String instanceUrl = (String) request.getSession().getAttribute(
					RequestKeysConstants.INSTANCE_URL);
			
		
		String oppID = request.getParameter("oppID")!=null?request.getParameter("oppID"):(String) request.getSession().getAttribute("oppID");

		if (accessToken == null||instanceUrl==null) {
			return;
		}

		try {
		encodedUrl.append("/portal/private/rest/salesforce/create/"+oppID);
		response.sendRedirect(response.encodeRedirectURL(encodedUrl.toString()));
		return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage(), e);
		}
		
	}

	private String  getOpportunityName(String instanceUrl, String accessToken,
			String oppID, PrintWriter writer) throws ServletException, IOException {
		HttpClient httpclient = new HttpClient();
		GetMethod get = new GetMethod(instanceUrl+ "/services/data/v30.0/query");
		String name=null;

		// set the token in the header
		get.setRequestHeader("Authorization", "OAuth " + accessToken);

		// set the SOQL as a query param
		NameValuePair[] params = new NameValuePair[1];
           String query="select Name from Opportunity where id="+ "\'"+oppID+"\' LIMIT 1"; //tem.out.println("\'Hello\'");
          // Select Name from Opportunity where Id='00624000003M6ac'
		params[0] = new NameValuePair("q",
				query);
		get.setQueryString(params);//"SELECT Name, Id from Account LIMIT 100

		try {
			httpclient.executeMethod(get);
			if (get.getStatusCode() == HttpStatus.SC_OK) {
				try {
					
					StringWriter sw = new StringWriter();
					InputStream rstream = get.getResponseBodyAsStream();
					IOUtils.copy(rstream, sw, "UTF-8");
					String json = sw.toString();
					
					
					JSONObject response = new JSONObject(json);
					
					LOG.info("Query response: "
							+ response.toString(2));					
					JSONArray results = response.getJSONArray("records");
					 name =results.getJSONObject(0).getString("Name");
				} catch (JSONException e) {
					
					LOG.error(e.getMessage(), e);
					throw new ServletException(e);
				}
			}
		} finally {
			get.releaseConnection();
		}
		return name;
	}

	

}