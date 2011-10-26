package org.seva.dc.ns.service;

import java.util.List;

import org.seva.dc.ns.domain.User;
import org.seva.dc.ns.dto.UserSearchDTO;

public interface UserService extends GenericService<User> {
	List<User>  findUser(UserSearchDTO  userSearchDTO);
	
	int  getUserCount(UserSearchDTO  userSearchDTO );

}
