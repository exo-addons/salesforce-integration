package org.exoplatform.salesforce.integ.rest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.salesforce.integ.connector.storage.api.ConfigurationInfoStorage;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.impl.DefaultSpaceApplicationHandler;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.social.service.rest.RestChecker;
import org.exoplatform.social.service.rest.Util;
import org.json.JSONObject;

@Path("/salesforce")

public class OppRestService implements ResourceContainer { 
	  OrganizationService orgService = (OrganizationService) PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);
	    RepositoryService repositoryService = (RepositoryService) PortalContainer.getInstance().getComponentInstanceOfType(RepositoryService.class);
	    private static final String portalContainerName = "portal";
	    private static final String[] SUPPORTED_FORMATS = new String[]{"json"};


	    @GET
	    @Path("create/{oppName}")
	    public Response emailSetsubstatus(@Context HttpServletRequest request,
	                                      @PathParam("oppName") String opportunity) {
	    	Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
	    	 SpaceService spaceService = Util.getSpaceService(portalContainerName);
	    
            if (sourceIdentity == null)
				return Response.status(Response.Status.UNAUTHORIZED).build();
       	 String owner = sourceIdentity.getRemoteId();
            Space project_ = new Space();
            project_.setDisplayName(opportunity);
            project_.setPrettyName(opportunity);
            project_.setRegistration(Space.OPEN);
            project_.setDescription("projectDescription");
            project_.setType(DefaultSpaceApplicationHandler.NAME);
            project_.setVisibility(Space.PUBLIC);
            project_.setRegistration(Space.VALIDATION);
            project_.setPriority(Space.INTERMEDIATE_PRIORITY);
            spaceService.createSpace(project_, owner);
            return Response.ok("Created").build();
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
