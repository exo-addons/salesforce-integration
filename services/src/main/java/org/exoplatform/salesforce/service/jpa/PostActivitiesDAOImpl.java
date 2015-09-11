package org.exoplatform.salesforce.service.jpa;

import org.exoplatform.commons.persistence.impl.EntityManagerService;
import org.exoplatform.commons.persistence.impl.GenericDAOJPAImpl;
import org.exoplatform.commons.api.persistence.ExoTransactional;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.salesforce.dao.PostActivitiesHandler;
import org.exoplatform.salesforce.domain.PostActivitiesEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.logging.Logger;

/**
 * Created by bechir on 06/08/15.
 */
public class PostActivitiesDAOImpl extends GenericDAOJPAImpl<PostActivitiesEntity, Long> implements PostActivitiesHandler {

    private static final Logger LOG = Logger.getLogger("PostActivitiesDAOImpl");

    private EntityManagerService entityService;

    public PostActivitiesDAOImpl(EntityManagerService entityService) {
        this.entityService = entityService;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityService.getEntityManager();
    }
    
    @ExoTransactional
    @Override
	public PostActivitiesEntity create(PostActivitiesEntity entity) {
		PortalContainer container = PortalContainer.getInstance();

		RequestLifeCycle.begin(container);
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
		RequestLifeCycle.end();

		return entity;
	}

    @Override
    public PostActivitiesEntity findPost(String postId) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("PostActivitiesEntity.findPost",PostActivitiesEntity.class);
        query.setParameter("postId", postId);

        return (PostActivitiesEntity) query.getSingleResult();
    }

    @Override
    public PostActivitiesEntity findActivity(String activityId) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("PostActivitiesEntity.findActivity",PostActivitiesEntity.class);
        query.setParameter("activityId", activityId);

        return (PostActivitiesEntity) query.getSingleResult();
    }
}
