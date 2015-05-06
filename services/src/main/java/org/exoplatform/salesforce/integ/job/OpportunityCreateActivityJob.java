package org.exoplatform.salesforce.integ.job;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.salesforce.integ.connector.entity.Opportunity;
import org.exoplatform.salesforce.integ.connector.entity.UserConfig;
import org.exoplatform.salesforce.integ.rest.UserService;
import org.exoplatform.salesforce.integ.util.RequestKeysConstants;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.activity.model.ExoSocialActivityImpl;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.social.service.rest.Util;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.force.api.ApiConfig;
import com.force.api.ApiSession;
import com.force.api.ApiVersion;
import com.force.api.ForceApi;
import com.force.api.QueryResult;


/**
 * @author dev.zaouiahmed@gmail.com
 *
 */
public class OpportunityCreateActivityJob implements Job {
	private static final Log LOG = ExoLogger.getLogger(OpportunityCreateActivityJob.class);
	SpaceService spaceService = (SpaceService) PortalContainer.getInstance().getComponentInstanceOfType(SpaceService.class);
    ActivityManager activityManager = (ActivityManager) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(ActivityManager.class);
	private  final AtomicBoolean notStarted = new AtomicBoolean();

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			//UserService.map
	Iterator<Entry<String, UserConfig>> iter = UserService.userMap.entrySet().iterator();

	

			 
			while (iter.hasNext()&&!notStarted.get()) {
				notStarted.set(true);
				LOG.info("------------------->"+notStarted.get());
				Entry<String, UserConfig> mEntry = iter.next();
				//LOG.info(mEntry.getKey() + " : " + mEntry.getValue().getAccesstoken());
				
				ApiVersion apiVersion = ApiVersion.DEFAULT_VERSION;
				ApiConfig c = new ApiConfig()
				.setClientId(System.getProperty("oauth.salesforce.clientId"))
				.setClientSecret(System.getProperty("oauth.salesforce.clientSecret"))
				.setRedirectURI(System.getProperty("oauth.salesforce.redirectUri"))
				.setLoginEndpoint(RequestKeysConstants.SF_PROD)
				.setApiVersion(apiVersion);
				
				ApiSession s =  new ApiSession(mEntry.getValue().getAccesstoken(),mEntry.getValue().getInstanceUrl());
				QueryResult<Opportunity> q= new ForceApi(c,s).query("SELECT Name,Amount,CloseDate,StageName,isClosed,Description FROM Opportunity LIMIT 1000", Opportunity.class);
				List<Opportunity> opp =q.getRecords();
				if(opp.size()>0){
					for(int i=0;i<opp.size();i++){
						Space sp =spaceService.getSpaceByDisplayName(opp.get(i).getName());
						if(sp!=null){
				            IdentityManager identityManager=Util.getIdentityManager("portal");
				            if(identityManager == null) {
				                return;
				            }
							 Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, sp.getPrettyName(), false);
							 if(opp.get(i).getDescription()!=null&&spaceIdentity.getProfile().getProperty("description")!=null&&!spaceIdentity.getProfile().getProperty("description").equals("Not defined"))
							 { 
								 if(opp.get(i).getDescription().equals(spaceIdentity.getProfile().getProperty("description")))
								 {
									 LOG.info("for descri"+opp.get(i).getName());
								 ExoSocialActivity activity = new ExoSocialActivityImpl();
								// activity.set
								 activity.setTitle("The description of the opportunity has been updated to :"+opp.get(i).getDescription());
					                activity.setType("Salesforce_Activity");
					                activity.setBody("The description of the opportunity has been updated to :"+opp.get(i).getDescription());
					                activityManager.saveActivityNoReturn(spaceIdentity, activity);
								 }
							 }
							 
							 if(opp.get(i).getCloseDate()!=null&&spaceIdentity.getProfile().getProperty("CloseDate")!=null)
									if(opp.get(i).getCloseDate()!=null){
										DateTimeFormatter dateFormat = DateTimeFormat
												.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
										DateTime t =dateFormat.parseDateTime(opp.get(i).getCloseDate().toString());
										//t.toString().equals(spaceIdentity.getProfile().getProperty("CloseDate"));
										 if(!t.toString().equals(spaceIdentity.getProfile().getProperty("CloseDate")))
										 {
											 LOG.info("for close date"+opp.get(i).getName());
										 ExoSocialActivity activity = new ExoSocialActivityImpl();
										// activity.set
										 activity.setTitle("The close date of the opportunity has been updated to :"+t.toString());
							                activity.setType("Salesforce_Activity");
							                activity.setBody("The close date of the opportunity has been updated to :"+t.toString());
							                activityManager.saveActivityNoReturn(spaceIdentity, activity);
										 }
										
									}
							 
							 //LOG.info(opp.get(i).getCloseDate().toString());
							 
							 if(spaceIdentity.getProfile().getProperty("stageName")!=null&&!spaceIdentity.getProfile().getProperty("stageName").equals("Not defined")&&opp.get(i).getStageName().toString()!=null)
							 {
								 if(!opp.get(i).getStageName().value().equals(spaceIdentity.getProfile().getProperty("stageName")))
								 {
									 LOG.info("for stage "+opp.get(i).getName());
								 ExoSocialActivity activity = new ExoSocialActivityImpl();
								// activity.set
								 activity.setTitle("The stage of the opportunity has been updated to :"+opp.get(i).getStageName().value());
					                activity.setType("Salesforce_Activity");
					                activity.setBody("The stage of the opportunity has been updated to :"+opp.get(i).getStageName().value());
					                activityManager.saveActivityNoReturn(spaceIdentity, activity);
								 }
								 
							 }
							 
							 
							 
							 if(spaceIdentity.getProfile().getProperty("ammount")!=null && !spaceIdentity.getProfile().getProperty("ammount").equals("Not defined")&&opp.get(i).getAmount()!=null)
							 { 
								 if(!opp.get(i).getAmount().toString().equals(spaceIdentity.getProfile().getProperty("ammount")))
								 {
								 ExoSocialActivity activity = new ExoSocialActivityImpl();
								// activity.set
								 activity.setTitle("The Amount of the opportunity has been updated to :"+opp.get(i).getAmount());
					                activity.setType("Salesforce_Activity");
					                activity.setBody("The Amount of the opportunity has been updated to :"+opp.get(i).getAmount());
					                activityManager.saveActivityNoReturn(spaceIdentity, activity);
								 }
							 }
							
						}
					}
					
					
				}
				// oppProfile.setProperty("opportunityName", opportunity);
                //oppProfile.setProperty("description", description);
               // oppProfile.setProperty("CloseDate", closeDate);
               // oppProfile.setProperty("ammount", ammount);
               // oppProfile.setProperty("stageName", stageName);
				
			}
			notStarted.set(false);
		} catch (Exception e) {
			LOG.error("e opportunity update job", e);
			notStarted.set(true);
		}
		
		
	}

}
