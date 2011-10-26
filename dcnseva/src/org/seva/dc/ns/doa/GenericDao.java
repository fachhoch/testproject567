package org.seva.dc.ns.doa;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface GenericDao<T, PK extends Serializable> {

	T getById(PK id);
	
	T loadById(PK id);

	List<T> findAll();
	
	List<T> findByCriteria(Map criterias);

	void save(T entity);

	void update(T entity);

	void saveOrUpdate(T entity);

	void delete(T entity);
	
	void deleteById(PK id);
	
	T merge(T detachedInstance);}
