package org.seva.dc.ns.service;

import java.util.List;

import org.seva.dc.ns.domain.Ns;
import org.seva.dc.ns.dto.NsSearchDTO;

public interface NSService extends GenericService<Ns> {
	
	List<Ns>  findNs(NsSearchDTO nsSearchDTO);
	
	int  getNsCount(NsSearchDTO nsSearchDTO );


	
	
}
