package org.sairam.ns.service.dao;

import java.util.List;

import org.sairam.ns.service.dto.UserSearchDTO;
import org.sairam.ns.service.model.User;

import com.googlecode.objectify.Query;

public class DefaultUserManager extends ObjectifyGenericDAO<User> implements UserManager {

	@Override
	public List<User> findUser(UserSearchDTO userSearchDTO) {
		return listByDoWithOby(new UserSearch(userSearchDTO));
	}

	@Override
	public int getUserCount(UserSearchDTO userSearchDTO) {
		return countByDoWithOby(new UserSearch(userSearchDTO){
			@Override
			protected void adPagination(Query<User> query) {
				// TODO Auto-generated method stub
				//super.adPagination(query);
			}
		});
	}
	
	private class UserSearch  extends  DoWithOby<UserSearchDTO>{

		public UserSearch(UserSearchDTO searchDTO) {
			super(searchDTO);
		}

		@Override
		protected void addFilters(Query<User> query) {
			
			
		}
		
	}

}
