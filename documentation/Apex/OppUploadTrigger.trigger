trigger OppUploadTrigger on FeedItem (after insert) {


    for (FeedItem F : Trigger.new) {
    //check if the insert is  on opportunity
        if (F.ParentId.getSObjectType() == Opportunity.SObjectType){
                if(F.Type=='ContentPost')
                  { 
       HttpPostFileCallout.getContent('http://salesforce-01.no-ip.org:7443/rest/salesforce/chatterattachments/',F.ContentData);
                  }

            
           
           
        } 

       
    }
    
}