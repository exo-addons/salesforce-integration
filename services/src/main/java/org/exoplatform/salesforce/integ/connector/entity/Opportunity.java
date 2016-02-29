package org.exoplatform.salesforce.integ.connector.entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.exoplatform.salesforce.integ.util.PicklistEnumConverter;

import com.thoughtworks.xstream.annotations.XStreamConverter;
/**
 * @author dev.zaouiahmed@gmail.com
 *
 */

//https://www.salesforce.com/developer/docs/api/Content/sforce_api_objects_opportunity.htm

@JsonIgnoreProperties(ignoreUnknown=true)
public class Opportunity {
	 
	
	private String id;

	@JsonProperty("Id")
	public String getId() {
		return this.id;
	}
	
	private String Name;

	@JsonProperty("Name")
	public String getName() {
		return this.Name;
	}

	@JsonProperty("Name")
	public void setName(String Name) {
		this.Name = Name;
	}

	// AccountId
	private String AccountId;

	@JsonProperty("AccountId")
	public String getAccountId() {
		return this.AccountId;
	}

	@JsonProperty("AccountId")
	public void setAccountId(String AccountId) {
		this.AccountId = AccountId;
	}

	// RecordTypeId
	private String RecordTypeId;

	@JsonProperty("RecordTypeId")
	public String getRecordTypeId() {
		return this.RecordTypeId;
	}

	@JsonProperty("RecordTypeId")
	public void setRecordTypeId(String RecordTypeId) {
		this.RecordTypeId = RecordTypeId;
	}

	// Description
	private String Description;

	@JsonProperty("Description")
	public String getDescription() {
		return this.Description;
	}

	@JsonProperty("Description")
	public void setDescription(String Description) {
		this.Description = Description;
	}

	// StageName
	@XStreamConverter(PicklistEnumConverter.class)
	private StageNameEnum StageName;

	@JsonProperty("StageName")
	public StageNameEnum getStageName() {
		return this.StageName;
	}

	@JsonProperty("StageName")
	public void setStageName(StageNameEnum StageName) {
		this.StageName = StageName;
	}

	// Amount
	private Double Amount;

	@JsonProperty("Amount")
	public Double getAmount() {
		return this.Amount;
	}

	@JsonProperty("Amount")
	public void setAmount(Double Amount) {
		this.Amount = Amount;
	}

	// Probability
	private Double Probability;

	@JsonProperty("Probability")
	public Double getProbability() {
		return this.Probability;
	}

	@JsonProperty("Probability")
	public void setProbability(Double Probability) {
		this.Probability = Probability;
	}

	// ExpectedRevenue
	private Double ExpectedRevenue;

	@JsonProperty("ExpectedRevenue")
	public Double getExpectedRevenue() {
		return this.ExpectedRevenue;
	}

	@JsonProperty("ExpectedRevenue")
	public void setExpectedRevenue(Double ExpectedRevenue) {
		this.ExpectedRevenue = ExpectedRevenue;
	}

	// TotalOpportunityQuantity
	private Double TotalOpportunityQuantity;

	@JsonProperty("TotalOpportunityQuantity")
	public Double getTotalOpportunityQuantity() {
		return this.TotalOpportunityQuantity;
	}

	@JsonProperty("TotalOpportunityQuantity")
	public void setTotalOpportunityQuantity(Double TotalOpportunityQuantity) {
		this.TotalOpportunityQuantity = TotalOpportunityQuantity;
	}

	// CloseDate
	private org.joda.time.DateTime CloseDate;

	@JsonProperty("CloseDate")
	public org.joda.time.DateTime getCloseDate() {
		return this.CloseDate;
	}

	@JsonProperty("CloseDate")
	public void setCloseDate(org.joda.time.DateTime CloseDate) {
		this.CloseDate = CloseDate;
	}

	// Type
	@XStreamConverter(PicklistEnumConverter.class)
	private TypeEnum Type;

	@JsonProperty("Type")
	public TypeEnum getType() {
		return this.Type;
	}

	@JsonProperty("Type")
	public void setType(TypeEnum Type) {
		this.Type = Type;
	}

	// NextStep
	private String NextStep;

	@JsonProperty("NextStep")
	public String getNextStep() {
		return this.NextStep;
	}

	@JsonProperty("NextStep")
	public void setNextStep(String NextStep) {
		this.NextStep = NextStep;
	}

	// IsClosed
	private Boolean IsClosed;

	@JsonProperty("IsClosed")
	public Boolean getIsClosed() {
		return this.IsClosed;
	}

	@JsonProperty("IsClosed")
	public void setIsClosed(Boolean IsClosed) {
		this.IsClosed = IsClosed;
	}

	// IsWon
	private Boolean IsWon;

	@JsonProperty("IsWon")
	public Boolean getIsWon() {
		return this.IsWon;
	}

	@JsonProperty("IsWon")
	public void setIsWon(Boolean IsWon) {
		this.IsWon = IsWon;
	}

	// ForecastCategory
	@XStreamConverter(PicklistEnumConverter.class)
	private ForecastCategoryEnum ForecastCategory;

	@JsonProperty("ForecastCategory")
	public ForecastCategoryEnum getForecastCategory() {
		return this.ForecastCategory;
	}

	@JsonProperty("ForecastCategory")
	public void setForecastCategory(ForecastCategoryEnum ForecastCategory) {
		this.ForecastCategory = ForecastCategory;
	}

	// ForecastCategoryName
	@XStreamConverter(PicklistEnumConverter.class)
	private ForecastCategoryNameEnum ForecastCategoryName;

	@JsonProperty("ForecastCategoryName")
	public ForecastCategoryNameEnum getForecastCategoryName() {
		return this.ForecastCategoryName;
	}

	@JsonProperty("ForecastCategoryName")
	public void setForecastCategoryName(ForecastCategoryNameEnum ForecastCategoryName) {
		this.ForecastCategoryName = ForecastCategoryName;
	}

	// CurrencyIsoCode
	@XStreamConverter(PicklistEnumConverter.class)
	private CurrencyIsoCodeEnum CurrencyIsoCode;

	@JsonProperty("CurrencyIsoCode")
	public CurrencyIsoCodeEnum getCurrencyIsoCode() {
		return this.CurrencyIsoCode;
	}

	@JsonProperty("CurrencyIsoCode")
	public void setCurrencyIsoCode(CurrencyIsoCodeEnum CurrencyIsoCode) {
		this.CurrencyIsoCode = CurrencyIsoCode;
	}

	// CampaignId
	private String CampaignId;

	@JsonProperty("CampaignId")
	public String getCampaignId() {
		return this.CampaignId;
	}

	@JsonProperty("CampaignId")
	public void setCampaignId(String CampaignId) {
		this.CampaignId = CampaignId;
	}

	// HasOpportunityLineItem
	private Boolean HasOpportunityLineItem;

	@JsonProperty("HasOpportunityLineItem")
	public Boolean getHasOpportunityLineItem() {
		return this.HasOpportunityLineItem;
	}

	@JsonProperty("HasOpportunityLineItem")
	public void setHasOpportunityLineItem(Boolean HasOpportunityLineItem) {
		this.HasOpportunityLineItem = HasOpportunityLineItem;
	}

	// Pricebook2Id
	private String Pricebook2Id;

	@JsonProperty("Pricebook2Id")
	public String getPricebook2Id() {
		return this.Pricebook2Id;
	}

	@JsonProperty("Pricebook2Id")
	public void setPricebook2Id(String Pricebook2Id) {
		this.Pricebook2Id = Pricebook2Id;
	}

	// TerritoryId
	private String TerritoryId;

	@JsonProperty("TerritoryId")
	public String getTerritoryId() {
		return this.TerritoryId;
	}

	@JsonProperty("TerritoryId")
	public void setTerritoryId(String TerritoryId) {
		this.TerritoryId = TerritoryId;
	}

	// FiscalQuarter
	private Integer FiscalQuarter;

	@JsonProperty("FiscalQuarter")
	public Integer getFiscalQuarter() {
		return this.FiscalQuarter;
	}

	@JsonProperty("FiscalQuarter")
	public void setFiscalQuarter(Integer FiscalQuarter) {
		this.FiscalQuarter = FiscalQuarter;
	}

	// FiscalYear
	private Integer FiscalYear;

	@JsonProperty("FiscalYear")
	public Integer getFiscalYear() {
		return this.FiscalYear;
	}

	@JsonProperty("FiscalYear")
	public void setFiscalYear(Integer FiscalYear) {
		this.FiscalYear = FiscalYear;
	}

	// Fiscal
	private String Fiscal;

	@JsonProperty("Fiscal")
	public String getFiscal() {
		return this.Fiscal;
	}

	@JsonProperty("Fiscal")
	public void setFiscal(String Fiscal) {
		this.Fiscal = Fiscal;
	}

	// SyncedQuoteId
	private String SyncedQuoteId;

	@JsonProperty("SyncedQuoteId")
	public String getSyncedQuoteId() {
		return this.SyncedQuoteId;
	}

	@JsonProperty("SyncedQuoteId")
	public void setSyncedQuoteId(String SyncedQuoteId) {
		this.SyncedQuoteId = SyncedQuoteId;
	}

	// ProductType__c
	@XStreamConverter(PicklistEnumConverter.class)
	private ProductTypeEnum ProductType__c;

	@JsonProperty("ProductType__c")
	public ProductTypeEnum getProductType__c() {
		return this.ProductType__c;
	}

	@JsonProperty("ProductType__c")
	public void setProductType__c(ProductTypeEnum ProductType__c) {
		this.ProductType__c = ProductType__c;
	}




}
