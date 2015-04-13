package org.exoplatform.salesforce.integ.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.space.impl.DefaultSpaceApplicationHandler;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.social.service.rest.Util;

@Path("/salesforce")

public class OppRestService implements ResourceContainer { 
	  OrganizationService orgService = (OrganizationService) PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);
	    RepositoryService repositoryService = (RepositoryService) PortalContainer.getInstance().getComponentInstanceOfType(RepositoryService.class);
	    private static final String portalContainerName = "portal";


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
	    
}
