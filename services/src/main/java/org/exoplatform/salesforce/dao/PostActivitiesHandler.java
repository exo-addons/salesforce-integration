package org.exoplatform.salesforce.dao;

import org.exoplatform.commons.api.persistence.GenericDAO;
import org.exoplatform.salesforce.domain.PostActivitiesEntity;

/**
 * Created by bechir on 06/08/15.
 */
public interface PostActivitiesHandler extends GenericDAO<PostActivitiesEntity, Long> {
    PostActivitiesEntity findPost(String postId);

    PostActivitiesEntity findActivity(String activityId);
}
