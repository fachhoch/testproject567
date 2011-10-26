package org.seva.dc.ns.doa;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.seva.dc.ns.dto.SearchDTO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public abstract class AbstractHibernateDAO<T, PK  extends  Serializable> extends HibernateDaoSupport implements GenericDao<T, PK> {
	
	private Class<T> persistentClass;
	
	@SuppressWarnings("unchecked")
	public AbstractHibernateDAO() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public Class<T> getPersistentClass() {
        return persistentClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getById(PK id) {
		 return (T) getSessionFactory().getCurrentSession().get(getPersistentClass(), id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T loadById(PK id) {
		return (T) getSessionFactory().getCurrentSession().load(getPersistentClass(), id);
	}

	@Override
	public List<T> findAll() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<T> findByCriteria(Map criterias) {
		return null;
	}

	@Override
	public void save(T entity) {
		getSessionFactory().getCurrentSession().save(entity);
		
	}

	@Override
	public void update(T entity) {
		getSessionFactory().getCurrentSession().update(entity);
		
	}

	@Override
	public void saveOrUpdate(T entity) {
		getSessionFactory().getCurrentSession().saveOrUpdate(entity);
		
	}

	@Override
	public void delete(T entity) {
		getSessionFactory().getCurrentSession().delete(entity);
	}

	@Override
	public void deleteById(PK id) {
		getSessionFactory().getCurrentSession().delete(loadById(id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public T merge(T detachedInstance) {
		return (T)getSessionFactory().getCurrentSession().merge(detachedInstance);
	}
	
	
	protected T findUniqueResultByCriteria(CriteriaCallback  criteriaCallback){
        Criteria crit = getSessionFactory().getCurrentSession().createCriteria(getPersistentClass());
        criteriaCallback.doWithCriteria(crit);
        return (T)crit.uniqueResult();
    }
	
	protected int findCount(CriteriaCallback  criteriaCallback) {
        Criteria crit = getSessionFactory().getCurrentSession().createCriteria(getPersistentClass());
        crit.setProjection(Projections.rowCount());
        criteriaCallback.doWithCriteria(crit);
        return ((Number)crit.uniqueResult()).intValue();
   }
	
	protected List<T> findByCriteria(CriteriaCallback  criteriaCallback ) {
        Criteria crit = getSessionFactory().getCurrentSession().createCriteria(getPersistentClass());
        criteriaCallback.doWithCriteria(crit);
        return crit.list();
   }
	
	protected abstract  class    CriteriaCallback {
		   protected abstract void doWithCriteria(Criteria  criteria);
	   }
	   
	   protected abstract  class SimpleCriteriaCallback<S extends SearchDTO>  extends  CriteriaCallback {
		  protected  S  searchDTO;
		   public SimpleCriteriaCallback(S  dataTableDTO) {
			this.searchDTO=dataTableDTO;
		}
		   @Override
		protected void doWithCriteria(Criteria criteria) {
			addPagination(criteria);
			addFilters(criteria);
		}
		protected abstract void addFilters(Criteria  criteria);
		
		protected  void addPagination(Criteria criteria ){
			if(searchDTO.getPaginationDTO()!=null){
				criteria.setMaxResults(searchDTO.getPaginationDTO().getCount())
				.setFirstResult(searchDTO.getPaginationDTO().getFirst());
			}
			if(searchDTO.getSortDTO().getColumn()!=null){
				criteria.addOrder(searchDTO.getSortDTO().isAsc()  ? 
					Order.asc(searchDTO.getSortDTO().getColumn()) : 
					Order.desc(searchDTO.getSortDTO().getColumn()));
			}
		}   
	   }
	   
	   
	   protected abstract class CountCriteriaCallback  extends  SimpleCriteriaCallback {
		   
		   public CountCriteriaCallback(SearchDTO searchDTO ) {
			super(searchDTO);
		   }
		   @Override
		   protected void addPagination(Criteria arg0) {
		   }
	   }

	
}
