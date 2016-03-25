@isTest(SeeAllData=true)
public class OppFeedTriggerTest {
	static testMethod void insertFeedItemTest(){        
        Test.startTest();
        
		Account newAccount = new Account (name = 'Test_Account',
        BillingCountry = 'Billing Country');
        insert newAccount;       
        
        // Add Opportunity
        Opportunity opp = new Opportunity(
        	Name = 'Test Opp',
        	CloseDate=Date.Today(), 
        	Contract_End_Date__c=Date.Today(), 
        	Annual_Contract_Value__c=100,
        	StageName='Open',
            AccountId = newAccount.Id
        );
        insert opp;
                
        Opportunity oppInserted = [Select Id From Opportunity Where Id =:opp.Id];
        System.assert(oppInserted!=null);   
        
        FeedItem textPost = new FeedItem(
        	Body = 'Welcome',
        	ParentId = opp.Id,
            Type = 'TextPost'
        );
        insert textPost;
        
        ContentVersion cv = new ContentVersion();
        cv.title='Test title';       
        cv.VersionData=blob.valueof('New Bitmap Image.bmp');
        cv.PathOnClient = 'fakePath';
        insert cv;

        FeedItem contentPost = new FeedItem(
        	Body = 'Welcome',
        	ParentId = opp.Id,
            Type = 'ContentPost',
            RelatedRecordId = cv.Id
        );
        insert contentPost;
        
        FeedItem linkPost = new FeedItem(
        	Body = 'Welcome',
        	ParentId = opp.Id,
            Type = 'LinkPost',
            LinkUrl = 'fakeURL'
        );
        insert linkPost;               
               
        Test.stopTest();
    }    
	static testMethod void insertFeedCommentTest(){        
        Test.startTest();
        
		Account newAccount = new Account (name = 'Test_Account',
        BillingCountry = 'Billing Country');
        insert newAccount;       
        
        // Add Opportunity
        Opportunity opp = new Opportunity(
        	Name = 'Test Opp',
        	CloseDate=Date.Today(), 
        	Contract_End_Date__c=Date.Today(), 
        	Annual_Contract_Value__c=100,
        	StageName='Open',
            AccountId = newAccount.Id
        );
        insert opp;
                
        Opportunity oppInserted = [Select Id From Opportunity Where Id =:opp.Id];
        System.assert(oppInserted!=null);     
        
        FeedItem textPost = new FeedItem(
        	Body = 'Welcome',
        	ParentId = opp.Id,
            Type = 'TextPost'
        );
        insert textPost;

        FeedComment feedComment = new FeedComment(
            CommentBody = 'Comment',
            FeedItemId = textPost.Id
        );
        insert feedComment;                

	    Test.stopTest();               
    }
    // Test HttpCallout Separately until we have a way to test it in the triggers
    static testMethod void HttpCalloutTest(){
        Test.startTest();
        SingleRequestMock fakeResponse = new SingleRequestMock(200,
                                                               'Complete',
                                                               '[{"Name": "sForceTest1"}]',
                                                               null);        
        Test.setMock(HttpCalloutMock.class, fakeResponse);
        
        // Fake callout to post a file
        HttpPostFileCallout.getContent(ConfigurationManager.CALLOUT_ENDPOINT+'salesforce/chatterattachments/FakeId',Blob.valueOf('Name: sForceTest1'));
        // Fake callout to post a text
        HttpCallout.getContent(ConfigurationManager.CALLOUT_ENDPOINT+'salesforce/chatterpost/FakeId');
        
        Test.stopTest();
    }
}