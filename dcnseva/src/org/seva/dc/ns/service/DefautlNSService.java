package org.seva.dc.ns.service;

import java.util.List;

import org.seva.dc.ns.doa.GenericDao;
import org.seva.dc.ns.doa.NsDAO;
import org.seva.dc.ns.domain.Ns;
import org.seva.dc.ns.dto.NsSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service(value="nsService")
public class DefautlNSService extends DefaultService<Ns> implements NSService {
	
	
	@Autowired()
	protected void init(@Qualifier("nsDAO")GenericDao genericDao) {
		setEntityDao(genericDao);
	}
	
	@Override
	public List<Ns> findNs(NsSearchDTO nsSearchDTO) {
		
		return ((NsDAO)entityDao).findNs(nsSearchDTO);
	}

	@Override
	public int getNsCount(NsSearchDTO nsSearchDTO) {
		return ((NsDAO)entityDao).getNsCount(nsSearchDTO);
	}
	

}
