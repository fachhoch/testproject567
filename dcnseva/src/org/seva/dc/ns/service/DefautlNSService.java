package org.seva.dc.ns.service;

import org.seva.dc.ns.domain.Ns;
import org.springframework.stereotype.Service;

@Service(value="defaultNSService")
public class DefautlNSService extends DefaultService<Ns> implements NSService {
	
	
	protected boolean isCreate(Ns t) {
		return t.getId()==null;
	};

}
