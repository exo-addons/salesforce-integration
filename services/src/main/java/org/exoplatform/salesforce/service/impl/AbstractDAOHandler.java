package org.exoplatform.salesforce.service.impl;

import org.exoplatform.salesforce.dao.PostActivitiesHandler;
import org.exoplatform.salesforce.service.DAOHandler;

abstract public class AbstractDAOHandler implements DAOHandler {

  protected PostActivitiesHandler paHandler;

  public PostActivitiesHandler getPostActivitiesHandler() {
    return paHandler;
  }

}