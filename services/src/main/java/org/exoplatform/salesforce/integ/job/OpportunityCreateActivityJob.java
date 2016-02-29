package org.exoplatform.salesforce.integ.job;

import com.force.api.*;
import org.exoplatform.commons.utils.PropertyManager;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.salesforce.VariablesUtil;
import org.exoplatform.salesforce.integ.component.activity.UISalesforceActivityBuilder;
import org.exoplatform.salesforce.integ.connector.entity.Opportunity;
import org.exoplatform.salesforce.integ.connector.entity.UserConfig;
import org.exoplatform.salesforce.integ.rest.UserService;
import org.exoplatform.salesforce.integ.util.RequestKeysConstants;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author dev.zaouiahmed@gmail.com
 *
 */
public class OpportunityCreateActivityJob implements Job,VariablesUtil {
	private static final Log LOG = ExoLogger.getLogger(OpportunityCreateActivityJob.class);
	SpaceService spaceService = (SpaceService) PortalContainer.getInstance().getComponentInstanceOfType(SpaceService.class);
    ActivityManager activityManager = (ActivityManager) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(ActivityManager.class);
	//used to check if there is an already job running 
    private  final AtomicBoolean notStarted = new AtomicBoolean();

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			//UserService.map
	Iterator<Entry<String, UserConfig>> iter = UserService.userMap.entrySet().iterator();

	

			 
			while (iter.hasNext()&&!notStarted.get()) {
				notStarted.set(true);
				Entry<String, UserConfig> mEntry = iter.next();
				ApiVersion apiVersion = ApiVersion.DEFAULT_VERSION;
				ApiConfig c = new ApiConfig()
				.setClientId(PropertyManager.getProperty(CLIENT_ID))
				.setClientSecret(PropertyManager.getProperty(CLIENT_SECRET))
				.setRedirectURI(PropertyManager.getProperty(REDIRECT_URI))
				.setLoginEndpoint(PropertyManager.getProperty(SF_INSTANCE_URL))
				.setApiVersion(apiVersion);
				
				ApiSession s =  new ApiSession(mEntry.getValue().getAccesstoken(),mEntry.getValue().getInstanceUrl());
				QueryResult<Opportunity> q= new ForceApi(c,s).query("SELECT Id,Name,Amount,CloseDate,StageName,isClosed,Description FROM Opportunity LIMIT 1000", Opportunity.class);
				List<Opportunity> opp =q.getRecords();
				boolean paramsUpdated = false;
				for(int index =0; index < opp.size();index ++)
				if(opp.size()>0){
					for(int i=0;i<opp.size();i++){
						Space sp =spaceService.getSpaceByDisplayName(opp.get(i).getName());
						if(sp!=null){
				            IdentityManager identityManager=Util.getIdentityManager("portal");
				            if(identityManager == null) {
				                return;
				            }
							 Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, sp.getPrettyName(), false);
							 if(spaceIdentity.getProfile().getProperty("oppID")!=null&& !opp.get(i).getId().equals(spaceIdentity.getProfile().getProperty("oppID").toString()))
								 continue;

							ExoSocialActivity firstactivity = activityManager.getActivitiesOfSpaceWithListAccess(spaceIdentity).loadOlder(spaceIdentity.getProfile().getCreatedTime(),1).get(0);
							Map<String, String> templateParams = firstactivity.getTemplateParams();

							 if(opp.get(i).getDescription()!=null&&spaceIdentity.getProfile().getProperty("description")!=null&&!spaceIdentity.getProfile().getProperty("description").equals("Not defined"))
							 {
								 if(!opp.get(i).getDescription().equals(spaceIdentity.getProfile().getProperty("description")))
								 {
									 if(templateParams.containsKey(UISalesforceActivityBuilder.DESCRIPTION_PARAM)) {
										 templateParams.put(UISalesforceActivityBuilder.DESCRIPTION_PARAM,opp.get(i).getDescription());
										 paramsUpdated = true;
									 }
									 LOG.info("for descri"+opp.get(i).getName());
									    Profile oppProfile = spaceIdentity.getProfile();
						                oppProfile.setProperty("description", opp.get(i).getDescription());
						                Util.getIdentityManager("portal").saveProfile(oppProfile);
								 /*ExoSocialActivity activity = new ExoSocialActivityImpl();
								// activity.set
								 activity.setTitle("The description of the opportunity has been updated to :"+opp.get(i).getDescription());
									activity.setType(UISalesforceActivity.ACTIVITY_TYPE);
					                activity.setBody("The description of the opportunity has been updated to :"+opp.get(i).getDescription());
					                activityManager.saveActivityNoReturn(spaceIdentity, activity);*/
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
											 if(templateParams.containsKey(UISalesforceActivityBuilder.CLOSEDATE_PARAM)) {
												 templateParams.put(UISalesforceActivityBuilder.CLOSEDATE_PARAM,t.toString());
												 paramsUpdated = true;
											 }
											 LOG.info("for close date"+opp.get(i).getName());
											    Profile oppProfile = spaceIdentity.getProfile();
								                oppProfile.setProperty("CloseDate", t.toString());
								                Util.getIdentityManager("portal").saveProfile(oppProfile);
										 /*ExoSocialActivity activity = new ExoSocialActivityImpl();
										// activity.set
										 activity.setTitle("The close date of the opportunity has been updated to :"+t.toString());
											activity.setType(UIDefaultActivity.ACTIVITY_TYPE);
							                activity.setBody("The close date of the opportunity has been updated to :"+t.toString());
							                activityManager.saveActivityNoReturn(spaceIdentity, activity);*/
										 }
										
									}
							 
							 //LOG.info(opp.get(i).getCloseDate().toString());
							 
							 if(spaceIdentity.getProfile().getProperty("stageName")!=null&&!spaceIdentity.getProfile().getProperty("stageName").equals("Not defined")&&opp.get(i).getStageName().toString()!=null)
							 {
								 if(!opp.get(i).getStageName().value().equals(spaceIdentity.getProfile().getProperty("stageName")))
								 {
									 if(templateParams.containsKey(UISalesforceActivityBuilder.STAGE_PARAM)) {
										 templateParams.put(UISalesforceActivityBuilder.STAGE_PARAM,opp.get(i) .getStageName().value().toString());
										 paramsUpdated = true;
									 }
									 LOG.info("for stage "+opp.get(i).getName());
									    Profile oppProfile = spaceIdentity.getProfile();
						                oppProfile.setProperty("stageName",opp.get(i) .getStageName().value().toString());
						                Util.getIdentityManager("portal").saveProfile(oppProfile);
								 /*ExoSocialActivity activity = new ExoSocialActivityImpl();
								// activity.set
								 activity.setTitle("The stage of the opportunity has been updated to :"+opp.get(i).getStageName().value());
									activity.setType(UIDefaultActivity.ACTIVITY_TYPE);
					                activity.setBody("The stage of the opportunity has been updated to :"+opp.get(i).getStageName().value());
					                activityManager.saveActivityNoReturn(spaceIdentity, activity);*/
								 }
								 
							 }
							 
							 
							 
							 if(spaceIdentity.getProfile().getProperty("ammount")!=null && !spaceIdentity.getProfile().getProperty("ammount").equals("Not defined")&&opp.get(i).getAmount()!=null)
							 { 
								 if(!opp.get(i).getAmount().toString().equals(spaceIdentity.getProfile().getProperty("ammount")))
								 {
									 if(templateParams.containsKey(UISalesforceActivityBuilder.AMOUNT_PARAM)) {
										 templateParams.put(UISalesforceActivityBuilder.AMOUNT_PARAM,opp.get(i) .getAmount().toString());
										 paramsUpdated = true;
									 }
								 /*ExoSocialActivity activity = new ExoSocialActivityImpl();
								// activity.set
								 activity.setTitle("The Amount of the opportunity has been updated to :"+opp.get(i).getAmount());
									activity.setType(UIDefaultActivity.ACTIVITY_TYPE);
					                activity.setBody("The Amount of the opportunity has been updated to :"+opp.get(i).getAmount());
					                activityManager.saveActivityNoReturn(spaceIdentity, activity);*/
								 }
							 }
							if(paramsUpdated) {
								firstactivity.setTemplateParams(templateParams);
								activityManager.updateActivity(firstactivity);
								paramsUpdated = false;
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
			
		}
		finally {
			notStarted.set(true);
		}
		
		
	}

}
