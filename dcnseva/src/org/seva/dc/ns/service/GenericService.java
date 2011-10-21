package org.seva.dc.ns.service;

public interface GenericService<T> {
	
	
	T createOrUpdate(T t);
	
	
	void delete(T t);
	
	
	
	
}
