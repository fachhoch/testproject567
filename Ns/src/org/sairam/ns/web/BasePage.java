package org.sairam.ns.web;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebResponse;
import org.wicketstuff.jquery.JQueryBehavior;

public class BasePage extends WebPage {
	
	
	public BasePage() {
		add(new JQueryBehavior());
	}
	
	
	protected void setHeaders(WebResponse response) 
    { 
            response.setHeader("Pragma", "no-cache"); 
            response.setHeader("Cache-Control", "no-cache, max-age=0, must-revalidate, no-store"); 
    }
}
