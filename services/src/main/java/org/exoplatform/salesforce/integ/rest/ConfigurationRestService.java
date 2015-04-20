package org.exoplatform.salesforce.integ.rest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringEscapeUtils;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.salesforce.integ.connector.storage.api.ConfigurationInfoStorage;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.ext.app.SessionProviderService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.MembershipTypeHandler;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.Query;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserHandler;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.image.ImageUtils;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.model.AvatarAttachment;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.social.service.rest.RestChecker;
import org.exoplatform.social.service.rest.Util;
import org.json.JSONObject;






@Path("/salesforce")
public class ConfigurationRestService implements ResourceContainer {
    private static final String portalContainerName = "portal";
    private static final Log LOG = ExoLogger.getLogger(ConfigurationRestService.class.getName());
    private static final String[] SUPPORTED_FORMATS = new String[]{"json"};

    OrganizationService orgService = (OrganizationService) PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);
    RepositoryService repositoryService = (RepositoryService) PortalContainer.getInstance().getComponentInstanceOfType(RepositoryService.class);

    
    
    @POST
    @Path("config")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createGCUser(@Context HttpServletRequest request,@Context HttpServletResponse response,@Context UriInfo uriInfo,
                                 Map<String,String> conf) throws Exception {

        Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
        MediaType mediaType = RestChecker.checkSupportedFormat("json", SUPPORTED_FORMATS);
        try {
            if(sourceIdentity == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            String clientId=conf.get("firstname");
            String clientSecret =conf.get("clientSecret");
            String redirectUri=conf.get("redirectUri");
            
            System.out.println(clientId);
            System.out.println(clientSecret);
            System.out.println(redirectUri);
            




           

            JSONObject jsonGlobal = new JSONObject();
            jsonGlobal.put("message"," conf saved");
            return Response.ok(jsonGlobal.toString(), mediaType).build();
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An internal error has occured").build();
        }
    }

}