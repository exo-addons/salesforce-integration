public class HttpCallout {
 
// Pass in the endpoint to be used using the string url
   @future(callout=true)
   public static void getContent(String url) {
 
// Instantiate a new http object
    Http h = new Http();
 
// Instantiate a new HTTP request, specify the method (GET) as well as the endpoint
    HttpRequest req = new HttpRequest();
    req.setEndpoint(url);
    req.setMethod('GET');
 

// Send the request
    h.send(req);
  }
 
}