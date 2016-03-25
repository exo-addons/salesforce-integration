trigger OpportunityTrackerTrigger on Opportunity (after update) {
    String parameters = '';
    for (Opportunity opp : Trigger.new) {
        // Access the "old" record by its ID in Trigger.oldMap
        Opportunity oldOpp = Trigger.oldMap.get(opp.Id);

        parameters += 'newName='+EncodingUtil.urlEncode(opp.Name, 'UTF-8');
        if(oldOpp.stageName != opp.stageName) {
            parameters += '&oldstageName='+EncodingUtil.urlEncode(oldOpp.stageName, 'UTF-8');
            parameters += '&newstageName='+EncodingUtil.urlEncode(opp.stageName, 'UTF-8');
        }
        parameters += '&oldamount='+oldOpp.Amount;
        parameters += '&newamount='+opp.Amount;
        parameters += '&oldclosedate='+oldOpp.CloseDate.format();
        parameters += '&newclosedate='+opp.CloseDate.format();
        String olddescription = '';
        String newdescription = '';
        if(oldOpp.Description != null) {
          olddescription = oldOpp.Description;
        }
        if(opp.Description != null) {
          newdescription = opp.Description;
        }
        parameters += '&olddescription='+olddescription;
        parameters += '&newdescription='+newdescription;

		if (!Test.isRunningTest()) {
        	HttpCallout.getContent(ConfigurationManager.CALLOUT_ENDPOINT+'salesforce/addupdatecomment/'+EncodingUtil.urlEncode(oldOpp.Name, 'UTF-8')+'?'+parameters);
        }
    }
}