package org.seva.dc.ns.service;

import java.util.List;


public interface GenericService<T> {
	
	
public void createOrUpdate(final T entity) ;
	
	
	public T getById(final Long id) ;
	
	public void delete(final T entity) ;
	
	public T loadById(final Long id) ;
	
	public T merge(final T entity) ;
	
	public List<T> getAll();
	
	
}
