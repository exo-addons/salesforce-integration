package org.exoplatform.salesforce.service.jpa;

import org.exoplatform.commons.persistence.impl.EntityManagerService;
import org.exoplatform.salesforce.service.DAOHandler;
import org.exoplatform.salesforce.service.impl.AbstractDAOHandler;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

/**
 * Created by bechir on 06/08/15.
 */
public class DAOHandlerJPAImpl extends AbstractDAOHandler implements DAOHandler {

    private static final Log LOG = ExoLogger.getLogger("DAOHandlerJPAImpl");

    public DAOHandlerJPAImpl(EntityManagerService entityService) {
        LOG.info("DAOHandlerJPAImpl is creating...");
        paHandler = new PostActivitiesDAOImpl(entityService);
        LOG.info("DAOHandlerJPAImpl is created");
    }

}
