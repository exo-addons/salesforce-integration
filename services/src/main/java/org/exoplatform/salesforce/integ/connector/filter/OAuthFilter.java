package org.exoplatform.salesforce.integ.connector.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.salesforce.integ.connector.servlet.CustomRequestWrapper;
import org.exoplatform.salesforce.integ.rest.UserService;
import org.exoplatform.salesforce.integ.util.Utils;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.web.filter.Filter;

public class OAuthFilter implements Filter {
	

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        ConversationState state = ConversationState.getCurrent();
        String userId = (state != null) ? state.getIdentity().getUserId() : null;
    	 String uri = httpServletRequest.getRequestURI();
    	Cookie[] cookies = httpServletRequest.getCookies();
    	 if (!uri.contains(":spaces:")) {
    		 filterChain.doFilter(servletRequest, servletResponse);
    	      return;
    	    }
    	 if(httpServletRequest.getParameter("status")!=null){
    	 
    	  //wrapp the request to remove param 
    		 CustomRequestWrapper newRequest = new CustomRequestWrapper(httpServletRequest);
         newRequest.removeParameter("status");
        // newRequest.getParameterMap();
         filterChain.doFilter(newRequest, servletResponse);
         return;
    	 }
    
			//UserService.isInto(userId);
		
				try {
					 String spaceId = uri.split(":spaces:")[1];
					 spaceId = spaceId.split("/", 2)[0];
					if(userId!=null &&!userId.equals("__anonim")&&Utils.iSOpportunitySpace(spaceId)&&!Utils.hasCookies(cookies)){
						  httpServletResponse.sendRedirect("/salesforce-extension/oauth?initialURI="+uri);
						  return;
					    }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
	
		      filterChain.doFilter(servletRequest, servletResponse);
		
	}

}
