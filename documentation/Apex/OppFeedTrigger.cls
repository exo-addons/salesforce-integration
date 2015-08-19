trigger OppFeedTrigger on FeedItem (after insert) {

String parameters='poster='+EncodingUtil.urlEncode(UserInfo.getName(), 'UTF-8');
Blob data=null;

    for (FeedItem F : Trigger.new) {
    //check if the insert is  on opportunity
        if (F.ParentId.getSObjectType() == Opportunity.SObjectType){
            // get the opportunity name
            Opportunity opp =[select Name from Opportunity where id=:F.ParentId];
            String oppName=opp.Name;
            parameters +='&oppName='+EncodingUtil.urlEncode(oppName, 'UTF-8');
            System.debug('the opportunity name is: '+oppName);
             parameters +='&postId='+F.id;
             parameters +='&postType='+F.Type;
             String mentionnedNames='';
             Boolean isFirst = true;
             //check if the text post is a mention 
             ConnectApi.FeedElement feedElement = ConnectApi.ChatterFeeds.getFeedElement(null, F.id);
             List<ConnectApi.MessageSegment> messageSegments = feedElement.body.messageSegments;
                        for (ConnectApi.MessageSegment messageSegment : messageSegments) {
                        System.debug('feedElement.body'+feedElement.body);
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
                                    
                                    System.debug(' the mentionned Name is:'+mentionSegment.name);
                                    //parameters += '&mentionned='+EncodingUtil.urlEncode(mentionSegment.name, 'UTF-8');
                                }
                                
                        }
                        if(String.isNotEmpty(mentionnedNames))  
                        parameters += '&mentionned='+EncodingUtil.urlEncode(mentionnedNames, 'UTF-8');
                if(F.Type=='TextPost')
                {
                System.debug (UserInfo.getName()+ ' posted new message'+  F.body);
                     parameters += '&textPost='+EncodingUtil.urlEncode(F.body, 'UTF-8');
                 }
         
                if(F.Type=='ContentPost')
                  { 
                  System.debug('is content post');
                     parameters += '&contentPost='+EncodingUtil.urlEncode(F.ContentFileName, 'UTF-8');
                     
                    if(F.body!=null)
                        {
                       parameters += '&contentPostText='+EncodingUtil.urlEncode(F.body, 'UTF-8');
                        }

                    if(F.ContentData!=null)
                        {
                      data=F.ContentData;
                        }
                        
                         
                      else
                      //get the related record if attached from existing content
                        {
                          ContentVersion C =[select VersionData from ContentVersion where id=:F.RelatedRecordId];
                          data=C.VersionData;
                        }
                        parameters +='&contentID='+F.RelatedRecordId;
                        System.debug('http://salesforce-01.no-ip.org:7443/rest/salesforce/chatterattachments/'+F.ParentId+'?'+parameters); 
              HttpPostFileCallout.getContent('http://salesforce-01.no-ip.org:7443/rest/salesforce/chatterattachments/'+F.ParentId+'?'+parameters,data);
              return;
                 
                  }
               if(F.Type=='LinkPost')
                  { 
                   System.debug('link url'+F.LinkUrl);
                   parameters += 'postedlink='+EncodingUtil.urlEncode(F.LinkUrl, 'UTF-8');
                  }
                  

           
           
        } 
                              
        HttpCallout.getContent('http://salesforce-01.no-ip.org:7443/rest/salesforce/chatterpost/'+F.ParentId+'?'+parameters);
    }
    
}