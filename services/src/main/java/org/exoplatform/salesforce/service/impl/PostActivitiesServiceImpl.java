package org.exoplatform.salesforce.service.impl;

import org.exoplatform.salesforce.PostActivitiesEntity;
import org.exoplatform.salesforce.service.DAOHandler;
import org.exoplatform.salesforce.service.PostActivitiesService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.inject.Inject;

/**
 * Created by bechir on 10/08/15.
 */
public class PostActivitiesServiceImpl implements PostActivitiesService {

    private static final Log LOG = ExoLogger.getExoLogger(PostActivitiesServiceImpl.class);

    @Inject
    DAOHandler daoHandler;

    public PostActivitiesServiceImpl() {
    }

    //For testing purpose only
    public PostActivitiesServiceImpl(DAOHandler daoHandler) {
        this.daoHandler = daoHandler;
    }

    @Override
    public PostActivitiesEntity createEntity(PostActivitiesEntity postActivitiesEntity) {
        PostActivitiesEntity obj = daoHandler.getPostActivitiesHandler().create(postActivitiesEntity);
        return obj;
    }

    @Override
    public PostActivitiesEntity updateEntity(String postId) {
        PostActivitiesEntity obj = daoHandler.getPostActivitiesHandler().findPost(postId);
        PostActivitiesEntity objUpdated = daoHandler.getPostActivitiesHandler().update(obj);
        return objUpdated;
    }

    @Override
    public PostActivitiesEntity findEntityByPostId(String postId) {
        PostActivitiesEntity obj = daoHandler.getPostActivitiesHandler().findPost(postId);
        return obj;
    }

    @Override
    public PostActivitiesEntity findEntityByActivityId(String activityId) {
        PostActivitiesEntity obj = daoHandler.getPostActivitiesHandler().findActivity(activityId);
        return obj;
    }

}
