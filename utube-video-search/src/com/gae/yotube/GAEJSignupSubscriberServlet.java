package com.gae.yotube;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.gae.yotube.service.LinksReader;
import com.gae.yotube.service.LinksReader.LinksDTO;
import com.gae.yotube.service.VideoService;
import com.gae.yotube.service.model.Video;
import com.gae.yotube.util.ListPartition;
import com.gae.yotube.util.ServiceUtil;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.xml.internal.ws.api.pipe.NextAction;

@SuppressWarnings("serial")
public class GAEJSignupSubscriberServlet extends HttpServlet {
	private static final Logger _logger = Logger
			.getLogger(GAEJSignupSubscriberServlet.class.getName());

	
	public  static final String PARAM_REQ_HANDLER="requestHandler";
	public static final String PARAM_VIDEOS="videos";
	public static final String PARAM_URL="http://www.mirchi9.com/cinemas/";
	
	static LinksReader  linksReader= new LinksReader();

	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String strCallResult = "";
		resp.setContentType("text/plain");
		try {
			_logger.log(Level.INFO, "starting task");

//			for(Video video :VideoHelper.getVideos(Integer.parseInt(notOfTimes)) ){
//				ServiceUtil.VIDEO_SERVICE.addVideo(video);
//			}
			
			 Map<String, String> params = Maps.newHashMap();
			 Map<String, String[]>  paramsFromReq=req.getParameterMap(); 
			 for(String  key:paramsFromReq.keySet()){
				 params.put(key, ((String[])paramsFromReq.get(key))[0]);
			 }
			 RequestHandler  requestHandler=RequestHandler.getRequestHandler(req.getParameter(PARAM_REQ_HANDLER));
			 _logger.log(Level.INFO, "starting task  for handler"+requestHandler.toString());
			 Class<? extends Handler>  handlerClass=  registry.get(requestHandler);
			 Handler handler=handlerClass.newInstance();
			 AbstractHandler  abstractHandler=(AbstractHandler)handler;
			 abstractHandler.setParameters(params);
			 _logger.log(Level.INFO, "started"+handlerClass.getName());
			 handler.handle();
			 _logger.log(Level.INFO, "finished"+handlerClass.getName());
			 for(Nexttask  nexttask  :handler.getNexttasks()){
				TaskOptions taskOptions= newTaskOptions();
				for(String key :nexttask.parameters.keySet()){
					taskOptions.param(PARAM_REQ_HANDLER, nexttask.requestHandler.toString());
					Object value=nexttask.parameters.get(key);
					if(value!=null){
						if(value instanceof  String ){
							taskOptions.param(key, (String)value);
						}else{
							taskOptions.param(key,(byte[])value);
						}
					}
					getQueue().add(taskOptions);
				}
			 }
			_logger.log(Level.INFO, "task finished");
			resp.getWriter().println(strCallResult);
		} catch (Exception ex) {
			_logger.log(Level.SEVERE, "Uncaught exception", ex);
			resp.getWriter().println("failed");
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	private static  interface Handler {
		void handle() throws Exception;
		List<Nexttask>  getNexttasks();
	}
	
	
	public static  abstract class AbstractHandler  implements  Handler{
		Map<String, String>  parameters= Maps.newHashMap();
		List<Nexttask>  nexttasks= Lists.newArrayList();
		void setParameters(Map<String, String> parameters ){
			this.parameters=parameters;
		}
		@Override
		public List<Nexttask> getNexttasks() {
			return nexttasks;
		}
		void addNextTask(Nexttask  nexttask){
			nexttasks.add(nexttask);
		}
	}
	
	public static class  Mirchi9Handler  extends  AbstractHandler{
		@Override
		public void handle() throws Exception{
			LinksDTO  linksDTO=getLinksDTO();
			List<Video>  retrievedVideos=  Lists.newArrayList(Maps.transformEntries(linksDTO.getLinks(), new Maps.EntryTransformer<String, String, Video>() {
				public Video transformEntry(String key, String value) {
					Video  video= new Video();
					video.setTitle(key);
					video.setLink(value);
					return video;
				};
			}).values());
			List<List<Video>>  partitions= ListPartition.partition(retrievedVideos, retrievedVideos.size()>6 ?5 :retrievedVideos.size() );
			for(List<Video> listOfVideos : partitions ){
				addNextTask(getNextTask(listOfVideos, RequestHandler.VALIDATE));	
			}
			nextPageTask(linksDTO);
		}
		protected LinksDTO  getLinksDTO() throws Exception{
			String url=parameters.get(PARAM_URL);
			url=url==null ? "http://www.mirchi9.com/cinemas/" :url;
			return linksReader.readMirci9(url);
		}
		protected void nextPageTask(LinksDTO linksDTO ){
			Nexttask  newTask= new Nexttask();
			newTask.requestHandler=getNextTaskRequestHandler();
			newTask.addParameter(PARAM_URL, linksDTO.getNextLink());
			addNextTask(newTask);
		}
		protected RequestHandler getNextTaskRequestHandler(){
			return RequestHandler.READ_MIRCHI;
		}
	}
	
	public static Nexttask  getNextTask(List<Video>  videos, RequestHandler  requestHandler) {
		Nexttask  nextTask= new Nexttask();
		nextTask.requestHandler=requestHandler;
		nextTask.addParameter(PARAM_VIDEOS, org.apache.commons.codec.binary.Base64.encodeBase64(toByteArray(Lists.newArrayList(videos))));
		return nextTask;
	}
	
	
	public static class TeleugLinks  extends Mirchi9Handler {
		@Override
		protected LinksDTO getLinksDTO() throws Exception {
			//return linksReader.readTeluguFreeLinks("http://telugufreelinks.blogspot.com/search/label/telugu%20movies?max-results=100");
			//return super.getLinksDTO();
			String url=parameters.get(PARAM_URL);
			url=url==null ? "http://telugufreelinks.blogspot.com/search/label/telugu%20movies?max-results=100" :url;
			return linksReader.readTeluguFreeLinks(url);

		}

		@Override
		protected RequestHandler getNextTaskRequestHandler() {
			return RequestHandler.READ_TELUGU_LINKS;
		}
	}
	
	public static class Validate  extends  AbstractHandler{

		@Override
		public void handle() throws Exception {
			List<Video>  newVideos=getVideosFromParameter(parameters.get(PARAM_VIDEOS));
			List< Video> filteredVideos= Lists.newArrayList(  com.google.common.collect.Iterables.filter(newVideos, new Predicate<Video>() {
				@Override
				public boolean apply(Video input) {
					return !videoService.exsistsVideoByTitle(input.getTitle());
				}
			}));
			if(!filteredVideos.isEmpty()){
				addNextTask(getNextTask(filteredVideos, RequestHandler.PERSISTS));
			}
		}
	}
	
	public static class SaveHandler  extends  AbstractHandler {

		@Override
		public void handle() throws Exception {
			videoService.saveAll(getVideosFromParameter(parameters.get(PARAM_VIDEOS)));
		}
		
	}
	
	private static List<Video>  getVideosFromParameter(String videosString ){
		if(videosString==null)  return null;
		return (List<Video>)toObject(Base64.decodeBase64(videosString));
	}
	static VideoService  videoService= ServiceUtil.VIDEO_SERVICE;
	
	private static Map<RequestHandler, Class<? extends Handler>>  registry= Maps.newHashMap();
	
	static{
		registry.put(RequestHandler.READ_MIRCHI, Mirchi9Handler.class);
		registry.put(RequestHandler.READ_TELUGU_LINKS, TeleugLinks.class);
		registry.put(RequestHandler.VALIDATE, Validate.class);
		registry.put(RequestHandler.PERSISTS, SaveHandler.class);
	}
	
	public static byte[] toByteArray (Object obj)
	{
	  ByteArrayOutputStream bos = new ByteArrayOutputStream();
	  try {
	    ObjectOutputStream oos = new ObjectOutputStream(bos); 
	    oos.writeObject(obj);
	    oos.flush(); 
	    oos.close(); 
	    bos.close();
	    return  bos.toByteArray ();
	  }
	  catch (IOException ex) {
		  throw  new RuntimeException(ex);
	  }
	}	
	public static Object toObject (byte[] bytes)
	{
	  Object obj = null;
	  try {
	    ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
	    ObjectInputStream ois = new ObjectInputStream (bis);
	    obj = ois.readObject();
	  }
	  catch (IOException ex) {
	    //TODO: Handle the exception
	  }
	  catch (ClassNotFoundException ex) {
	    //TODO: Handle the exception
	  }
	  return obj;
	}
	
	private static Queue  getQueue(){
		return  QueueFactory.getQueue("subscription-queue");
	}
	
	private static TaskOptions  newTaskOptions(){
		return TaskOptions.Builder.withUrl("/gaejsignupsubscriber");
	}
	
	
	private static enum RequestHandler{
		READ_MIRCHI,READ_TELUGU_LINKS,
		VALIDATE,PERSISTS;
		
		public static RequestHandler  getRequestHandler(final String requestHandler){
			return com.google.common.collect.Iterables.find(Lists.newArrayList(RequestHandler.values()),  new  Predicate<RequestHandler>(){
				@Override
				public boolean apply(RequestHandler input) {
					return input.toString().equals(requestHandler);
				}
			});
		}
	}
	
	private static class Nexttask implements  Serializable {
		RequestHandler  requestHandler;
		Map<String, Object>  parameters= Maps.newHashMap();
		void addParameter(String key, Object value){
			parameters.put(key, value);
		}
	}
	
//	private static Function<Map<String, String>, List<Video>>  helper= new Function<Map<String,String>, List<Video>>() {
//		List<Video>  videos= Lists.newArrayList();
//		{
//			
//		}
//		@Override
//		public List<Video> apply(Map<String, String> arg0) {
//			
//			return null;
//		}
//	};

}