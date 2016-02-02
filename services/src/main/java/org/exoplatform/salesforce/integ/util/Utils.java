package org.exoplatform.salesforce.integ.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.social.service.rest.Util;

public class Utils {
	private static final String portalContainerName = "portal";
	private static IdentityManager identityManager = Util.getIdentityManager(portalContainerName);
	private static final String characterEncoding = "UTF-8";
    private static final String cipherTransformation = "AES/CBC/PKCS5Padding";
    private static final String aesEncryptionAlgorithm = "AES";
	
	
	public static boolean iSOpportunitySpace(String spaceName){
		SpaceService spaceService = Util.getSpaceService(portalContainerName);
		Space space=spaceService.getSpaceByPrettyName(spaceName);
		if(space!=null){
			 Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, space.getPrettyName(), false);
			 if(spaceIdentity.getProfile().getProperty("oppID")!=null)
				 return true;
		}
		return false;
		
	}
	
	public static String getSpaceUrl(String spaceName){
		SpaceService spaceService = Util.getSpaceService(portalContainerName);
		Space space=spaceService.getSpaceByPrettyName(spaceName);
		StringBuffer baseSpaceURL = null;
		baseSpaceURL = new StringBuffer();
		 baseSpaceURL.append(PortalContainer.getCurrentPortalContainerName()+ "/g/:spaces:") ;
		String groupId = space.getGroupId();
		String permanentSpaceName = groupId.split("/")[2];
		if (permanentSpaceName.equals(space.getPrettyName())) {
			baseSpaceURL.append(permanentSpaceName);
			baseSpaceURL.append("/");
			baseSpaceURL.append(permanentSpaceName);
		} else {
			baseSpaceURL.append(space.getPrettyName());
			baseSpaceURL.append("/");
			baseSpaceURL.append(space.getPrettyName());
		}
		return baseSpaceURL.toString();
	}
	
	public static boolean hasCookies(Cookie[] cookies) {
		// Cookie[] cookies = request.getCookies();
		String accesstoken = null;
		String instance_url = null;
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie1 = cookies[i];

			if (cookie1.getName().equals("tk_ck_")) {

				accesstoken = cookie1.getValue();
			}

			if (cookie1.getName().equals("inst_ck_")) {

				instance_url = cookie1.getValue();
			}
		}
		if (accesstoken != null && instance_url != null)
			return true;
		return false;
	}
	
	
	public static String[] getCookies(Cookie[] cookies) {
		// Cookie[] cookies = request.getCookies();

		String accesstoken = null;
		String instance_url = null;
		String[] tks = { accesstoken, instance_url };
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie1 = cookies[i];

			if (cookie1.getName().equals("tk_ck_")) {

				accesstoken = cookie1.getValue();
			}

			if (cookie1.getName().equals("inst_ck_")) {

				instance_url = cookie1.getValue();
			}
		}
		return tks;

	}
	
	public static Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }

        return null;
    }
	
	
	public static String getOpportunityId(String spaceName){
		SpaceService spaceService = Util.getSpaceService(portalContainerName);
		Space space=spaceService.getSpaceByPrettyName(spaceName);
		if(space!=null){
			 Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, space.getPrettyName(), false);
			 if(spaceIdentity.getProfile().getProperty("oppID")!=null)
				 return spaceIdentity.getProfile().getProperty("oppID").toString();
		}
		return null;
		
	}
	
	//get the body data sent from SF post
    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

    
    //decrypts AES encrypted string from apex using Crypto.encryptWithManagedIV
    
    public static byte[] decryptBase64EncodedWithManagedIV(String encryptedText, String key) throws Exception {
        byte[] cipherText = Base64.decodeBase64(encryptedText.getBytes());
        byte[] keyBytes = Base64.decodeBase64(key.getBytes());
        return decryptWithManagedIV(cipherText, keyBytes);
    }

    public static byte[] decryptWithManagedIV(byte[] cipherText, byte[] key) throws Exception{
        byte[] initialVector = Arrays.copyOfRange(cipherText,0,16);
        byte[] trimmedCipherText = Arrays.copyOfRange(cipherText,16,cipherText.length); 
        return decrypt(trimmedCipherText, key, initialVector);
    }

    public static byte[] decrypt(byte[] cipherText, byte[] key, byte[] initialVector) throws Exception{
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
        cipherText = cipher.doFinal(cipherText);
        return cipherText;
    }   
	
	

}
