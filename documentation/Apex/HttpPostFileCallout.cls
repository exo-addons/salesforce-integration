public class HttpPostFileCallout {
 

   @future(callout=true)
   public static void getContent(String url,Blob fileBody) {
 

    Http h = new Http();
 

    HttpRequest req = new HttpRequest();
    req.setMethod('POST');
    req.setBody(EncodingUtil.base64Encode(fileBody));
    req.setEndpoint(url);
    req.setTimeout(ConfigurationManager.CALLOUT_TIMEOUT);

    Blob key = EncodingUtil.base64Decode('mRMjHmlC1C+1L/Dkz8EJuw==');
    Blob cipherText = Crypto.encryptWithManagedIV('AES128', key, Blob.valueOf(ConfigurationManager.SID));
    String strToken = 'Basic ' +EncodingUtil.base64Encode(cipherText);
    req.setHeader('Authorization',strToken);

    req.setCompressed(false);
    req.setHeader('Content-Type', 'multipart/form-data'); 

    HttpResponse res = h.send(req);
    res.getBody();    
        
    system.debug('------- Body: ' + res.getBody());

  }
 
}