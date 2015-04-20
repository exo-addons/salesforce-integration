/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.salesforce.portlet.config;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import juzu.Path;
import juzu.Response;
import juzu.View;
import juzu.template.Template;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.salesforce.integ.connector.storage.api.ConfigurationInfoStorage;





public class ConfigurationController {
	
    @Inject
    @Path("index.gtmpl")
    Template index;
    
    @View
    public Response.Content index() throws Exception {
    	
    	ConfigurationInfoStorage configurationInfoStorage = (ConfigurationInfoStorage) PortalContainer.getInstance().getComponentInstanceOfType(ConfigurationInfoStorage.class);

        Map<String, String> result = new HashMap<String, String>();
        if (configurationInfoStorage.getConfigurationInfo()!=null){
        	
            result.put("clientId", configurationInfoStorage.getConfigurationInfo().getClientId());
            result.put("clientSecret", configurationInfoStorage.getConfigurationInfo().getClientSecret());
            result.put("redirectUri", configurationInfoStorage.getConfigurationInfo().getRedirectUri());

        }
        return index.with(result).ok();
    }
    




  
}
