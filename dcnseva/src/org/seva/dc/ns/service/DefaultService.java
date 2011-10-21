package org.seva.dc.ns.service;

import org.seva.dc.ns.doa.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class DefaultService<T> implements GenericService<T> {

	@Autowired
	private GenericDao<T, Long> genericDao ;
	
	@Override
	public T createOrUpdate(T t) {
		return isCreate(t) ?  genericDao.create(t)  :genericDao.update(t);
	}
	
	
	protected abstract boolean  isCreate(T t);
	
	
	@Override
	public void delete(T t) {
		// TODO Auto-generated method stub
		genericDao.delete(t);
	}

}
