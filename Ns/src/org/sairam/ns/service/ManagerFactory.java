package org.sairam.ns.service;

import org.sairam.ns.service.dao.DefaultNsManager;
import org.sairam.ns.service.dao.DefaultUserManager;
import org.sairam.ns.service.dao.GenericDAO;
import org.sairam.ns.service.dao.NsManager;
import org.sairam.ns.service.dao.ObjectifyGenericDAO;
import org.sairam.ns.service.dao.UserManager;
import org.sairam.ns.service.model.FItem;
import org.sairam.ns.service.model.Ns;
import org.sairam.ns.service.model.NsDisTeam;
import org.sairam.ns.service.model.User;

import com.googlecode.objectify.ObjectifyService;

public class ManagerFactory {
	
	static {
		ObjectifyService.register(User.class);
		ObjectifyService.register(Ns.class);
		ObjectifyService.register(FItem.class);
		ObjectifyService.register(NsDisTeam.class);
	}
	

	
	public  interface FItemManger extends  GenericDAO<FItem>{};

	public  static class DefaultFItemManger extends  ObjectifyGenericDAO<FItem>  implements FItemManger {}
	

	
	public  interface NsDisTeamManager  extends  GenericDAO<NsDisTeam>{};

	public  static class DefaultNsDisTeamManager extends  ObjectifyGenericDAO<NsDisTeam>  implements NsDisTeamManager {}
	
	
	protected  ManagerFactory() {}
	
	
	public static   NsManager  getNsManger(){
		return new DefaultNsManager();
	}
	
	public static UserManager  getUserManager() {
		return new DefaultUserManager();
	}
	
	public static FItemManger  getFItemManger(){
		return new DefaultFItemManger();
	}
	
	public static NsDisTeamManager  getNsDisTeamManager(){
		return new DefaultNsDisTeamManager();	
	}
}
