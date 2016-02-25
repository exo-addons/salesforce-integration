trigger OppFeedCommentTrigger on FeedComment(after insert) {
String parameters='poster='+EncodingUtil.urlEncode(UserInfo.getName(), 'UTF-8');


    for (FeedComment F : Trigger.new) {
    //check if the comment is  on opportunity post
        if (F.ParentId.getSObjectType() == Opportunity.SObjectType){
            // get the opportunity name
            Opportunity opp =[select Name from Opportunity where id=:F.ParentId];
            String oppName=opp.Name;
            parameters +='&oppName='+EncodingUtil.urlEncode(oppName, 'UTF-8');
            System.debug('the opportunity name is: '+oppName);
             parameters +='&postID='+F.FeedItemId;
                System.debug (UserInfo.getName()+ ' posted new comment'+  F.CommentBody);
                    //check if the comment on post is a mention 
                    
                     ConnectApi.FeedElement feedElement = ConnectApi.ChatterFeeds.getFeedElement(null, F.FeedItemId);
                     parameters += '&commentPost='+EncodingUtil.urlEncode(F.CommentBody, 'UTF-8');
                     String mentionnedNames='';
                     Boolean isFirst = true;
                     String mentionnedIds='';
                    ConnectApi.Comment comment=ConnectApi.ChatterFeeds.getComment(null, F.Id);


               List<ConnectApi.MessageSegment> messageSegments = comment.body.messageSegments;
                        for (ConnectApi.MessageSegment messageSegment : messageSegments) {
                                if (messageSegment instanceof ConnectApi.MentionSegment) {
                                    ConnectApi.MentionSegment mentionSegment = (ConnectApi.MentionSegment) messageSegment;
                                    System.debug('Mentioned user name: ' + mentionSegment.name);
                                    //System.debug('Text message:'+mentionSegment.name);
                                    if(isFirst) {
                                    mentionnedNames+=mentionSegment.name;
                                    mentionnedIds+=mentionSegment.record.id;
                                    isFirst=false;
                                    }else{
                                     mentionnedNames+=','+mentionSegment.name;
                                     mentionnedIds+=','+mentionSegment.record.id;
                                    }
                                }

                        }
                        if(String.isNotEmpty(mentionnedNames)) {
                            parameters += '&mentionned='+EncodingUtil.urlEncode(mentionnedNames, 'UTF-8');
                            parameters += '&mentionnedIds='+EncodingUtil.urlEncode(mentionnedIds, 'UTF-8');
                        }






        }
        System.Debug(parameters);
        HttpCallout.getContent(ConfigurationManager.CALLOUT_ENDPOINT+'salesforce/chattercomments/'+F.ParentId+'?'+parameters);
    }

}