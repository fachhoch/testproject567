package org.seva.dc.ns.service;

import java.util.List;

import org.seva.dc.ns.doa.GenericDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public  abstract class DefaultService<T> implements GenericService<T> {

	protected GenericDao<T, Long> entityDao ;

	protected abstract void init(GenericDao genericDao) ;
	
	@Override
	public void createOrUpdate(T entity)  {
		entityDao.saveOrUpdate(entity);
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
	
	protected void setEntityDao(GenericDao genericDao ){
		this.entityDao=genericDao;
	}
	
	
	@Override
	public List<T> getAll() {
		return entityDao.findAll();
	}
}
