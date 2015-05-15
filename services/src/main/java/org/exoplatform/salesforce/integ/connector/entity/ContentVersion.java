package org.exoplatform.salesforce.integ.connector.entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import org.exoplatform.salesforce.integ.util.PicklistEnumConverter;

/**
 *  * based on generated DTO objects using camel-salesforce maven plugin 
 * @author dev.zaouiahmed@gmail.com 
 */
/*
    "checksum",
    "contentDocument",
    "contentDocumentId",
    "contentLocation",
    "contentModifiedById",
    "contentModifiedDate",
    "contentSize",
    "contentUrl",
    "description",
    "externalDataSourceId",
    "externalDocumentInfo1",
    "externalDocumentInfo2",
    "featuredContentBoost",
    "featuredContentDate",
    "fileExtension",
    "fileType",
    "firstPublishLocationId",
    "isLatest",
    "origin",,
    "pathOnClient",
    "positiveRatingCount",
    "publishStatus",
    "ratingCount",
    "reasonForChange",
    "tagCsv",
    "title",
    "versionData",
    "versionNumber"
    */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ContentVersion {
	private String ContentDocumentId;
	  private Boolean IsLatest;
	  private String ContentUrl;
	  private String VersionNumber;
	  private String Title;
	  private String Description;
	  private String ReasonForChange;
	  private String PathOnClient;
	  private Integer RatingCount;
	  private DateTime ContentModifiedDate;
	  private String ContentModifiedById;
	  private Integer PositiveRatingCount;
	  private Integer NegativeRatingCount;
	  private Integer FeaturedContentBoost;
	  private DateTime FeaturedContentDate;
	  private String TagCsv;
	  private String FileType;

	  @XStreamConverter(PicklistEnumConverter.class)
	  private PublishStatusEnum PublishStatus;

	  @XStreamAlias("VersionData")
	  private String VersionDataUrl;
	  private Integer ContentSize;
	  private String FirstPublishLocationId;

	  @XStreamConverter(PicklistEnumConverter.class)
	  private OriginEnum Origin;

	  @XStreamConverter(PicklistEnumConverter.class)
	  private ContentLocationEnum ContentLocation;
	  private String ExternalDocumentInfo1;
	  private String ExternalDocumentInfo2;
	  private String ExternalDataSourceId;
	  private String Checksum;

	  @JsonProperty("ContentDocumentId")
	  public String getContentDocumentId()
	  {
	    return this.ContentDocumentId;
	  }

	  @JsonProperty("ContentDocumentId")
	  public void setContentDocumentId(String ContentDocumentId) {
	    this.ContentDocumentId = ContentDocumentId;
	  }

	  @JsonProperty("IsLatest")
	  public Boolean getIsLatest()
	  {
	    return this.IsLatest;
	  }

	  @JsonProperty("IsLatest")
	  public void setIsLatest(Boolean IsLatest) {
	    this.IsLatest = IsLatest;
	  }

	  @JsonProperty("ContentUrl")
	  public String getContentUrl()
	  {
	    return this.ContentUrl;
	  }

	  @JsonProperty("ContentUrl")
	  public void setContentUrl(String ContentUrl) {
	    this.ContentUrl = ContentUrl;
	  }

	  @JsonProperty("VersionNumber")
	  public String getVersionNumber()
	  {
	    return this.VersionNumber;
	  }

	  @JsonProperty("VersionNumber")
	  public void setVersionNumber(String VersionNumber) {
	    this.VersionNumber = VersionNumber;
	  }

	  @JsonProperty("Title")
	  public String getTitle()
	  {
	    return this.Title;
	  }

	  @JsonProperty("Title")
	  public void setTitle(String Title) {
	    this.Title = Title;
	  }

	  @JsonProperty("Description")
	  public String getDescription()
	  {
	    return this.Description;
	  }

	  @JsonProperty("Description")
	  public void setDescription(String Description) {
	    this.Description = Description;
	  }

	  @JsonProperty("ReasonForChange")
	  public String getReasonForChange()
	  {
	    return this.ReasonForChange;
	  }

	  @JsonProperty("ReasonForChange")
	  public void setReasonForChange(String ReasonForChange) {
	    this.ReasonForChange = ReasonForChange;
	  }

	  @JsonProperty("PathOnClient")
	  public String getPathOnClient()
	  {
	    return this.PathOnClient;
	  }

	  @JsonProperty("PathOnClient")
	  public void setPathOnClient(String PathOnClient) {
	    this.PathOnClient = PathOnClient;
	  }

	  @JsonProperty("RatingCount")
	  public Integer getRatingCount()
	  {
	    return this.RatingCount;
	  }

	  @JsonProperty("RatingCount")
	  public void setRatingCount(Integer RatingCount) {
	    this.RatingCount = RatingCount;
	  }

	  @JsonProperty("ContentModifiedDate")
	  public DateTime getContentModifiedDate()
	  {
	    return this.ContentModifiedDate;
	  }

	  @JsonProperty("ContentModifiedDate")
	  public void setContentModifiedDate(DateTime ContentModifiedDate) {
	    this.ContentModifiedDate = ContentModifiedDate;
	  }

	  @JsonProperty("ContentModifiedById")
	  public String getContentModifiedById()
	  {
	    return this.ContentModifiedById;
	  }

	  @JsonProperty("ContentModifiedById")
	  public void setContentModifiedById(String ContentModifiedById) {
	    this.ContentModifiedById = ContentModifiedById;
	  }

	  @JsonProperty("PositiveRatingCount")
	  public Integer getPositiveRatingCount()
	  {
	    return this.PositiveRatingCount;
	  }

	  @JsonProperty("PositiveRatingCount")
	  public void setPositiveRatingCount(Integer PositiveRatingCount) {
	    this.PositiveRatingCount = PositiveRatingCount;
	  }

	  @JsonProperty("NegativeRatingCount")
	  public Integer getNegativeRatingCount()
	  {
	    return this.NegativeRatingCount;
	  }

	  @JsonProperty("NegativeRatingCount")
	  public void setNegativeRatingCount(Integer NegativeRatingCount) {
	    this.NegativeRatingCount = NegativeRatingCount;
	  }

	  @JsonProperty("FeaturedContentBoost")
	  public Integer getFeaturedContentBoost()
	  {
	    return this.FeaturedContentBoost;
	  }

	  @JsonProperty("FeaturedContentBoost")
	  public void setFeaturedContentBoost(Integer FeaturedContentBoost) {
	    this.FeaturedContentBoost = FeaturedContentBoost;
	  }

	  @JsonProperty("FeaturedContentDate")
	  public DateTime getFeaturedContentDate()
	  {
	    return this.FeaturedContentDate;
	  }

	  @JsonProperty("FeaturedContentDate")
	  public void setFeaturedContentDate(DateTime FeaturedContentDate) {
	    this.FeaturedContentDate = FeaturedContentDate;
	  }

	  @JsonProperty("TagCsv")
	  public String getTagCsv()
	  {
	    return this.TagCsv;
	  }

	  @JsonProperty("TagCsv")
	  public void setTagCsv(String TagCsv) {
	    this.TagCsv = TagCsv;
	  }

	  @JsonProperty("FileType")
	  public String getFileType()
	  {
	    return this.FileType;
	  }

	  @JsonProperty("FileType")
	  public void setFileType(String FileType) {
	    this.FileType = FileType;
	  }

	  @JsonProperty("PublishStatus")
	  public PublishStatusEnum getPublishStatus()
	  {
	    return this.PublishStatus;
	  }

	  @JsonProperty("PublishStatus")
	  public void setPublishStatus(PublishStatusEnum PublishStatus) {
	    this.PublishStatus = PublishStatus;
	  }

	  @JsonProperty("VersionData")
	  public String getVersionDataUrl()
	  {
	    return this.VersionDataUrl;
	  }

	  @JsonProperty("VersionData")
	  public void setVersionDataUrl(String VersionDataUrl) {
	    this.VersionDataUrl = VersionDataUrl;
	  }

	  @JsonProperty("ContentSize")
	  public Integer getContentSize()
	  {
	    return this.ContentSize;
	  }

	  @JsonProperty("ContentSize")
	  public void setContentSize(Integer ContentSize) {
	    this.ContentSize = ContentSize;
	  }

	  @JsonProperty("FirstPublishLocationId")
	  public String getFirstPublishLocationId()
	  {
	    return this.FirstPublishLocationId;
	  }

	  @JsonProperty("FirstPublishLocationId")
	  public void setFirstPublishLocationId(String FirstPublishLocationId) {
	    this.FirstPublishLocationId = FirstPublishLocationId;
	  }

	  @JsonProperty("Origin")
	  public OriginEnum getOrigin()
	  {
	    return this.Origin;
	  }

	  @JsonProperty("Origin")
	  public void setOrigin(OriginEnum Origin) {
	    this.Origin = Origin;
	  }

	  @JsonProperty("ContentLocation")
	  public ContentLocationEnum getContentLocation()
	  {
	    return this.ContentLocation;
	  }

	  @JsonProperty("ContentLocation")
	  public void setContentLocation(ContentLocationEnum ContentLocation) {
	    this.ContentLocation = ContentLocation;
	  }

	  @JsonProperty("ExternalDocumentInfo1")
	  public String getExternalDocumentInfo1()
	  {
	    return this.ExternalDocumentInfo1;
	  }

	  @JsonProperty("ExternalDocumentInfo1")
	  public void setExternalDocumentInfo1(String ExternalDocumentInfo1) {
	    this.ExternalDocumentInfo1 = ExternalDocumentInfo1;
	  }

	  @JsonProperty("ExternalDocumentInfo2")
	  public String getExternalDocumentInfo2()
	  {
	    return this.ExternalDocumentInfo2;
	  }

	  @JsonProperty("ExternalDocumentInfo2")
	  public void setExternalDocumentInfo2(String ExternalDocumentInfo2) {
	    this.ExternalDocumentInfo2 = ExternalDocumentInfo2;
	  }

	  @JsonProperty("ExternalDataSourceId")
	  public String getExternalDataSourceId()
	  {
	    return this.ExternalDataSourceId;
	  }

	  @JsonProperty("ExternalDataSourceId")
	  public void setExternalDataSourceId(String ExternalDataSourceId) {
	    this.ExternalDataSourceId = ExternalDataSourceId;
	  }

	  @JsonProperty("Checksum")
	  public String getChecksum()
	  {
	    return this.Checksum;
	  }

	  @JsonProperty("Checksum")
	  public void setChecksum(String Checksum) {
	    this.Checksum = Checksum;
	  }
}
