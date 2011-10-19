package com.gae.yotube;

import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.util.lang.Bytes;

import com.gae.yotube.service.HtmlReader;
import com.gae.yotube.service.HtmlReader.LinkDTO;
import com.gae.yotube.service.HtmlReader.VideoDTO;
import com.gae.yotube.service.HtmlReader.VideoSrcDTO;
import com.gae.yotube.service.model.Video;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlFileUploadPage extends BasePage {
		
	
	public XmlFileUploadPage() {
		final FileUploadForm simpleUploadForm = new FileUploadForm("simpleUpload");
        add(simpleUploadForm);
	}
	
	private class FileUploadForm extends Form<Void>
    {
        private FileUploadField fileUploadField;

        /**
         * Construct.
         * 
         * @param name
         *            Component name
         */
        public FileUploadForm(String name)
        {
            super(name);

            // set this form to multipart mode (allways needed for uploads!)
            setMultiPart(true);

            // Add one file input field
            add(fileUploadField = new FileUploadField("fileInput"));

            // Set maximum size to 100K for demo purposes
            setMaxSize(Bytes.megabytes(50));
        }

        /**
         * @see org.apache.wicket.markup.html.form.Form#onSubmit()
         */
        @Override
        protected void onSubmit()
        {
        	 XStream xstream = new XStream(new DomDriver());
    		 xstream.alias("videodto", VideoDTO.class);
    		 xstream.alias("videoSrcDTO", VideoSrcDTO.class);
    		 xstream.alias("vinkDTO", LinkDTO.class);
        	 
        	try{
            	FileUpload fileUpload= fileUploadField.getFileUpload();
            	String xml=IOUtils.toString(fileUpload.getInputStream());
            	List<VideoDTO> videoDTOs= (List<HtmlReader.VideoDTO>)xstream.fromXML(xml);
            	for(VideoDTO  videoDTO  :videoDTOs){
            		Video  video=new Video();
            		video.setTitle(videoDTO.name);
            		String links=xstream.toXML(videoDTO.videoSrcDTOs);
            		video.setLink(links);
            		Queue queue = QueueFactory.getQueue("subscription-queue");
            		TaskOptions taskOptions= TaskOptions.Builder.withUrl("/gaejsignupsubscriber");
            		taskOptions.param(GAEJSignupSubscriberServlet.PARAM_REQ_HANDLER, GAEJSignupSubscriberServlet.RequestHandler.VALIDATE.toString());
            		taskOptions.param(GAEJSignupSubscriberServlet.PARAM_VIDEOS, 
            				Base64.encodeBase64(GAEJSignupSubscriberServlet.toByteArray(Lists.newArrayList(video))));
            		queue.add(taskOptions);
            	}
            	
        	}catch (Exception e) {
        		throw new RuntimeException(e);
			}
        }
    }
}
