package org.exoplatform.salesforce.integ.rest;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.net.URI;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.salesforce.integ.connector.entity.Opportunity;
import org.exoplatform.salesforce.integ.connector.servlet.OAuthServlet;
import org.exoplatform.salesforce.integ.connector.storage.api.ConfigurationInfoStorage;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.activity.model.ExoSocialActivityImpl;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.impl.DefaultSpaceApplicationHandler;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.social.service.rest.RestChecker;
import org.exoplatform.social.service.rest.Util;
import org.joda.time.DateTime;
import org.json.JSONObject;

import com.force.api.ForceApi;
import com.force.api.QueryResult;

@Path("/salesforce")

public class OppRestService implements ResourceContainer { 
	  OrganizationService orgService = (OrganizationService) PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);
	    RepositoryService repositoryService = (RepositoryService) PortalContainer.getInstance().getComponentInstanceOfType(RepositoryService.class);
		private DateTime CloseDate;
	    private static final String portalContainerName = "portal";
	    private static final String[] SUPPORTED_FORMATS = new String[]{"json"};


	    @GET
	    @Path("create/{oppName}")
	    public Response createOpp(@Context HttpServletRequest request,
	                                      @PathParam("oppName") String opportunity,
	                                      @QueryParam("ammount")String ammount,
	                                      @QueryParam("description")String description,
	                                      @QueryParam("isClosed")String isClosed,
	                                      @QueryParam("stageName")String stageName) {
	    //	oppName+"?"+ammount+"?"+description+"?"+isClosed+"?"+stageName)
	    	Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
	    	 SpaceService spaceService = Util.getSpaceService(portalContainerName);
	    	 IdentityManager identityManager = Util.getIdentityManager(portalContainerName);
	    	 Cookie[] cookies = request.getCookies();
	            String accesstoken=null;
	            String instance_url=null;
	    	 ActivityManager activityManager = (ActivityManager) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(ActivityManager.class);
	    	 ExoSocialActivity activity = new ExoSocialActivityImpl();
            if (sourceIdentity == null)
				return Response.status(Response.Status.UNAUTHORIZED).build();
            //if request param are lost try to query from request name
		if (stageName == null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie1 = cookies[i];
				if (cookie1.getName().equals("tk_ck_")) {

					accesstoken = cookie1.getValue();
				}

				if (cookie1.getName().equals("inst_ck_")) {

					instance_url = cookie1.getValue();
				}

			}
			if(accesstoken!=null&&instance_url!=null){
				
				ForceApi api = OAuthServlet.initApiFromCookies(accesstoken, instance_url);
				QueryResult<Opportunity> q=api.query("SELECT Amount,CloseDate,StageName,isClosed,Description FROM Opportunity where Name="+ "\'"+opportunity+"\' LIMIT 1", Opportunity.class);
				if(q.getTotalSize()>0){
					
					ammount = q.getRecords().get(0).getAmount().toString();
					description = q.getRecords().get(0).getDescription();
					isClosed = q.getRecords().get(0).getIsClosed().toString();
					CloseDate=q.getRecords().get(0).getCloseDate();
					stageName = q.getRecords().get(0).getStageName().toString();
					
				}
			}
		}
       	 String owner = sourceIdentity.getRemoteId();
            Space project_ = new Space();
            project_.setDisplayName(opportunity);
            project_.setPrettyName(opportunity);
            project_.setRegistration(Space.OPEN);
            project_.setDescription(description);
            project_.setType(DefaultSpaceApplicationHandler.NAME);
            project_.setVisibility(Space.PUBLIC);
            project_.setRegistration(Space.VALIDATION);
            project_.setPriority(Space.INTERMEDIATE_PRIORITY);
           Space s= spaceService.createSpace(project_, owner);
            if (s != null) {
            	 activity.setUserId(sourceIdentity.getId());
                Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, s.getPrettyName(), false);
                activity.setTitle("The opportunity: " +opportunity +" With description: "+description+ " and stage :"+stageName +" And ammount :" + ammount 
                		+" has been imported to eXo");
                activity.setType("Salesforce_Activity");
                activity.setBody("The opportunity: " +opportunity +" descp: "+description+" has a stage :stageName" );
                activityManager.saveActivityNoReturn(spaceIdentity, activity);
            } 
          System.out.println(s.getPrettyName());

       
           // return Response.seeOther(URI.create(Util.getBaseUrl() + "/portal/invitations").build();
            return Response.seeOther(URI.create(Util.getBaseUrl() + "/portal")).build();
           // return Response.ok("Created").build();
	    }
	    
	    
	    @POST
	    @Path("config")
	    @Consumes({MediaType.APPLICATION_JSON})
	    public Response createConfig(@Context HttpServletRequest request,@Context HttpServletResponse response,@Context UriInfo uriInfo,
	                                 Map<String,String> conf) throws Exception {

	        Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
	        MediaType mediaType = RestChecker.checkSupportedFormat("json", SUPPORTED_FORMATS);
	        ConfigurationInfoStorage configurationInfoStorage = (ConfigurationInfoStorage) PortalContainer.getInstance().getComponentInstanceOfType(ConfigurationInfoStorage.class);
	        try {
	            if(sourceIdentity == null) {
	                return Response.status(Response.Status.UNAUTHORIZED).build();
	            }
	            String clientId=conf.get("clientId");
	            String clientSecret =conf.get("clientSecret");
	            String redirectUri=conf.get("redirectUri");
	            
	            
	            System.out.println(clientId);
	            System.out.println(clientSecret);
	            System.out.println(redirectUri);
	            configurationInfoStorage.saveConfigurationInfo(clientId, clientSecret, redirectUri);

	            JSONObject jsonGlobal = new JSONObject();
	            jsonGlobal.put("message"," conf saved");
	            return Response.ok(jsonGlobal.toString(), mediaType).build();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An internal error has occured").build();
	        }
	    }
	    
	    @GET
	    @Path("getconfig")
	    @Consumes({MediaType.APPLICATION_JSON})
	    public Response getConfig(@Context HttpServletRequest request,@Context HttpServletResponse response,@Context UriInfo uriInfo) throws Exception {
	        Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
	        MediaType mediaType = RestChecker.checkSupportedFormat("json", SUPPORTED_FORMATS);
	        ConfigurationInfoStorage configurationInfoStorage = (ConfigurationInfoStorage) PortalContainer.getInstance().getComponentInstanceOfType(ConfigurationInfoStorage.class);
	        try {
	            IdentityManager identityManager=Util.getIdentityManager(portalContainerName);
	            if(sourceIdentity == null) {
	                return Response.status(Response.Status.UNAUTHORIZED).build();
	            }
	            
	            JSONObject json = new JSONObject();
	           

	            
	            String clientId=configurationInfoStorage.getConfigurationInfo().getClientId();
	            String clientSecret =configurationInfoStorage.getConfigurationInfo().getClientSecret();
	            String redirectUri=configurationInfoStorage.getConfigurationInfo().getRedirectUri();
	            
	            configurationInfoStorage.getConfigurationInfo().getClientId();
	            json.put("clientId",clientId);
	            json.put("clientSecret",clientSecret);
	            json.put("redirectUri",redirectUri);

	           
	            return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An internal error has occured").build();
	        }
	    }
	    
	    
	    
}
