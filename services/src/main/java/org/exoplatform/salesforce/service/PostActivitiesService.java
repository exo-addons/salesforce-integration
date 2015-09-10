package org.exoplatform.salesforce.service;

import org.exoplatform.salesforce.domain.PostActivitiesEntity;

/**
 * Created by bechir on 10/08/15.
 */
public interface PostActivitiesService {
    public PostActivitiesEntity createEntity(PostActivitiesEntity postActivitiesEntity);
    public PostActivitiesEntity updateEntity(String postId);
    public PostActivitiesEntity findEntityByPostId(String postId);
    public PostActivitiesEntity findEntityByActivityId(String activityId);
}
