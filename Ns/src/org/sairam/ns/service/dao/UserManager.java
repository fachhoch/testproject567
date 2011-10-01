package org.sairam.ns.service.dao;

import java.util.List;

import org.sairam.ns.service.dto.UserSearchDTO;
import org.sairam.ns.service.model.User;

public interface UserManager extends GenericDAO<User> {
	
	List<User>  findUser(UserSearchDTO  userSearchDTO);
	
	int  getUserCount(UserSearchDTO  userSearchDTO );

}
