package org.sairam.ns.service.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.googlecode.objectify.Key;

@Entity
public class NsDisTeam {
	
	@Id
	private Long id;
	
	private Key<Ns>  nsKey;
	
	private Key<User>  userKey;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Key<Ns> getNsKey() {
		return nsKey;
	}

	public Key<User> getUserKey() {
		return userKey;
	}
	
	public void setNsKey(Ns  ns) {
		this.nsKey = ns.getKey();
	}
	
	public void setUserKey(User  user) {
		this.userKey = user.getKey();
	}
}
