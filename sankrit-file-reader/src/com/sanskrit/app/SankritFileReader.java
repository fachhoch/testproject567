package com.sanskrit.app;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;


public class SankritFileReader {
	
	List<FileDTO>  fileDTOs= new ArrayList<SankritFileReader.FileDTO>();
	Logger  logger=Logger.getLogger(SankritFileReader.class.getName());
	
	public SankritFileReader() {
		logger.log(Level.INFO, "start");
		System.setProperty ("sun.net.client.defaultReadTimeout", "70000");
		System.setProperty ("sun.net.client.defaultConnectTimeout", "70000");

		try{
			init("http://www.newsonair.com/nsd_schedule.asp");
			logger.log(Level.INFO, "finsih");
		}catch (Exception e) {
			//throw new RuntimeException(e);
			logger.log(Level.SEVERE,"exception", e);
		}
	}
	
	void init(String url) throws Exception{
		Parser parser= new Parser(url);
		NodeList audioLinksNodeList= parser.parse(new NodeFilter() {
			@Override
			public boolean accept(Node node) {
				if(node  instanceof  LinkTag){
					LinkTag  linkTag=(LinkTag)node;
					return StringUtils.contains(linkTag.extractLink(), "http://www.newsonair.com/writereaddata/bulletins/Sanskrit");
				}
				return false;
			}
		});
		
		for(org.htmlparser.util.NodeIterator  iterator =audioLinksNodeList.elements();  iterator.hasMoreNodes();){
			Node node=iterator.nextNode();	
			LinkTag  linkTag=(LinkTag)node;
			FileDTO  fileDTO= new FileDTO();
			fileDTO.setName(linkTag.getLinkText()+".mp3");
			fileDTO.setFilebytes(getBytes(linkTag.extractLink()));
			logger.log(Level.INFO, "file "+fileDTO.getName()+fileDTO.getFilebytes().length);
			fileDTOs.add(fileDTO);
		}
	}
	
	
	private byte[]  getBytes(String location) throws Exception{
		logger.log(Level.INFO, "reading location"+location);
		URL url = new URL(location);
		URLConnection uc = url.openConnection();
	    int contentLength = uc.getContentLength();
	    InputStream raw = uc.getInputStream();
	    InputStream in = new BufferedInputStream(raw);
	    byte[] data = new byte[contentLength];
	    int bytesRead = 0;
	    int offset = 0;
	    while (offset < contentLength) {
	      bytesRead = in.read(data, offset, data.length - offset);
	      if (bytesRead == -1)
	        break;
	      offset += bytesRead;
	    }
	    in.close();
	    if (offset != contentLength) {
	        throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
	     }
	    logger.log(Level.INFO, "finished reding file"+location);
	    return data;
	}
	
	
	public static class FileDTO  {
		String name;
		byte[]  filebytes;
		
		public byte[] getFilebytes() {
			return filebytes;
		}
		public void setFilebytes(byte[] filebytes) {
			this.filebytes = filebytes;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	List<FileDTO>  getFileDTOs(){
		return fileDTOs;
	}
	
	void sendEmail() throws Exception{
		logger.log(Level.INFO, "email start");
		if(fileDTOs.size()==0)return;
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
		Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("fachhoch@gmail.com"));
//        msg.addRecipient(Message.RecipientType.TO,
//                         new InternetAddress("manda.sairam@gmail.com"));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress("karrisr@yahoo.com"));
        msg.setSubject("Files  from  newsonair.com");
		Multipart mp = new MimeMultipart();

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent("Attached are files from sanskrit", "text/html");
        htmlPart.setText("Attached are files from sanskrit");
        mp.addBodyPart(htmlPart);
        for(FileDTO  fileDTO  :getFileDTOs()){
	        MimeBodyPart attachment = new MimeBodyPart();
	        attachment.setFileName(fileDTO.getName());
	        DataSource  dataSource=new ByteArrayDataSource(fileDTO.getFilebytes(), "audio/mpeg");
	        attachment.setDataHandler(new DataHandler(dataSource));
	        mp.addBodyPart(attachment);
        }
        msg.setContent(mp);
       Transport.send(msg);		
		logger.log(Level.INFO, "email end");

		
	}
	
}
