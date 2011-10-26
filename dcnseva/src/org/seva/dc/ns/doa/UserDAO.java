package org.seva.dc.ns.doa;

import java.util.List;

import org.seva.dc.ns.domain.User;
import org.seva.dc.ns.dto.UserSearchDTO;

public interface UserDAO extends GenericDao<User, Long> {
		
	List<User>  findUser(UserSearchDTO  userSearchDTO);
	
	int  getUserCount(UserSearchDTO  userSearchDTO );

	
}
