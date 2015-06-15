package org.exoplatform.salesforce.integ.connector.entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/*
 *
https://developer.salesforce.com/docs/atlas.en-us.api.meta/api/sforce_api_calls_query_aggregateresult.htm
 used to count the total feed in aka opp
*/


@JsonIgnoreProperties(ignoreUnknown=true)
public class AggregateResult {
	
	
	
	private String expr0;

	@JsonProperty("expr0")
	public String getexpr0() {
		return this.expr0;
	}
	

}
