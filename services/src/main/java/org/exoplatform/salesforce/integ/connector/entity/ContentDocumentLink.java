package org.exoplatform.salesforce.integ.connector.entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.exoplatform.salesforce.integ.util.PicklistEnumConverter;

import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * based on generated DTO objects using camel-salesforce maven plugin 
 * @author dev.zaouiahmed@gmail.com 
 * https://www.salesforce.com/developer/docs/api/Content/sforce_api_objects_contentdocumentlink.htm
 */


@JsonIgnoreProperties(ignoreUnknown=true)
	public class ContentDocumentLink  {

	    // LinkedEntityId
	    private String LinkedEntityId;

	    @JsonProperty("LinkedEntityId")
	    public String getLinkedEntityId() {
	        return this.LinkedEntityId;
	    }

	    @JsonProperty("LinkedEntityId")
	    public void setLinkedEntityId(String LinkedEntityId) {
	        this.LinkedEntityId = LinkedEntityId;
	    }

	    // ContentDocumentId
	    private String ContentDocumentId;

	    @JsonProperty("ContentDocumentId")
	    public String getContentDocumentId() {
	        return this.ContentDocumentId;
	    }

	    @JsonProperty("ContentDocumentId")
	    public void setContentDocumentId(String ContentDocumentId) {
	        this.ContentDocumentId = ContentDocumentId;
	    }

	    // ShareType
	    @XStreamConverter(PicklistEnumConverter.class)
	    private ShareTypeEnum ShareType;

	    @JsonProperty("ShareType")
	    public ShareTypeEnum getShareType() {
	        return this.ShareType;
	    }

	    @JsonProperty("ShareType")
	    public void setShareType(ShareTypeEnum ShareType) {
	        this.ShareType = ShareType;
	    }
}
