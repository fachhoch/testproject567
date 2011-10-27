package org.seva.dc.ns.service;

import org.seva.dc.ns.doa.GenericDao;
import org.seva.dc.ns.domain.FItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service(value="fItemService")
public class DefaultFItemService extends DefaultService<FItem> implements
		FItemService {

	@Autowired
	@Override
	protected void init(@Qualifier("fItemDAO")  GenericDao genericDao) {
		setEntityDao(genericDao);
	}

}
