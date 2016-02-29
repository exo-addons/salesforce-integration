public class HttpCallout {
 
// Pass in the endpoint to be used using the string url
   @future(callout=true)
   public static void getContent(String url) {
 
// Instantiate a new http object
    Http h = new Http();
 
// Instantiate a new HTTP request, specify the method (GET) as well as the endpoint
    HttpRequest req = new HttpRequest();
    req.setEndpoint(url);
    req.setTimeout(ConfigurationManager.CALLOUT_TIMEOUT);
   
    Blob key = EncodingUtil.base64Decode('mRMjHmlC1C+1L/Dkz8EJuw==');
    Blob cipherText = Crypto.encryptWithManagedIV('AES128', key, Blob.valueOf(ConfigurationManager.SID));
    String strToken = 'Basic ' +EncodingUtil.base64Encode(cipherText);
    req.setHeader('Authorization',strToken);
    req.setMethod('GET');
 

// Send the request
    h.send(req);
  }
 
}