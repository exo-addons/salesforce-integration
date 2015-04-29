package org.exoplatform.salesforce.integ.connector.entity;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.exoplatform.salesforce.integ.util.PicklistEnumConverter;

import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * @author dev.zaouiahmed@gmail.com
 *
 */


/**
 * this entity will be used to push document content of an opportunity to eXo space
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ContentDocument {
	

	    // IsArchived
	    private Boolean IsArchived;

	    @JsonProperty("IsArchived")
	    public Boolean getIsArchived() {
	        return this.IsArchived;
	    }

	    @JsonProperty("IsArchived")
	    public void setIsArchived(Boolean IsArchived) {
	        this.IsArchived = IsArchived;
	    }

	    // ArchivedById
	    private String ArchivedById;

	    @JsonProperty("ArchivedById")
	    public String getArchivedById() {
	        return this.ArchivedById;
	    }

	    @JsonProperty("ArchivedById")
	    public void setArchivedById(String ArchivedById) {
	        this.ArchivedById = ArchivedById;
	    }

	    // ArchivedDate
	    private org.joda.time.DateTime ArchivedDate;

	    @JsonProperty("ArchivedDate")
	    public org.joda.time.DateTime getArchivedDate() {
	        return this.ArchivedDate;
	    }

	    @JsonProperty("ArchivedDate")
	    public void setArchivedDate(org.joda.time.DateTime ArchivedDate) {
	        this.ArchivedDate = ArchivedDate;
	    }

	    // Title
	    private String Title;
        
	    
	    @JsonProperty("Title")
	    public String getTitle() {
	        return this.Title;
	    }

	    @JsonProperty("Title")
	    public void setTitle(String Title) {
	        this.Title = Title;
	    }
	    
	    
	    //FileType
	    
	    private String FileType;
	    
	    @JsonProperty("FileType")
	    public String gettFileType() {
	        return this.FileType;
	    }
	    
	    @JsonProperty("FileType")
	    public void setFileType(String FileType) {
	        this.FileType = FileType;
	    }
	    
	    //OwnerId
	    
	    // Title
	    private String OwnerId;
        
	    
	    @JsonProperty("OwnerId")
	    public String getOwnerId() {
	        return this.OwnerId;
	    }

	    @JsonProperty("OwnerId")
	    public void setOwnerId(String OwnerId) {
	        this.OwnerId = OwnerId;
	    }
	    
	    

	    // PublishStatus
	    @XStreamConverter(PicklistEnumConverter.class)
	    private PublishStatusEnum PublishStatus;

	    @JsonProperty("PublishStatus")
	    public PublishStatusEnum getPublishStatus() {
	        return this.PublishStatus;
	    }

	    @JsonProperty("PublishStatus")
	    public void setPublishStatus(PublishStatusEnum PublishStatus) {
	        this.PublishStatus = PublishStatus;
	    }

	    // LatestPublishedVersionId
	    private String LatestPublishedVersionId;

	    @JsonProperty("LatestPublishedVersionId")
	    public String getLatestPublishedVersionId() {
	        return this.LatestPublishedVersionId;
	    }

	    @JsonProperty("LatestPublishedVersionId")
	    public void setLatestPublishedVersionId(String LatestPublishedVersionId) {
	        this.LatestPublishedVersionId = LatestPublishedVersionId;
	    }

	    // ParentId
	    private String ParentId;

	    @JsonProperty("ParentId")
	    public String getParentId() {
	        return this.ParentId;
	    }

	    @JsonProperty("ParentId")
	    public void setParentId(String ParentId) {
	        this.ParentId = ParentId;
	    }

	}


