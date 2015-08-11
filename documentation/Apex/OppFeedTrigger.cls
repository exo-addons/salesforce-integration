trigger OppFeedTrigger on FeedItem (after insert) {

String parameters='poster='+EncodingUtil.urlEncode(UserInfo.getName(), 'UTF-8');


    for (FeedItem F : Trigger.new) {
    //check if the insert is  on opportunity
        if (F.ParentId.getSObjectType() == Opportunity.SObjectType){
            // get the opportunity name
            Opportunity opp =[select Name from Opportunity where id=:F.ParentId];
            String oppName=opp.Name;
            parameters +='&oppName='+EncodingUtil.urlEncode(oppName, 'UTF-8');
            System.debug('the opportunity name is: '+oppName);
             parameters +='&postType='+F.Type;
             parameters +='&postId='+F.id;
                if(F.Type=='TextPost')
                {
                System.debug (UserInfo.getName()+ ' posted new message'+  F.body);
                    //check if the text post is a mention 
                     ConnectApi.FeedElement feedElement = ConnectApi.ChatterFeeds.getFeedElement(null, F.id);
                     parameters += '&textPost='+EncodingUtil.urlEncode(F.body, 'UTF-8');
                     String mentionnedNames='';
                     Boolean isFirst = true;
                List<ConnectApi.MessageSegment> messageSegments = feedElement.body.messageSegments;
                        for (ConnectApi.MessageSegment messageSegment : messageSegments) {
                                if (messageSegment instanceof ConnectApi.MentionSegment) {
                                    ConnectApi.MentionSegment mentionSegment = (ConnectApi.MentionSegment) messageSegment;
                                    System.debug('Mentioned user name: ' + mentionSegment.name);
                                    //System.debug('Text message:'+mentionSegment.name);
                                    if(isFirst) {
                                    mentionnedNames+=mentionSegment.name;
                                    isFirst=false;
                                    }else{
                                     mentionnedNames+=','+mentionSegment.name;
                                    }
                                    
                                    //System.debug(' the mentionned Name is:'+mentionSegment.name);
                                    //parameters += '&mentionned='+EncodingUtil.urlEncode(mentionSegment.name, 'UTF-8');
                                }
                                
                        }
                        if(String.isNotEmpty(mentionnedNames))  
                        parameters += '&mentionned='+EncodingUtil.urlEncode(mentionnedNames, 'UTF-8');
                 }
         
                if(F.Type=='ContentPost')
                  { 
                     parameters += '&contentPost='+EncodingUtil.urlEncode(F.ContentFileName, 'UTF-8');
                     
                    if(F.body!=null)
                        {
                       parameters += '&contentPostText='+EncodingUtil.urlEncode(F.body, 'UTF-8');
                        }
                  }
               if(F.Type=='LinkPost')
                  { 
                   System.debug('link url'+F.LinkUrl);
                   parameters += 'postedlink='+EncodingUtil.urlEncode(F.LinkUrl, 'UTF-8');
                  }
            
           
           
        } 
        // HttpCallout.getContent('http://yyyy:8080/rest/salesforce/addupdatecomment/'+opp.Id+'?'+parameters);
        System.Debug(parameters);                        
        HttpCallout.getContent('http://salesforce.no-ip.org:7443/rest/salesforce/chatterpost/'+F.ParentId+'?'+parameters);
    }
    
}