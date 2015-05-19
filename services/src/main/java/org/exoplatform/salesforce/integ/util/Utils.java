package org.exoplatform.salesforce.integ.util;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.social.service.rest.Util;

public class Utils {
	private static final String portalContainerName = "portal";
	private static IdentityManager identityManager = Util.getIdentityManager(portalContainerName);
	
	public static boolean iSOpportunitySpace(String spaceName){
		SpaceService spaceService = Util.getSpaceService(portalContainerName);
		Space space=spaceService.getSpaceByPrettyName(spaceName);
		if(space!=null){
			 Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, space.getPrettyName(), false);
			 if(spaceIdentity.getProfile().getProperty("oppID")!=null)
				 return true;
		}
		return false;
		
	}
	
	public static String getSpaceUrl(String spaceName){
		SpaceService spaceService = Util.getSpaceService(portalContainerName);
		Space space=spaceService.getSpaceByPrettyName(spaceName);
		StringBuffer baseSpaceURL = null;
		baseSpaceURL = new StringBuffer();
		 baseSpaceURL.append(PortalContainer.getCurrentPortalContainerName()+ "/g/:spaces:") ;
		String groupId = space.getGroupId();
		String permanentSpaceName = groupId.split("/")[2];
		if (permanentSpaceName.equals(space.getPrettyName())) {
			baseSpaceURL.append(permanentSpaceName);
			baseSpaceURL.append("/");
			baseSpaceURL.append(permanentSpaceName);
		} else {
			baseSpaceURL.append(space.getPrettyName());
			baseSpaceURL.append("/");
			baseSpaceURL.append(space.getPrettyName());
		}
		return baseSpaceURL.toString();
	}

}
