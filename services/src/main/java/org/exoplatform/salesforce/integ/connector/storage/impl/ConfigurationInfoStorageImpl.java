package org.exoplatform.salesforce.integ.connector.storage.impl;

import javax.jcr.Node;

import org.chromattic.api.ChromatticSession;
import org.exoplatform.commons.chromattic.ChromatticLifeCycle;
import org.exoplatform.commons.chromattic.ChromatticManager;
import org.exoplatform.salesforce.integ.connector.entity.ConfigurationInfo;
import org.exoplatform.salesforce.integ.connector.storage.api.ConfigurationInfoStorage;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;


public class ConfigurationInfoStorageImpl implements ConfigurationInfoStorage{



    public static final String PERCENT_STR = "%";
    private ChromatticLifeCycle lifeCycle;

    public ConfigurationInfoStorageImpl(ChromatticManager chromatticManager, NodeHierarchyCreator nodeHierarchyCreator)
    {
        this.lifeCycle = chromatticManager.getLifeCycle("sf");
    }

    public ChromatticSession getSession() {
        return lifeCycle.getChromattic().openSession();
    }

	@Override
	public void saveConfigurationInfo(String clientId, String clientSecret,
			String redirectUri) throws Exception {
		
        Node node = getSession().getJCRSession().getRootNode();
        node = node.getNode("production");
        if (!node.hasNode("ConfigurationInfo"))
        {
            node = node.addNode("ConfigurationInfo", "sf:ConfigurationInfo");
            node.addMixin("mix:referenceable");
            node.getSession().save();
        }
        ChromatticSession session = getSession();
        ConfigurationInfo ConfigurationInfo = (ConfigurationInfo)session.findByPath(ConfigurationInfo.class, "/production/ConfigurationInfo", true);
        if (ConfigurationInfo != null)
        {
            if(clientId!=null) ConfigurationInfo.setClientId(clientId);
            if(clientSecret!=null) ConfigurationInfo.setClientSecret(clientSecret);
            if(redirectUri!=null) ConfigurationInfo.setRedirectUri(redirectUri);


            session.save();
        }
        
		
	}

	@Override
	public ConfigurationInfo getConfigurationInfo() throws Exception {
		ChromatticSession session = getSession();
        ConfigurationInfo ConfigurationInfo = (ConfigurationInfo)session.findByPath(ConfigurationInfo.class, "/production/ConfigurationInfo", true);
        if(ConfigurationInfo==null) {
            Node node = getSession().getJCRSession().getRootNode();
            node = node.getNode("production");
                node = node.addNode("ConfigurationInfo", "sf:configurationInfo");
                node.addMixin("mix:referenceable");
                node.getSession().save();

             ConfigurationInfo = (ConfigurationInfo)session.findByPath(ConfigurationInfo.class, "/production/ConfigurationInfo", true);

               ConfigurationInfo.setClientId("");
               ConfigurationInfo.setClientSecret("");
               ConfigurationInfo.setRedirectUri("");



                session.save();

        }
        return ConfigurationInfo;
	}

    


}