package org.seva.dc.ns.service;

import java.util.List;

import org.seva.dc.ns.doa.UserDAO;
import org.seva.dc.ns.domain.User;
import org.seva.dc.ns.dto.UserSearchDTO;

public class DefaultUserService extends DefaultService<User> implements UserService {

	@Override
	public List<User> findUser(UserSearchDTO userSearchDTO) {
		// TODO Auto-generated method stub
		return ((UserDAO)entityDao).findUser(userSearchDTO);
	}

	@Override
	public int getUserCount(UserSearchDTO userSearchDTO) {
		return ((UserDAO)entityDao).getUserCount(userSearchDTO);
	}

}
