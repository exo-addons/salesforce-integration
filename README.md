# salesforce-integration
salesforce integration for eXo platform


Depends on Force.com REST API Connector please refer to https://github.com/jesperfj/force-rest-api to build from source  


uses case:
===============

-1- Clicking on the "create deal room" button from salesforce will create an exo space with some information of the selected opportunity(amout, stage description) and any modification on these fiels from salesforce will create new activity at eXo corresponding space.


![Opportunity update](https://raw.github.com/exo-addons/salesforce-integration/master/documentation/readme/update.png)

-2- Salesforce Document Synchronizer :

Clicking on get document from salesforce will get or synchronize the opportunity space document with the SF content.

![Salesforce attachement](https://raw.github.com/exo-addons/salesforce-integration/master/documentation/readme/opportunity_attachement.png)

![Synchronizer](https://raw.github.com/exo-addons/salesforce-integration/master/documentation/readme/content.png)