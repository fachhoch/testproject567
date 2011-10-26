package org.seva.dc.ns.service;

import java.util.List;

import org.seva.dc.ns.doa.NsDAO;
import org.seva.dc.ns.domain.Ns;
import org.seva.dc.ns.dto.NsSearchDTO;
import org.springframework.stereotype.Service;

@Service(value="nsService")
public class DefautlNSService extends DefaultService<Ns> implements NSService {

	@Override
	public List<Ns> findNs(NsSearchDTO nsSearchDTO) {
		
		return ((NsDAO)entityDao).findNs(nsSearchDTO);
	}

	@Override
	public int getNsCount(NsSearchDTO nsSearchDTO) {
		return ((NsDAO)entityDao).getNsCount(nsSearchDTO);
	}
	

}
