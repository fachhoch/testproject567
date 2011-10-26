package org.seva.dc.ns.doa;

import java.util.List;

import org.seva.dc.ns.domain.Ns;
import org.seva.dc.ns.dto.NsSearchDTO;

public interface NsDAO extends GenericDao<Ns, Long> {
	
	List<Ns>  findNs(NsSearchDTO nsSearchDTO);
	
	int  getNsCount(NsSearchDTO nsSearchDTO );

	
	
	
}
