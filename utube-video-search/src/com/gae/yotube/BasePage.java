package com.gae.yotube;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.protocol.http.WebResponse;
import org.wicketstuff.jquery.JQueryBehavior;

import com.gae.yotube.util.ServiceUtil;

public class BasePage extends WebPage {
	
	
	public BasePage() {
		add(new JQueryBehavior());
		add(new AjaxLink<Void>("upload"){
			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(FileUploadPage.class);
				setRedirect(true);
			}
		});
		add(new AjaxLink<Void>("xmlupload"){
			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(XmlFileUploadPage.class);
				setRedirect(true);
			}
		});
		add(new AjaxLink<Void>("home"){
			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(HomePage.class);
				setRedirect(true);
			}
		});

	}
	
	
	protected void setHeaders(WebResponse response) 
    { 
            response.setHeader("Pragma", "no-cache"); 
            response.setHeader("Cache-Control", "no-cache, max-age=0, must-revalidate, no-store"); 
    }
	
	public static class JqueryYtubeBehaviour  extends  JQueryBehavior{
		 public static final CompressedResourceReference JQUERY_UTUBE_JS = new CompressedResourceReference(JQueryBehavior.class, "jquery.youtubin.js");
		 @Override
		public void renderHead(IHeaderResponse response) {
			super.renderHead(response);
			response.renderJavascriptReference(JQUERY_UTUBE_JS);
			response.renderJavascript(ServiceUtil.GROOVY_XML.getScript("youtubin"), "youtubin");
		 }
	}
}
