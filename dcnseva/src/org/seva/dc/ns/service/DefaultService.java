package org.seva.dc.ns.service;

import org.seva.dc.ns.doa.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public  class DefaultService<T> implements GenericService<T> {

	@Autowired
	protected GenericDao<T, Long> entityDao ;

	@Override
	public void createOrUpdate(T entity)  {
		
	}

	@Override
	public T getById(Long id)  {
		return entityDao.getById(id);
	}

	@Override
	public void delete(T entity)  {
		entityDao.delete(entity);
	}

	@Override
	public T loadById(Long id)  {
		return entityDao.loadById(id);
	}

	@Override
	public T merge(T entity) {
		return entityDao.merge(entity);
	}
	

}
