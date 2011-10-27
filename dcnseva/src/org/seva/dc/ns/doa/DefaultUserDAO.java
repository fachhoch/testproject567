package org.seva.dc.ns.doa;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.seva.dc.ns.domain.User;
import org.seva.dc.ns.dto.UserSearchDTO;
import org.springframework.stereotype.Repository;

@Repository(value="userDAO")
public class DefaultUserDAO extends AbstractHibernateDAO<User, Long> implements UserDAO {

	@Override
	public List<User> findUser(UserSearchDTO userSearchDTO) {
		return findByCriteria(new UserSearch(userSearchDTO));
	}

	@Override
	public int getUserCount(UserSearchDTO userSearchDTO) {
		return findCount(new UserSearch(userSearchDTO){
			@Override
			protected void addPagination(Criteria criteria) {
			}
		});
	}
	
	private class UserSearch  extends  SimpleCriteriaCallback<UserSearchDTO>{

		public UserSearch(UserSearchDTO searchDTO) {
			super(searchDTO);
		}

		@Override
		protected void addFilters(Criteria criteria) {
		}
	}
	@Override
	public User findByName(String name) {
		return findUniqueResultByCriteria(new CriteriaCallback() {
			@Override
			protected void doWithCriteria(Criteria criteria) {
				criteria.add(Restrictions.eq("username", "saibaba"));
			}
		});
	}
	
}
