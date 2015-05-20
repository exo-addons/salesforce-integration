Setup the Application
===============

Configure your server to support SSL:
-----------------------

1. Generate server key using Java keytool:

keytool -genkey -alias alias -keypass mypassword -keystore keystore.key -storepass mypassword

2. Enable ssl into your platform_instalation/conf/server.xml

```
<Connector SSLEnabled="true" clientAuth="false" keystoreFile="/path_to_key/keystore.key"
     keystorePass="mypassword" maxThreads="200" port="8443" protocol="HTTP/1.1" scheme="https" secure="true" sslProtocol="TLS"/>
```


Add eXo server as a connected app to salesforce with OAuth settings :
-----------------------

1. From Setup, click Create | Apps and click New to start defining a connected app.
2. Enter the name of your application.
3. Enter the contact email information, as well as any other information appropriate for your application.
4. Select Enable OAuth Settings and enter the Callback URL: 

```
https://salesforce:8443/salesforce-extension/oauth/_callback
```
![Add Connected app to Salesforce](https://raw.github.com/exo-addons/salesforce-integration/master/documentation/readme/oauth.png)

5. Select OAuth Scopes : "full access"
6.Click Save. The Consumer Key is created and displayed, and the Consumer Secret is created (click the link to reveal it)

![Consumer Secret and Key](https://raw.github.com/exo-addons/salesforce-integration/master/documentation/readme/key.png)

Create Custom Buttons on Opportunities pages:
-----------------------

Steps to do:

1. Setup -> Customize -> Opportunity -> Buttons and Links.
2. Create new custom button called "Create eXo Deal Room".
3. Choose behaviour "Display in existing window without sidebar or header" 
4. Choose Content Source as  "URL" 
5. Paste in the code the link below:

```
https://salesforce:8443/salesforce-extension/oauth?oppID={!Opportunity.Id}
```
6-save

Note : 
- https://salesforce:8443/salesforce-extension/oauth?oppID is the link to your eXo server
- {!Opportunity.Id} dynamic query param that will be send to the server according to the current selected opportunity.

![Add Custom Button](https://raw.github.com/exo-addons/salesforce-integration/master/documentation/readme/custom_button.png)

Add the new button to the Opportunity page layout:
-----------------------

1. Go to the opportunity layout.
2. Select Buttons.
3. Drag and drop the newly created custom Button to the "Opportunity Detail" section (next the default standard ones "edit", "save" and "clone")
4. Save.

Now if you go back to the opportunities list page and if you select any opportunity you will see that your custom button was succefully added to the layout.
Clicking to that button will create an eXo space with some mapped opportunity information.

![Opportunity](https://raw.github.com/exo-addons/salesforce-integration/master/documentation/readme/opp.png)

Getting started
===============


Build from sources
------------------
Depends on Force.com REST API Connector please refer to https://github.com/jesperfj/force-rest-api to build from source.


Configuration
-------------

oauth.salesforce.clientId=3MVG9Rd3qC6oMalU6h9PD3dpIB7AXRLwl8iwMAPfzStu
oauth.salesforce.clientSecret=9140283238048111603
oauth.salesforce.redirectUri=https://serverName:8443/salesforce-extension/oauth/_callback

The `oauth.salesforce.clientId` parameter is the `Client ID` and
The `oauth.salesforce.clientSecret` parameter is the `client Secret`
are generated when setup the connected application to sales force see Setup the Application section
The `oauth.salesforce.redirectUri` parameter is the `redirect Uri` used for OAuth flows to pass an access token to query the salesforce from exo server