package org.sairam.ns.service.dao;

import java.util.List;

import org.sairam.ns.service.dto.NsSearchDTO;
import org.sairam.ns.service.model.FItem;
import org.sairam.ns.service.model.Ns;
import org.sairam.ns.service.model.User;

public interface NsManager extends GenericDAO<Ns> {
	
	User  getLead(Ns  ns);
	
	List<User>  getDisTeamMembers(Ns  ns);
	
	List<FItem>  getFItems(Ns  ns);
	
	List<Ns>  findNs(NsSearchDTO  nsSearchDTO );
	
	int  getNsCount(NsSearchDTO  nsSearchDTO );
	
	
	
	
}
