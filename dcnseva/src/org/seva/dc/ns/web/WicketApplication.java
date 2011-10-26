package org.seva.dc.ns.web;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;



public class WicketApplication extends WebApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		
		return BasePage.class;
	}
	@Override
    protected void init()
    {
        super.init();
        // for Google App Engine
        getResourceSettings().setResourcePollFrequency(null);
        addComponentInstantiationListener(new SpringComponentInjector(this));        
    }
	
	@Override
    protected ISessionStore newSessionStore()
    {
        return new HttpSessionStore(this);
    }
	
	
	
	
}
