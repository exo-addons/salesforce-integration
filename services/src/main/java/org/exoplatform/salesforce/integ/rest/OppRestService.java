package org.exoplatform.salesforce.integ.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.exoplatform.commons.utils.MimeTypeResolver;
import org.exoplatform.commons.utils.PropertyManager;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.salesforce.VariablesUtil;
import org.exoplatform.salesforce.config.ApiProvider;
import org.exoplatform.salesforce.domain.PostActivitiesEntity;
import org.exoplatform.salesforce.integ.component.activity.UISalesforceActivity;
import org.exoplatform.salesforce.integ.component.activity.UISalesforceActivityBuilder;
import org.exoplatform.salesforce.integ.connector.entity.AggregateResult;
import org.exoplatform.salesforce.integ.connector.entity.Attachment;
import org.exoplatform.salesforce.integ.connector.entity.ContentDocumentLink;
import org.exoplatform.salesforce.integ.connector.entity.ContentVersion;
import org.exoplatform.salesforce.integ.connector.entity.Opportunity;
import org.exoplatform.salesforce.integ.connector.storage.api.ConfigurationInfoStorage;
import org.exoplatform.salesforce.integ.util.RequestKeysConstants;
import org.exoplatform.salesforce.integ.util.Utils;
import org.exoplatform.salesforce.service.PostActivitiesService;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.activity.model.ExoSocialActivityImpl;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.SpaceUtils;
import org.exoplatform.social.core.space.impl.DefaultSpaceApplicationHandler;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.social.plugin.doc.UIDocActivity;
import org.exoplatform.social.plugin.doc.UIDocActivityBuilder;
import org.exoplatform.social.service.rest.RestChecker;
import org.exoplatform.social.service.rest.Util;
import org.exoplatform.social.webui.activity.UIDefaultActivity;
import org.json.JSONException;
import org.json.JSONObject;

import com.force.api.ForceApi;
import com.force.api.QueryResult;

@Path("/salesforce")

public class OppRestService implements ResourceContainer,VariablesUtil {
		//TODO: Review all methods within this class
	    private static final Log LOG = ExoLogger.getLogger(OppRestService.class);
	    OrganizationService orgService;
	    RepositoryService repositoryService;
		SpaceService spaceService;
		IdentityManager identityManager;
		ActivityManager activityManager;
		PostActivitiesService postActivitiesService;
		//private DateTime CloseDate;
	    private static final String portalContainerName = "portal";
	    private static final String[] SUPPORTED_FORMATS = new String[]{"json"};

	public OppRestService(OrganizationService orgService, RepositoryService repositoryService, SpaceService spaceService, IdentityManager identityManager, ActivityManager activityManager, PostActivitiesService postActivitiesService) {
		this.orgService = orgService;
		this.repositoryService = repositoryService;
		this.spaceService = spaceService;
		this.identityManager = identityManager;
		this.activityManager = activityManager;
		this.postActivitiesService = postActivitiesService;
	}

	@SuppressWarnings("deprecation")
	@GET
	@Path("create/{oppID}")
	public Response createOpp(
			@Context HttpServletRequest request,
			@PathParam("oppID") String oppID) throws Exception {

		String oppName = null;
		String ammount = null;
		String description = null;
		String stageName = null;
		String closeDate = null;
		String permId=null;
	    	Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
	            String accesstoken=null;
	            String instance_url=null;
	    	 ExoSocialActivity activity = new ExoSocialActivityImpl();
            if (sourceIdentity == null)
				return Response.status(Response.Status.UNAUTHORIZED).build();
            Cookie tk_cookie=  Utils.getCookie(request, "tk_ck_");
            Cookie inst_cookie = Utils.getCookie(request, "inst_ck_");
            if(tk_cookie!=null)
            accesstoken=tk_cookie.getValue();
            if(inst_cookie!=null)
            	instance_url=inst_cookie.getValue();
            if(accesstoken!=null&&instance_url!=null){
            //init the api here as exo user identity not available in oauth servlet otherwise user action on salesforce are made through portal context
            	ForceApi api = ApiProvider.intApi(request.getSession(),accesstoken, instance_url,sourceIdentity.getRemoteId());
            	if(api==null)
            		 return Response.seeOther(URI.create(Util.getBaseUrl() + "/portal")).build();
            		
				//ConversationState.getCurrent().getIdentity().getUserId();
				Opportunity opp = null;
				try {
					opp = api.getSObject("Opportunity", oppID).as(Opportunity.class);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
					return Response.serverError().status(404).build();
				}
				oppName = opp.getName();
				if(spaceService.getSpaceByPrettyName(SpaceUtils.cleanString(oppName)) != null) {
					return Response.seeOther(URI.create(Util.getBaseUrl() + "/"+Utils.getSpaceUrl(SpaceUtils.cleanString(oppName)))).build();
				}
				 String qq="SELECT COUNT(Id) FROM OpportunityFeed where ParentId="+ "\'"+oppID+"\'";
					QueryResult<AggregateResult> totalFeed=api.query(qq, AggregateResult.class);
					//count the total feed at create deal room time on the opp 
					String TotalOppFeed=totalFeed.getRecords().get(0).getexpr0();
					//permId is the permanent id of the opportunity will be used to check update 
				    permId=opp.getId();
					ammount = (opp.getAmount()!=null) ?opp.getAmount().toString():"Not defined";
					description = (opp.getDescription()!=null)? opp.getDescription():"Not defined";
					closeDate= (opp.getCloseDate()!=null)? opp.getCloseDate().toString():"Not defined";
					if(closeDate!=null){
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date d = simpleDateFormat.parse(closeDate);
					closeDate = simpleDateFormat.format(d);
					}
					else
					{
					closeDate ="Not defined";
					}
					stageName = (opp.getStageName()!=null)?opp.getStageName().value().toString():"Not defined";
					
					
				
		

       	 String owner = sourceIdentity.getRemoteId();
            Space project_ = new Space();
            project_.setDisplayName(oppName);
            project_.setPrettyName(oppName);
            project_.setRegistration(Space.OPEN);
            if((description).equals("Not defined"))
			project_.setDescription(oppName);
		    else
			project_.setDescription(description);
            project_.setType(DefaultSpaceApplicationHandler.NAME);
            project_.setVisibility(Space.PRIVATE);
            project_.setRegistration(Space.VALIDATION);
            project_.setPriority(Space.INTERMEDIATE_PRIORITY);
            Space s= spaceService.createSpace(project_, owner);
            createspaceFolder(project_);

			spaceService.addMember(s,"salesforce");

            if (s != null) {
				Identity salesforceIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, "salesforce", false);
            	activity.setUserId(salesforceIdentity.getId());
                Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, s.getPrettyName(), false);
                Profile oppProfile = spaceIdentity.getProfile();
                oppProfile.setProperty("oppID", permId);
                oppProfile.setProperty("opportunityName", oppName);
                oppProfile.setProperty("description", description);
                oppProfile.setProperty("CloseDate", closeDate);
                oppProfile.setProperty("ammount", ammount);
                oppProfile.setProperty("stageName", stageName);
                //store the total feed of the opportunity as property of space profile
                //any change on total means need update in chatter that need to be push to eXo
                oppProfile.setProperty("nbOppFeed", TotalOppFeed);

				String oppUrl = instance_url + "/" + oppID;
				String oppLink = "<a href=\"" + oppUrl + "\">" + oppName + "</a>";
				activity.setTitle(oppLink);
				activity.setType(UISalesforceActivity.ACTIVITY_TYPE);
				Map<String, String> templateParams = new HashMap<String, String>();
				templateParams.put(UISalesforceActivityBuilder.DESCRIPTION_PARAM,description);
				templateParams.put(UISalesforceActivityBuilder.STAGE_PARAM,stageName);
				templateParams.put(UISalesforceActivityBuilder.CLOSEDATE_PARAM,closeDate);
				templateParams.put(UISalesforceActivityBuilder.AMOUNT_PARAM,ammount);
				activity.setTemplateParams(templateParams);
                activity.setBody("The opportunity: " +oppName +" descp: "+description+" has a stage :stageName" );
                activityManager.saveActivityNoReturn(spaceIdentity, activity);

				oppProfile.setProperty("firstsalesforceactivity",activity.getId());
				Util.getIdentityManager(portalContainerName).saveProfile(oppProfile);

            } 
		}
       
            return Response.seeOther(URI.create(Util.getBaseUrl() + "/portal")).build();
	    }

		private void createspaceFolder(Space s) {
			 try {
				Session session = repositoryService.getCurrentRepository().getSystemSession("collaboration");
				Node rootNode = session.getRootNode();
				Node groupFolder = rootNode.getNode("Groups" + s.getGroupId() + "/Documents");
				Node salesForceFolder = groupFolder.addNode("salesForceDocuments", "nt:folder");
				salesForceFolder.getSession().save();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				LOG.error(e.getMessage(), e);
			}
			
		}

	@GET
	@RolesAllowed("users")
	@Path("getopportunity/{oppName}")
	public Response getOpportunityData(
			@Context HttpServletRequest request,
			@PathParam("oppName") String oppName) throws JSONException {
		Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
		if (sourceIdentity == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		Space dealroom = spaceService.getSpaceByPrettyName(oppName);
		if(dealroom != null) {
			Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, dealroom.getPrettyName(), false);
			Profile oppProfile = spaceIdentity.getProfile();
			if(oppProfile != null && oppProfile.getProperty("oppID") != null) {
				JSONObject json = new JSONObject();
				json.put("opportunityName", oppProfile.getProperty("opportunityName").toString());
				json.put("description", oppProfile.getProperty("description").toString());
				json.put("CloseDate", oppProfile.getProperty("CloseDate").toString());
				json.put("amount", oppProfile.getProperty("ammount").toString());
				json.put("stageName", oppProfile.getProperty("stageName").toString());
				return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
			}
		}
		return Response.status(Response.Status.NO_CONTENT).build();
	}

		@GET
		@Path("addupdatecomment/{oppName}")
		public Response addUpdateComment(
				@Context HttpServletRequest request,
				@PathParam("oppName") String oppName,
				@QueryParam("newName") String newName,
				@QueryParam("oldstageName") String oldstageName,
				@QueryParam("newstageName") String newstageName,
				@QueryParam("oldamount") String oldamount,
				@QueryParam("newamount") String newamount,
				@QueryParam("oldclosedate") String oldclosedate,
				@QueryParam("newclosedate") String newclosedate,
				@QueryParam("olddescription") String olddeschhhription,
				@QueryParam("newdescription") String newdescription) throws Exception {
			MediaType mediaType = RestChecker.checkSupportedFormat("json", SUPPORTED_FORMATS);
			
			String authcriptedcode=null;
			byte[] checkSring =null;
			String authorization=null;
			 authorization = request.getHeader("Authorization");
			if (authorization == null || !authorization.startsWith("Basic"))
				return Response.status(Response.Status.UNAUTHORIZED).build();
	
			try {
	
				authcriptedcode = authorization.substring("Basic".length()).trim();
	
				checkSring = Utils.decryptBase64EncodedWithManagedIV(
						authcriptedcode, "mRMjHmlC1C+1L/Dkz8EJuw==");
			} catch (Exception e) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
			if (checkSring == null
					|| !(new String(checkSring, "UTF-8"))
							.equals(PropertyManager.getProperty(SF_SECURITY_SID))) {
	
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}


			Space space = spaceService.getSpaceByDisplayName(oppName);
			if(space == null) {
				return Response.ok("Opportunity Not Found", mediaType).build();
			}
			if(oppName != null && newName != null && !oppName.equals(newName)) {
				createcomment(space.getPrettyName(), "Name", oppName, newName);
				//TODO Rename Space after renaming Opportunity
				/*
				UserNode renamedNode = renamePageNode(newName, space);
				spaceService.renameSpace(space, newName);
				// rename group label
				OrganizationService organizationService = (OrganizationService) ExoContainerContext.getCurrentContainer().
						getComponentInstanceOfType(OrganizationService.class);
				GroupHandler groupHandler = organizationService.getGroupHandler();
				Group group = groupHandler.findGroupById(space.getGroupId());
				group.setLabel(space.getDisplayName());
				groupHandler.saveGroup(group, true);
				PortalRequestContext prContext = org.exoplatform.portal.webui.util.Util.getPortalRequestContext();
				prContext.createURL(NodeURL.TYPE).setNode(renamedNode);
				*/
			}
			if(oldstageName != null && newstageName != null && !oldstageName.equals(newstageName)) {
				createcomment(space.getPrettyName(), "Stage Name", oldstageName, newstageName);
			}
			if(oldamount != null && newamount != null && !oldamount.equals(newamount)) {
				createcomment(space.getPrettyName(), "Amount", oldamount, newamount);
			}
			if(oldclosedate != null && newclosedate != null && !oldclosedate.equals(newclosedate)) {
				createcomment(space.getPrettyName(), "Close Date", oldclosedate, newclosedate);
			}
			return Response.ok("Comment Added", mediaType).build();
		}


		@GET
		@Path("chatterpost/{oppID}")
		public Response chatterNewPost(
				@Context HttpServletRequest request,
				@PathParam("oppID") String oppID,
				@QueryParam("oppName") String oppName,
				@QueryParam("poster") String poster,
				@QueryParam("postType") String postType,
				@QueryParam("postId") String postId,
				@QueryParam("textPost") String textPost,
				@QueryParam("mentionned") String mentionned,
				@QueryParam("contentPost") String contentPost,
				@QueryParam("contentPostText") String contentPostText,
				@QueryParam("posterId") String posterId,
				@QueryParam("mentionnedIds") String mentionnedIds,
				@QueryParam("baseUrl") String baseUrl,
				@QueryParam("postedlink") String postedlink) throws Exception {
			MediaType mediaType = RestChecker.checkSupportedFormat("json", SUPPORTED_FORMATS);
			String[] supportedType = new String[] {"TextPost","LinkPost"};
			oppName=URLDecoder.decode(oppName, "UTF-8");
			poster=URLDecoder.decode(poster, "UTF-8");
			textPost =(textPost!=null)? URLDecoder.decode(textPost, "UTF-8"):null;
			contentPost=(contentPost!=null)? URLDecoder.decode(contentPost, "UTF-8"):null;
			mentionned=(mentionned!=null)? URLDecoder.decode(mentionned, "UTF-8"):null;
			String authcriptedcode=null;
			byte[] checkSring =null;
			String authorization=null;
			 authorization = request.getHeader("Authorization");
			if (authorization == null || !authorization.startsWith("Basic"))
				return Response.status(Response.Status.UNAUTHORIZED).build();
	
			try {
	
				authcriptedcode = authorization.substring("Basic".length()).trim();
	
				checkSring = Utils.decryptBase64EncodedWithManagedIV(
						authcriptedcode, "mRMjHmlC1C+1L/Dkz8EJuw==");
			} catch (Exception e) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
			if (checkSring == null
					|| !(new String(checkSring, "UTF-8"))
						.equals(PropertyManager.getProperty(SF_SECURITY_SID))) {
	
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}


			Space space = spaceService.getSpaceByDisplayName(URLDecoder.decode(oppName, "UTF-8"));
			String profile_page = baseUrl+"/_ui/core/userprofile/UserProfilePage?u=";
			String poster_link =profile_page+posterId;
			if (space == null) {
				return Response.status(Response.Status.NOT_FOUND).entity("Opportunity not found").build();
			}
			
			if (postType==null||!Arrays.asList(supportedType).contains(postType)) {
				return Response.status(Response.Status.OK).entity("").build();
			}

			Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, space.getPrettyName(), false);
			Identity salesforceIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, "salesforce", false);
			ExoSocialActivity activity = new ExoSocialActivityImpl();
			 activity.setUserId(salesforceIdentity.getId());
			String activitybody="<a href=\""
					+poster_link+ "\">"+poster+"</a>";
			if(postType.equals("TextPost")){
				if(mentionned!=null){
					String[] mentionnedList = mentionned.split(",");
					String[] mentionnedListIds = mentionnedIds.split(",");
					for(int i=0 ; i<mentionnedList.length;i++){
						String montionned_link=profile_page+mentionnedListIds[i];
						textPost =StringUtils.replace(textPost, "@"+mentionnedList[i], "<a href=\""
							+	montionned_link+ "\">"+"@"+mentionnedList[i]+"</a>",1);
						
					}
				
				}
				activitybody+=" posted new message: "+textPost;
			}
			else if(postType.equals("LinkPost")){
				
				activitybody+=" posted new link : "+postedlink;
			}
			
			activity.setTitle(activitybody);
             activityManager.saveActivityNoReturn(spaceIdentity, activity);
           
			try {
				postActivitiesService.createEntity(new PostActivitiesEntity(activity.getId(), postId));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOG.error(e.getMessage(), e);
				 return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An internal error has occured").build();
			}
             return Response.ok("post created", mediaType).build();

		}
		
		@GET
		@Path("chattercomments/{oppID}")
		public Response chatterNewComment(
				@Context HttpServletRequest request,
				@PathParam("oppID") String oppID,
				@QueryParam("oppName") String oppName,
				@QueryParam("postID") String postID,
				@QueryParam("poster") String poster,
				@QueryParam("commentPost") String commentPost,
				@QueryParam("posterId") String posterId,
				@QueryParam("mentionnedIds") String mentionnedIds,
				@QueryParam("baseUrl") String baseUrl,
				@QueryParam("mentionned") String mentionned) throws Exception {
			MediaType mediaType = RestChecker.checkSupportedFormat("json", SUPPORTED_FORMATS);
			String authcriptedcode=null;
			byte[] checkSring =null;
			String authorization=null;
			 authorization = request.getHeader("Authorization");
			if (authorization == null || !authorization.startsWith("Basic"))
				return Response.status(Response.Status.UNAUTHORIZED).build();
	
			try {
	
				authcriptedcode = authorization.substring("Basic".length()).trim();
	
				checkSring = Utils.decryptBase64EncodedWithManagedIV(
						authcriptedcode, "mRMjHmlC1C+1L/Dkz8EJuw==");
			} catch (Exception e) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
			if (checkSring == null
					|| !(new String(checkSring, "UTF-8"))
						.equals(PropertyManager.getProperty(SF_SECURITY_SID))) {
	
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}

			try {
				oppName = URLDecoder.decode(oppName, "UTF-8");
				poster = URLDecoder.decode(poster, "UTF-8");
				commentPost = (commentPost != null) ? URLDecoder.decode(commentPost, "UTF-8") : null;
				mentionned = (mentionned != null) ? URLDecoder.decode(mentionned, "UTF-8") : null;
				Space space = spaceService.getSpaceByDisplayName(URLDecoder.decode(oppName, "UTF-8"));
				if (space == null) {
					return Response.status(Response.Status.NOT_FOUND).entity("Opportunity not found").build();
				}

				String profile_page = baseUrl + RequestKeysConstants.SF_USER__PROFILE_PAGE;
				String poster_link = profile_page + posterId;
				Identity salesforceIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, "salesforce", false);
				PostActivitiesEntity chatterPost = postActivitiesService.findEntityByPostId(postID);
				if (chatterPost == null) {
					return Response.status(Response.Status.NOT_FOUND).entity("no related post found").build();
				}
				//chatterPost.getPostActivityId()

				ExoSocialActivity activityPost = activityManager.getActivity(chatterPost.getActivityId());
				if (activityPost == null) {
					return Response.status(Response.Status.NOT_FOUND).entity("no related post found").build();
				}
				// activity.setUserId(salesforceIdentity.getId());
				//activity.setType("");

				String activitybody = "<a href=\""
						+ poster_link + "\">" + poster + "</a>";
				//if(postType.equals("TextPost")){
				if (mentionned != null) {
					String[] mentionnedList = mentionned.split(",");
					String[] mentionnedListIds = mentionnedIds.split(",");
					for (int i = 0; i < mentionnedList.length; i++) {
						String montionned_link = profile_page + mentionnedListIds[i];
						commentPost = StringUtils.replace(commentPost, "@" + mentionnedList[i], "<a href=\""
								+ montionned_link + "\">" + "@" + mentionnedList[i] + "</a>", 1);
						//StringUtils.replace(text, searchString, replacement, max)e
						//StringUtils.replaceChars(str, searchChar, replaceChar)
						LOG.info("mentionned User:" + mentionnedList[i]);

					}

					//textPost=StringUtils.substringAfter("@"+mentionned, textpost);
					//textPost= StringUtils.substringAfter(textPost, "@"+mentionned);
					//activitybody+=textPost;


				}
				activitybody += " posted new comment: " + commentPost;
				//}

				ExoSocialActivity newcomment = new ExoSocialActivityImpl();
				newcomment.setType(UIDefaultActivity.ACTIVITY_TYPE);
				newcomment.setUserId(salesforceIdentity.getId());
				newcomment.setTitle(activitybody);
				newcomment.setBody(activitybody);
				activityManager.saveComment(activityPost, newcomment);
				return Response.ok("comment created", mediaType).build();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An internal error has occured").build();
			}

		}

	private void createcomment(String spaceName, String fieldName, String oldValue, String newValue) {
		Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, spaceName, false);
		Profile oppProfile = spaceIdentity.getProfile();
		Identity salesforceIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, "salesforce", false);
		ExoSocialActivity firstactivity = activityManager.getActivity(oppProfile.getProperty("firstsalesforceactivity").toString());
		Map<String, String> templateParams = firstactivity.getTemplateParams();
		boolean changed = true;
		switch (fieldName) {
			case "Name" :
				firstactivity.setTitle(newValue);
				break;
			case "Description" :
				if(templateParams.containsKey(UISalesforceActivityBuilder.DESCRIPTION_PARAM)) {
					templateParams.put(UISalesforceActivityBuilder.DESCRIPTION_PARAM, newValue);
					oppProfile.setProperty("description", newValue);
				}
				break;
			case "Stage Name" :
				if(templateParams.containsKey(UISalesforceActivityBuilder.STAGE_PARAM)) {
					templateParams.put(UISalesforceActivityBuilder.STAGE_PARAM, newValue);
					oppProfile.setProperty("stageName", newValue);
				}
				break;
			case "Amount" :
				if(templateParams.containsKey(UISalesforceActivityBuilder.AMOUNT_PARAM)) {
					templateParams.put(UISalesforceActivityBuilder.AMOUNT_PARAM, newValue);
					oppProfile.setProperty("ammount", newValue);
				}
				break;
			case "Close Date" :
				if(templateParams.containsKey(UISalesforceActivityBuilder.CLOSEDATE_PARAM)) {
					templateParams.put(UISalesforceActivityBuilder.CLOSEDATE_PARAM, newValue);
					oppProfile.setProperty("CloseDate", newValue);
				}
				break;
			default:
				changed = false;
				break;
		}

		try {
			if(changed) {
				firstactivity.setTemplateParams(templateParams);
				activityManager.updateActivity(firstactivity);
				identityManager.updateProfile(oppProfile);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		ExoSocialActivity newcomment = new ExoSocialActivityImpl();
		newcomment.setType(UIDefaultActivity.ACTIVITY_TYPE);
		newcomment.setUserId(salesforceIdentity.getId());
		newcomment.setTitle(fieldName+" changed from "+oldValue+" to "+newValue);
		newcomment.setBody(fieldName+" changed from "+oldValue+" to "+newValue);
		activityManager.saveComment(firstactivity,newcomment);
	}

	    @POST
	    @Path("config")
	    @Consumes({MediaType.APPLICATION_JSON})
	    public Response createConfig(@Context HttpServletRequest request,@Context HttpServletResponse response,@Context UriInfo uriInfo,
	                                 Map<String,String> conf) throws Exception {

	        Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
	        MediaType mediaType = RestChecker.checkSupportedFormat("json", SUPPORTED_FORMATS);
	        ConfigurationInfoStorage configurationInfoStorage = (ConfigurationInfoStorage) PortalContainer.getInstance().getComponentInstanceOfType(ConfigurationInfoStorage.class);
	        try {
	            if(sourceIdentity == null) {
	                return Response.status(Response.Status.UNAUTHORIZED).build();
	            }
	            String clientId=conf.get("clientId");
	            String clientSecret =conf.get("clientSecret");
	            String redirectUri=conf.get("redirectUri");
				String sfSecuritySID=conf.get("sfSecuritySID");

	            configurationInfoStorage.saveConfigurationInfo(clientId, clientSecret, redirectUri);
	            PropertyManager.setProperty(CLIENT_ID, clientId);
				PropertyManager.setProperty(CLIENT_SECRET,clientSecret);
				PropertyManager.setProperty(REDIRECT_URI, redirectUri);
				PropertyManager.setProperty(SF_SECURITY_SID, sfSecuritySID);

	            JSONObject jsonGlobal = new JSONObject();
	            jsonGlobal.put("message"," conf saved");
	            return Response.ok(jsonGlobal.toString(), mediaType).build();
	        } catch (Exception e) {
	            LOG.error(e.getMessage(), e);
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An internal error has occured").build();
	        }
	    }
	    
	    @GET
	    @Path("getconfig")
	    @Consumes({MediaType.APPLICATION_JSON})
	    public Response getConfig(@Context HttpServletRequest request,@Context HttpServletResponse response,@Context UriInfo uriInfo) throws Exception {
	        Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
	        try {
	            if(sourceIdentity == null) {
	                return Response.status(Response.Status.UNAUTHORIZED).build();
	            }
	            
	            JSONObject json = new JSONObject();



	            String clientId= PropertyManager.getProperty(CLIENT_ID);
	            String clientSecret = PropertyManager.getProperty(CLIENT_SECRET);
	            String redirectUri = PropertyManager.getProperty(REDIRECT_URI) ;
	            String sfSecuritySID = PropertyManager.getProperty(SF_SECURITY_SID) ;
	            json.put("clientId",clientId);
	            json.put("clientSecret",clientSecret);
	            json.put("redirectUri",redirectUri);
				json.put("sfSecuritySID",sfSecuritySID);

	           
	            return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
	        } catch (Exception e) {
	            LOG.error(e.getMessage(), e);
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An internal error has occured").build();
	        }
	    }
	    
	    /**
	     * get document contents of specific opportunity From SF  and store them into exo space drive
	     */  
	    
	    @GET
	    @Path("get/contentdocuments/{oppName}")
	    public Response syncDoc(@Context HttpServletRequest request,
	    		 @PathParam("oppName")String oppName,
	    		 @QueryParam("workspaceName") String workspaceName,
	    		 @QueryParam("nodepath") String nodepath) throws Exception {
	    	Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
	    	 boolean firstCall=false;
	    	 String accesstoken=null;
	            String instance_url=null;
	    	 workspaceName=(workspaceName==null)?"collaboration":workspaceName;
            if (sourceIdentity == null)
				return Response.status(Response.Status.UNAUTHORIZED).build();
            //if user token expired or user try to sync without going by process oauth will replace cookies check
            if(!ApiProvider.hasValidApiSession(sourceIdentity.getRemoteId()))
            	return Response.temporaryRedirect(URI.create("/salesforce-extension/oauth?initialURI=/portal/private/rest/salesforce"
						+ "/get/contentdocuments/"+oppName+"?"+"nodepath="+nodepath+"&amp;workspaceName="+workspaceName)).build();

            
            accesstoken=UserService.userMap.get(sourceIdentity.getRemoteId()).getAccesstoken();
            instance_url=UserService.userMap.get(sourceIdentity.getRemoteId()).getInstanceUrl();
            
			if(accesstoken==null||instance_url==null){
				request.getSession().setAttribute("oppName", oppName);
				request.getSession().setAttribute("nodepath", nodepath);
				request.getSession().setAttribute("workspaceName", workspaceName);
				return Response.temporaryRedirect(URI.create("/salesforce-extension/oauth?initialURI=/portal/private/rest/salesforce"
						+ "/get/contentdocuments/"+oppName+"?"+"nodepath="+nodepath+"&amp;workspaceName="+workspaceName)).build();
			}

			if(accesstoken!=null&&instance_url!=null){
				
				if(request.getSession().getAttribute(oppName)!=null)
				{
					oppName = (String) (request.getSession().getAttribute("oppName"));
					request.getSession().removeAttribute(oppName);
				}
				if(request.getSession().getAttribute(nodepath)!=null)
				{
					nodepath = (String) (request.getSession().getAttribute("nodepath"));
					request.getSession().removeAttribute(nodepath);
				}
				if(request.getSession().getAttribute(workspaceName)!=null)
				{
					workspaceName = (String) (request.getSession().getAttribute("workspaceName"));
					request.getSession().removeAttribute(workspaceName);
				}
			Space opp=spaceService.getSpaceByPrettyName(oppName);
			if(opp!=null){
				List<String> oppDocID = new ArrayList<String>();
				ForceApi api = ApiProvider.intApi(request.getSession(),accesstoken, instance_url,sourceIdentity.getRemoteId());
				//SELECT Id, ContentDocumentId FROM ContentDocumentLink WHERE LinkedEntityId = '00624000003onYq'
				 Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, opp.getPrettyName(), false);
				 String oppID =spaceIdentity.getProfile().getProperty("oppID").toString();
				//try to get the opportunity from the stored id
				
			
				String qq="SELECT Id, ContentDocumentId  FROM ContentDocumentLink where LinkedEntityId="+ "\'"+oppID+"\' LIMIT 100";
				//SELECT Id, ParentId, Name, ContentType, Body FROM Attachment where ParentId='006240000064jn0'
				String qqAttachement="SELECT Id, ParentId, Name, ContentType, Body FROM Attachment where ParentId="+ "\'"+oppID+"\' LIMIT 100";
				QueryResult<Attachment> queryAttachement=api.query(qqAttachement, Attachment.class);
				QueryResult<ContentDocumentLink> queryDocID=api.query(qq, ContentDocumentLink.class);
				if(queryAttachement.getTotalSize()>0)
					importAttachement(queryAttachement,accesstoken,nodepath,api,instance_url,workspaceName,oppName);
				if(queryDocID.getTotalSize()>0){
					List<ContentDocumentLink> contentsLink = queryDocID.getRecords();
					Iterator<ContentDocumentLink> contentsLinkIt = contentsLink.iterator();
					while (contentsLinkIt.hasNext()) {
						String id =contentsLinkIt.next().getContentDocumentId();
						oppDocID.add(id);
						LOG.info("content id--->:"+id);
					}
					 nodepath = StringUtils.substringAfter(nodepath, "/");
					for (int i = 0; i < oppDocID.size(); i++) {
						String contentid=oppDocID.get(i);
						String qq2="SELECT  PathOnClient, FileType, VersionData  FROM ContentVersion where ContentDocumentId="+ "\'"+contentid+"\' LIMIT 1";
						
						QueryResult<ContentVersion> qdoc=api.query(qq2, ContentVersion.class);
						if(qdoc.getTotalSize()>0){
							LOG.info(qdoc.getRecords().get(0).getVersionDataUrl());
							HttpClient	httpclient1= new HttpClient();
							String VD=qdoc.getRecords().get(0).getVersionDataUrl();
							String path=qdoc.getRecords().get(0).getPathOnClient();
							GetMethod get = new GetMethod(instance_url+ VD);
							get.setRequestHeader("Authorization", "OAuth " + accesstoken);
							httpclient1.executeMethod(get);
							if (get.getStatusCode() == HttpStatus.SC_OK) {
                                 // download resource of the opportunity to local 
								// will be stored in eXo JCR(should create specific drive or folder for SF content) in next commits
 								 byte[] bodyByte =	get.getResponseBody();
 								SessionProvider sessionProvider = SessionProvider.createSystemProvider();
 								String workspace = (workspaceName!=null) ? workspaceName : "collaboration"; 				
 								Session session = sessionProvider.getSession(workspace,
 					                    repositoryService.getCurrentRepository());
 								
 								try {
									
									///Groups/spaces/united_oil_office_portable_generators/Documents/united_oil_office_portable_generators/xxxx.png
									

										Node rootNode = session.getRootNode().getNode(nodepath);
									 // Node file = rootNode.addNode("file", "nt:file");
									 if (!rootNode.hasNode(oppName)) {
									    rootNode.addNode(oppName, "nt:folder");
									   rootNode.save();
									}

									//check if the doc already imported from SLF to same path
									Node oppNode = rootNode.getNode(oppName);
									Node oppIDs = null;
									if(!oppNode.hasNode("oppIDs"))
									{
										oppIDs =oppNode.addNode("oppIDs","nt:folder");
									
										oppIDs.canAddMixin("exo:hiddenable");
										oppIDs.addMixin("exo:hiddenable");
										oppNode.save();
										
										
									}
									oppIDs =oppNode.getNode("oppIDs");
									
										if(oppIDs.getNodes().getSize()==0){
											oppIDs.addNode(contentid,"nt:folder");
											oppIDs.save();
											firstCall=true;
										}
										
										//dirty quick check , will be replaced
										
										 NodeIterator nodeIter = oppIDs.getNodes() ;
										 while(nodeIter.hasNext()) {
											 Node id = nodeIter.nextNode() ;
											 if(!id.getName().equals(contentid)||firstCall){
													MimeTypeResolver resolver = new MimeTypeResolver();
													String type = resolver.getMimeType(path);

													Node fileNode = null;
													 if(!oppNode.hasNode(path)){
													fileNode = oppNode.addNode(path, "nt:file");
													
													Node jcrContent = fileNode.addNode("jcr:content", "nt:resource");
													jcrContent.setProperty("jcr:data",new ByteArrayInputStream(bodyByte));
													jcrContent.setProperty("jcr:lastModified",Calendar.getInstance());
													jcrContent.setProperty("jcr:encoding", "UTF-8");
													jcrContent.setProperty("jcr:mimeType", type);
													 }
													 if(!firstCall&&!oppIDs.hasNode(contentid))
													 {
													    oppIDs.addNode(contentid,"nt:folder");
													    oppIDs.save();
													 }
													 firstCall=false;
													oppNode.save();
													session.save();
												 }
												 
											 }
										
									

							         
	
									session.save();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									LOG.error(e.getMessage(), e);
								}

							}

						}
					}

				}

			}
				

		/*
                activity.setTitle("");
   
                activity.setType("Salesforce_Activity");
                activity.setBody("The opportunity" );
                activityManager.saveActivityNoReturn( activity);
                */
            } 

       
           // portal/g/:spaces:united_oil_office_portable_generators/united_oil_office_portable_generators
            return Response.seeOther(URI.create(Util.getBaseUrl() + "/"+Utils.getSpaceUrl(oppName)+"/documents")).build();
           // return Response.ok("Created").build();
	    }
	    
	    
	    private void importAttachement(
				QueryResult<Attachment> queryAttachement, String accesstoken, String nodepath, ForceApi api, String instance_url, String workspaceName, String oppName) {

			List<Attachment> oppAttachement = queryAttachement.getRecords();
			Iterator<Attachment> attachementIt = oppAttachement.iterator();
			List<String> oppAttachementID = new ArrayList<String>();
			
			 boolean firstCall=false;
			while (attachementIt.hasNext()) {
				String id =attachementIt.next().getId();
				oppAttachementID.add(id);
				LOG.info("attachement id--->:"+id);
			}
			 nodepath = StringUtils.substringAfter(nodepath, "/");
			for (int i = 0; i < oppAttachementID.size(); i++) {

				if(queryAttachement.getTotalSize()>0){
					LOG.info(queryAttachement.getRecords().get(0).getBodyUrl());
					HttpClient	httpclient1= new HttpClient();
					String VD=queryAttachement.getRecords().get(0).getBodyUrl();
					String path=queryAttachement.getRecords().get(0).getName();
					GetMethod get = new GetMethod(instance_url+ VD);
					get.setRequestHeader("Authorization", "OAuth " + accesstoken);
					try {
						httpclient1.executeMethod(get);
					} catch (HttpException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (get.getStatusCode() == HttpStatus.SC_OK) {
							 byte[] bodyByte = null;
							try {
								bodyByte = get.getResponseBody();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							SessionProvider sessionProvider = SessionProvider.createSystemProvider();
							String workspace = (workspaceName!=null) ? workspaceName : "collaboration"; 				
							Session session = null;
							try {
								session = sessionProvider.getSession(workspace,
								        repositoryService.getCurrentRepository());
							} catch (RepositoryException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							try {
							
							///Groups/spaces/united_oil_office_portable_generators/Documents/united_oil_office_portable_generators/xxxx.png
							

								Node rootNode = session.getRootNode().getNode(nodepath);
							 // Node file = rootNode.addNode("file", "nt:file");
							 if (!rootNode.hasNode(oppName)) {
							    rootNode.addNode(oppName, "nt:folder");
							   rootNode.save();
							}

							//check if the doc already imported from SLF to same path
							Node oppNode = rootNode.getNode(oppName);
							Node oppIDs = null;
							if(!oppNode.hasNode("oppIDs"))
							{
								oppIDs =oppNode.addNode("oppIDs","nt:folder");
							
								oppIDs.canAddMixin("exo:hiddenable");
								oppIDs.addMixin("exo:hiddenable");
								oppNode.save();
								
								
							}
							oppIDs =oppNode.getNode("oppIDs");
							
								if(oppIDs.getNodes().getSize()==0){
									oppIDs.addNode(oppAttachementID.get(i),"nt:folder");
									oppIDs.save();
									firstCall=true;
								}
								
								//dirty quick check , will be replaced
								
								 NodeIterator nodeIter = oppIDs.getNodes() ;
								 while(nodeIter.hasNext()) {
									 Node id = nodeIter.nextNode() ;
									 if(!id.getName().equals(oppAttachementID.get(i))||firstCall){
											MimeTypeResolver resolver = new MimeTypeResolver();
											String type = resolver.getMimeType(path);

											Node fileNode = null;
											 if(!oppNode.hasNode(path)){
											fileNode = oppNode.addNode(path, "nt:file");
											
											Node jcrContent = fileNode.addNode("jcr:content", "nt:resource");
											jcrContent.setProperty("jcr:data",new ByteArrayInputStream(bodyByte));
											jcrContent.setProperty("jcr:lastModified",Calendar.getInstance());
											jcrContent.setProperty("jcr:encoding", "UTF-8");
											jcrContent.setProperty("jcr:mimeType", type);
											 }
											 if(!firstCall&&!oppIDs.hasNode(oppAttachementID.get(i)))
											 {
											    oppIDs.addNode(oppAttachementID.get(i),"nt:folder");
											    oppIDs.save();
											 }
											 firstCall=false;
											oppNode.save();
											session.save();
										 }
										 
									 }
								
							

					         

							session.save();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							LOG.error(e.getMessage(), e);
						}

					}

				}
			}

		
			
		}

		@POST
	    @Path("/chatterattachments/{oppID}")
	    @Consumes(MediaType.MULTIPART_FORM_DATA)
	    @Produces(MediaType.TEXT_PLAIN)
	    public Response uploadImageOrFile(@Context HttpServletRequest request,@Context HttpServletResponse response,
				@PathParam("oppID") String oppID,
				@QueryParam("oppName") String oppName,
				@QueryParam("poster") String poster,
				@QueryParam("postId") String postId,
				@QueryParam("textPost") String textPost,
				@QueryParam("mentionned") String mentionned,
				@QueryParam("contentPost") String contentPost,
				@QueryParam("contentID") String contentID,
				@QueryParam("posterId") String posterId,
				@QueryParam("mentionnedIds") String mentionnedIds,
				@QueryParam("baseUrl") String baseUrl,
				@QueryParam("contentPostText") String contentPostText) throws RepositoryException, IOException{
            	String REPOSITORY = "repository";
	    	String WORKSPACE = "collaboration";
	    	oppName=URLDecoder.decode(oppName, "UTF-8");
			poster=URLDecoder.decode(poster, "UTF-8");
			textPost =(textPost!=null)? URLDecoder.decode(textPost, "UTF-8"):null;
			contentPost=(contentPost!=null)? URLDecoder.decode(contentPost, "UTF-8"):null;
			mentionned=(mentionned!=null)? URLDecoder.decode(mentionned, "UTF-8"):null;
			Space space = spaceService.getSpaceByDisplayName(URLDecoder.decode(oppName, "UTF-8"));
			String profile_page = baseUrl+RequestKeysConstants.SF_USER__PROFILE_PAGE;
			String poster_link =profile_page+posterId;
			if (space == null) {
				return Response.status(Response.Status.NOT_FOUND).entity("Opportunity not found").build();
			}
			
			Session session = repositoryService.getCurrentRepository().getSystemSession("collaboration");
			Node rootNode = session.getRootNode();
			Node sfFolder =rootNode.getNode("Groups"+space.getGroupId()+"/Documents/salesForceDocuments");
			MimeTypeResolver resolver = new MimeTypeResolver();
			String minetype = resolver.getMimeType(contentPost);
			//use contentID as unique name and title as real name as same content could be attached 
			Node fileNode = null;
			if(!sfFolder.hasNode(contentID)){
				fileNode=  sfFolder.addNode(contentID, "nt:file");
			fileNode.setProperty("exo:title", contentPost);
			Node jcrContent = fileNode.addNode("jcr:content", "nt:resource");
			String data = Utils.getBody(request);
			byte[] encodeBase64 = Base64.decodeBase64(data);
			jcrContent.setProperty("jcr:data",new ByteArrayInputStream(encodeBase64));
			jcrContent.setProperty("jcr:lastModified",Calendar.getInstance());
			jcrContent.setProperty("jcr:encoding", "UTF-8");
			jcrContent.setProperty("jcr:mimeType", minetype);
			sfFolder.save();
			}
			else
				fileNode=sfFolder.getNode(contentID);
			

			Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, space.getPrettyName(), false);
			Identity salesforceIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, "salesforce", false);
			ExoSocialActivity activity = new ExoSocialActivityImpl();

			
			activity.setType(UIDocActivityBuilder.ACTIVITY_TYPE);
			Map<String, String> templateParams = new HashMap<String, String>();
			String activitybody="<a href=\""
					+poster_link+ "\">"+poster+"</a>"
							+ " posted new file "+contentPostText;
			if(mentionned!=null){
				String[] mentionnedList = mentionned.split(",");
				String[] mentionnedListIds = mentionnedIds.split(",");
				for(int i=0 ; i<mentionnedList.length;i++){
					String montionned_link=profile_page+mentionnedListIds[i];
					activitybody =StringUtils.replace(contentPostText, "@"+mentionnedList[i], "<a href=\""
							+montionned_link+ "\">"+"@"+mentionnedList[i]+"</a>",1);
					
				}
			}
			
			
			templateParams.put(UIDocActivity.WORKSPACE,WORKSPACE);
			templateParams.put(UIDocActivity.REPOSITORY,REPOSITORY);
			templateParams.put(UIDocActivity.MESSAGE, activitybody);      
			//templateParams.put(UIDocActivity.DOCLINK,"/portal/rest/jcr/repository/collaboration/Users/t___/th___/tho___/thomas/Private/Documents/samir.pdf, DOCNAME=samir.pdf, DOCPATH=/Users/t___/th___/tho___/thomas/Private/Documents/samir.pdf");
			templateParams.put(UIDocActivity.DOCNAME,contentPost);
			templateParams.put(UIDocActivity.DOCUMENT_TITLE,contentPost);
			templateParams.put(UIDocActivity.IS_SYMLINK,"false");
			templateParams.put(UIDocActivity.MIME_TYPE, minetype);
			templateParams.put(UIDocActivity.DOCPATH,fileNode.getPath());
			activity.setTemplateParams(templateParams);
           
			
			
			activity.setUserId(salesforceIdentity.getId());
			activitybody+=" posted new file : "+"<a href=\"#\">"+contentPost+"</a>" +" " +contentPostText;			
			activity.setTitle(activitybody);
			activity.setBody(activitybody);
            activityManager.saveActivityNoReturn(spaceIdentity, activity);
			postActivitiesService.createEntity(new PostActivitiesEntity(activity.getId(), postId));

		return Response.status(200).build();
	}
}
