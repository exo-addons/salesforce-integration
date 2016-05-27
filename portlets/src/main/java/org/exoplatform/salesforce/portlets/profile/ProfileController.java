package org.exoplatform.salesforce.portlets.profile;

import com.force.api.ApiException;
import com.force.api.ForceApi;
import com.force.api.QueryResult;
import juzu.Path;
import juzu.Response;
import juzu.View;
import juzu.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.portal.application.RequestNavigationData;
import org.exoplatform.salesforce.integ.connector.entity.Contact;
import org.exoplatform.services.security.Identity;
import org.exoplatform.social.common.router.ExoRouter;
import org.exoplatform.salesforce.integ.connector.entity.Account;
import org.exoplatform.salesforce.integ.connector.entity.Lead;
import org.exoplatform.salesforce.service.SalesforceLogin;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserHandler;
import org.exoplatform.services.security.ConversationState;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Bechir on 11/05/16.
 */

public class ProfileController {

    private static final Logger LOG = Logger.getLogger(ProfileController.class.getName());

    @Inject
    @Path("index.gtmpl")
    Template index;

    @Inject
    OrganizationService organizationService;

    @Inject
    private SalesforceLogin salesforceLogin;

    private ForceApi api;

    private static final String allowedGroupId = "/spaces/exo_employees";

    private String Id = "";
    private String SFUrl = "";
    private String fullName = "";
    private String accountName = "";
    private String timeZone = "";
    private String rating = "";
    private String status = "";
    private String description = "";
    private Boolean hasOpportunity = true;
    private String owner;

    @PostConstruct
    public void init() {
        api = salesforceLogin.getSfApi();
        if(api == null) {
            api = salesforceLogin.loginToSalesforce();
        }
    }

    @View
    public Response.Content index() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        if(api == null) {
            LOG.warning("Can not retreive lead informations: Can not connect to Salesforce.");
            result.put("message","error");
            return index.with(result).ok();
        }
        Identity currentUserIdentity = ConversationState.getCurrent().getIdentity();
        String currentUserName = currentUserIdentity.getUserId();
        if(!currentUserIdentity.isMemberOf(allowedGroupId)) {
            //LOG.warning(currentUserIdentity.getUserId()+" is not member of "+allowedGroupId);
            result.put("message","error");
            return index.with(result).ok();
        }
        PortalRequestContext portalRequestContext = PortalRequestContext.getCurrentInstance();
        String requestPath = "/" + portalRequestContext.getControllerContext().getParameter(RequestNavigationData.REQUEST_PATH);
        ExoRouter.Route route = ExoRouter.route(requestPath);
        if (route != null) {
            currentUserName = route.localArgs.get("streamOwnerId");
        }

        try {
            UserHandler uh = organizationService.getUserHandler();
            User userByName = uh.findUserByName(currentUserName);
            if (userByName == null) {
                userByName = uh.findUserByName(currentUserIdentity.getUserId());
            }
            String contactQuery = "SELECT Id, FirstName, LastName, Description, Contact_Status__c, Timezone__c, AccountId FROM Contact where Email=" + "\'" + userByName.getEmail() + "\' LIMIT 1";
            QueryResult queryResult = api.query(contactQuery, Contact.class);
            if(queryResult.getTotalSize() > 0) {
                result.put("userType","Contact");
                List<Contact> contactList = queryResult.getRecords();
                for (Contact contact : contactList) {
                    Id = contact.getId();
                    SFUrl = salesforceLogin.getLoginEndPoint() + "/" + contact.getId();
                    if(StringUtils.isNotBlank(contact.getFirstName())) {
                        fullName = contact.getFirstName() + " " + contact.getLastName();
                    } else {
                        fullName = contact.getLastName();
                    }
                    description = contact.getDescription();
                    if (contact.getAccountId() != null && !contact.getAccountId().equals("")) {
                        Account contactAccount = api.getSObject("Account", contact.getAccountId()).as(Account.class);
                        accountName = contactAccount.getName();
                    }
                    if (contact.getTimezone__c() != null) {
                        timeZone = contact.getTimezone__c().value();
                    }
                    if (contact.getContact_Status__c() != null) {
                        status = contact.getContact_Status__c().value();
                    }
                }
            } else {
                String leadQuery = "SELECT Id, FirstName, LastName, Description, OwnerId, Account__c, Timezone__c, Rating, Status, isConverted FROM Lead where Email=" + "\'" + userByName.getEmail() + "\' LIMIT 1";
                queryResult = api.query(leadQuery, Lead.class);
                List<Lead> leadList = queryResult.getRecords();
                for (Lead lead : leadList) {
                    result.put("userType","Lead");
                    Id = lead.getId();
                    SFUrl = salesforceLogin.getLoginEndPoint() + "/" + lead.getId();
                    if(StringUtils.isNotBlank(lead.getFirstName())) {
                        fullName = lead.getFirstName() + " " + lead.getLastName();
                    } else {
                        fullName = lead.getLastName();
                    }
                    if (lead.getAccount__c() != null && !lead.getAccount__c().equals("")) {
                        Account leadAccount = api.getSObject("Account", lead.getAccount__c()).as(Account.class);
                        accountName = leadAccount.getName();
                    }
                    try {
                        org.exoplatform.salesforce.integ.connector.entity.User leadOwner = api.getSObject("User", lead.getOwnerId()).as(org.exoplatform.salesforce.integ.connector.entity.User.class);
                        if(leadOwner != null) {
                            if(StringUtils.isNotBlank(leadOwner.getFirstName())) {
                                owner = leadOwner.getFirstName() + " " + leadOwner.getLastName();
                            } else {
                                owner = leadOwner.getLastName();
                            }
                        }
                    } catch (ApiException apiex) {
                        owner = "Unassigned";
                    }
                    if (lead.getTimezone__c() != null) {
                        timeZone = lead.getTimezone__c().value();
                    }
                    if (lead.getRating() != null) {
                        rating = lead.getRating().value();
                    }
                    if (lead.getStatus() != null) {
                        status = lead.getStatus().value();
                    }
                    hasOpportunity = lead.getIsConverted();

                }
            }
            if (result.containsKey("userType")) {
                result.put("message", "success");
                result.put("Id", Id);
                result.put("SFUrl", SFUrl);
                result.put("fullName", fullName);
                result.put("accountName", accountName);
                result.put("owner", owner);
                result.put("timeZone", timeZone);
                result.put("rating", rating);
                result.put("status",status);
                result.put("description", description);
                result.put("hasOpportunity", hasOpportunity);
            } else {
                result.put("message", "error");
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
            result.put("message","error");
        }
        return index.with(result).ok();
    }
}
