public class HttpPostFileCallout {
 

   @future(callout=true)
   public static void getContent(String url,Blob fileBody) {
 

    Http h = new Http();
 

    HttpRequest req = new HttpRequest();
    req.setMethod('POST');
    req.setBody(EncodingUtil.base64Encode(fileBody));
    req.setEndpoint(url);

    req.setMethod('POST');
    req.setCompressed(false);
    req.setHeader('Content-Type', 'multipart/form-data'); 

    HttpResponse res = h.send(req);
    res.getBody();    
        
    system.debug('------- Body: ' + res.getBody());

  }
 
}