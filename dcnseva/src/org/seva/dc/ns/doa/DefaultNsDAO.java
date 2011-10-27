package org.seva.dc.ns.doa;

import java.util.List;

import org.hibernate.Criteria;
import org.seva.dc.ns.domain.Ns;
import org.seva.dc.ns.dto.NsSearchDTO;
import org.springframework.stereotype.Repository;

@Repository(value="nsDAO")
public class DefaultNsDAO extends AbstractHibernateDAO<Ns, Long> implements NsDAO {
	
	
	@Override
	public List<Ns> findNs(NsSearchDTO nsSearchDTO) {
		return findByCriteria(new NsSearch(nsSearchDTO));
	}

	@Override
	public int getNsCount(NsSearchDTO nsSearchDTO) {
		// TODO Auto-generated method stub
		return findCount(new NsSearch(nsSearchDTO){
			@Override
			protected void addPagination(Criteria criteria) {
			}
		});
	}
	
	private class NsSearch  extends  SimpleCriteriaCallback<NsSearchDTO>{

		public NsSearch(NsSearchDTO dataTableDTO) {
			super(dataTableDTO);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void addFilters(Criteria criteria) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
}
