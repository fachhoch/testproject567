package org.sairam.ns.service.dao;

import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;

public interface GenericDAO<T> {

	public Key<T> put(T entity);
	public Map<Key<T>, T> putAll(Iterable<T> entities);
	public void delete(T entity);
	public void deleteKey(Key<T> entityKey);
	public void deleteAll(Iterable<T> entities);
	public void deleteKeys(Iterable<Key<T>> keys);
	public T get(Long id) ;
	public T get(Key<T> key) ;

	/*
	 * Convenience method to generate a typed Key<T> for a given id
	 */
	public Key<T> getKey(Long id);

	/*
	 * Get ALL entities of type <T> in the datastore. Potentially very inefficient!!
	 * 	Make sure you have a good reason to use this!!
	 */
	public List<T> listAll();

	/**
	 * Convenience method to get all objects matching a single property
	 *
	 * @param propName
	 * @param propValue
	 * @return T matching Object
	 */
	public T getByProperty(String propName, Object propValue);
	public List<T> listByProperty(String propName, Object propValue);
	public List<Key<T>> listKeysByProperty(String propName, Object propValue);
	public T getByExample(T exampleObj);
	public List<T> listByExample(T exampleObj);

}
