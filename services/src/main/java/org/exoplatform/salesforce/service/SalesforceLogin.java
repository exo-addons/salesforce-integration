package org.exoplatform.salesforce.service;

import com.force.api.ApiConfig;
import com.force.api.ApiSession;
import com.force.api.ForceApi;
import com.force.api.QueryResult;
import org.apache.commons.codec.binary.Base64;
import org.exoplatform.commons.utils.PropertyManager;
import org.exoplatform.salesforce.VariablesUtil;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Bechir on 18/05/16.
 */

public class SalesforceLogin implements VariablesUtil{
    private static final String clientId = PropertyManager.getProperty(CLIENT_ID);
    private static final String username = PropertyManager.getProperty(SF_ADMIN_USERNAME);
    private static final String loginEndPoint = PropertyManager.getProperty(SF_INSTANCE_URL);
    private static final String privateKeyFile = PropertyManager.getProperty(SF_PRIVATE_KEY_File);

    private static final Logger LOG = Logger.getLogger(SalesforceLogin.class.getName());

    public ForceApi loginToSalesforce() {

        String header = "{\"alg\":\"RS256\"}";
        String claimTemplate = "'{'\"iss\": \"{0}\", \"sub\": \"{1}\", \"aud\": \"{2}\", \"exp\": \"{3}\"'}'";

        try {
            StringBuffer token = new StringBuffer();

            //Encode the JWT Header and add it to our string to sign
            token.append(Base64.encodeBase64URLSafeString(header.getBytes("UTF-8")));

            //Separate with a period
            token.append(".");

            //Create the JWT Claims Object
            String[] claimArray = new String[4];
            claimArray[0] = clientId;
            claimArray[1] = username;
            claimArray[2] = loginEndPoint;
            claimArray[3] = Long.toString((System.currentTimeMillis() / 1000) + 300);
            MessageFormat claims;
            claims = new MessageFormat(claimTemplate);
            String payload = claims.format(claimArray);

            //Add the encoded claims object
            token.append(Base64.encodeBase64URLSafeString(payload.getBytes("UTF-8")));

            // Load the private key (in PKCS#8 DER encoding).
            File keyFile = new File(privateKeyFile);
            byte[] encodedKey = new byte[(int) keyFile.length()];
            FileInputStream keyInputStream = new FileInputStream(keyFile);
            keyInputStream.read(encodedKey);
            keyInputStream.close();
            KeyFactory rSAKeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = rSAKeyFactory.generatePrivate(
                    new PKCS8EncodedKeySpec(encodedKey));

            //Sign the JWT Header + "." + JWT Claims Object
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(token.toString().getBytes("UTF-8"));
            String signedPayload = Base64.encodeBase64URLSafeString(signature.sign());

            //Separate with a period
            token.append(".");

            //Add the encoded signature
            token.append(signedPayload);

            String accessToken = null;
            String instanceURL = null;
            String response = getAuthToken(loginEndPoint+"/services/oauth2/token", "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion=" + token.toString());
            JSONObject json = new JSONObject(response.toString());
            try {
                accessToken = json.getString("access_token");
                instanceURL = json.getString("instance_url");
            } catch (JSONException jsonex) {
                return null;
            }
            ForceApi sfApi = null;
            if(accessToken != null && instanceURL != null) {
                sfApi = new ForceApi(new ApiSession(accessToken, instanceURL));
            }
            return sfApi;


        } catch (Exception e) {
            LOG.warning(e.getMessage());
            return null;
        }
    }

    static private String getAuthToken(String url, String urlParameters) throws Exception {

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject json = new JSONObject(response.toString());

        return json.toString();

    }

    public String getLoginEndPoint() {
        return loginEndPoint;
    }
}
