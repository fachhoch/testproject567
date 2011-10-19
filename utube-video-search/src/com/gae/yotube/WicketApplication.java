package com.gae.yotube;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.session.ISessionStore;



public class WicketApplication extends WebApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		
		return HomePage.class;
	}
	@Override
    protected void init()
    {
        super.init();
        // for Google App Engine
        getResourceSettings().setResourcePollFrequency(null);
    }
	
	@Override
    protected ISessionStore newSessionStore()
    {
        return new HttpSessionStore(this);
    }
	
	@Override
	protected WebRequest newWebRequest(final HttpServletRequest servletRequest) {
	    return new GaeSafeServletWebRequest(servletRequest);
	}
	
	
}
