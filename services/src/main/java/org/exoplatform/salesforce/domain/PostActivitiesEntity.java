package org.exoplatform.salesforce.domain;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Created by bechir on 06/08/15.
 */
@Entity
@Table(name = "POST_ACTIVITIES")
@NamedQueries({
        @NamedQuery(name="PostActivitiesEntity.findPost",
                    query = "select p from PostActivitiesEntity p where p.postId = :postId"),
        @NamedQuery(name="PostActivitiesEntity.findActivity",
                query = "select p from PostActivitiesEntity p where p.activityId = :activityId")
})
public class PostActivitiesEntity {
    private long postActivityId;
    private String activityId;
    private String postId;

    public PostActivitiesEntity(String activityId, String postId) {
        this.activityId = activityId;
        this.postId = postId;
    }

    public PostActivitiesEntity() {
    }

    @Id
    @GeneratedValue
    @Column(name = "POST_ACTIVITY_ID", nullable = false, insertable = true, updatable = true)
    public long getPostActivityId() {
        return postActivityId;
    }

    public void setPostActivityId(long postActivityId) {
        this.postActivityId = postActivityId;
    }

    @Basic
    @Column(name = "ACTIVITY_ID", nullable = false, insertable = true, updatable = true, length = 50, unique = true)
    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    @Basic
    @Column(name = "POST_ID", nullable = false, insertable = true, updatable = true, length = 50, unique = true)
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostActivitiesEntity that = (PostActivitiesEntity) o;

        if (postActivityId != that.postActivityId) return false;
        if (activityId != null ? !activityId.equals(that.activityId) : that.activityId != null) return false;
        if (postId != null ? !postId.equals(that.postId) : that.postId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (postActivityId ^ (postActivityId >>> 32));
        result = 31 * result + (activityId != null ? activityId.hashCode() : 0);
        result = 31 * result + (postId != null ? postId.hashCode() : 0);
        return result;
    }
}
