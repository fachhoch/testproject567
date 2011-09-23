package com.gae.yotube.xml

import com.google.apphosting.api.DatastorePb.GetResponse;


class GroovyXml {
	
	Map<String, String>  scriptMap= new HashMap<String, String>();
	
	public GroovyXml(){
		loadScripts();
	}
	public  void loadScripts(){
		    
			def scripts=new XmlParser().parse(this.getClass().getClassLoader().getResourceAsStream("com/gae/yotube/xml/scripts.xml"));
			scripts.script.each {
				String name=it.@name.toString();
				String script=it.text();
				scriptMap.put(name, script);
			}	
	}
	public String getScript(String name){
		return scriptMap.get(name);
	}
	
	
}
