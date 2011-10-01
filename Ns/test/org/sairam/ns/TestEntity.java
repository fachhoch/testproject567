package org.sairam.ns;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sairam.ns.service.ManagerFactory;
import org.sairam.ns.service.ManagerFactory.NsDisTeamManager;
import org.sairam.ns.service.dao.NsManager;
import org.sairam.ns.service.dao.UserManager;
import org.sairam.ns.service.model.User;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;


public class TestEntity {
	
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	   @Before
	   public void setUp() {
	        helper.setUp();
	   }
	
	   @After
	   public void tearDown() {
	       helper.tearDown();
	   } 
	  
	 @Test
	public void testNsEntity(){

		 NsManager  nsManager=  ManagerFactory.getNsManger();
		 UserManager userManager= ManagerFactory.getUserManager();
		 NsDisTeamManager  nsDisTeamManager=ManagerFactory.getNsDisTeamManager();
		 
		 User  user= new User();
		 user.setFirstName("sairam");
		 user.setLastName("manda");
		 userManager.put(user);

		 
//		 Ns  ns= new Ns();
//		 ns.setLead(user);
//		 ns.setNsOn(new Date());
//		 nsManager.put(ns);
//		 
//		 NsDisTeam  nsDisTeam= new NsDisTeam();
//		 nsDisTeam.setNs(ns);
//		 nsDisTeam.setUser(user);
//		 nsDisTeamManager.put(nsDisTeam);
//		 
//		 FItem  fItem= new FItem();
//		 fItem.setFtType(FType.RICE);
//		 fItem.setQuantiy(34);
//		 fItem.setUser(user);
//		 fItem.setNs(ns);
//		 
//		 Ns  loadedFromDb=nsManager.get(ns.getId());
//		 Assert.assertNotNull(loadedFromDb);
	}
}
