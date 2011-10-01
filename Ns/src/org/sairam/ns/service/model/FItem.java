package org.sairam.ns.service.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.googlecode.objectify.Key;

@Entity
public class FItem {
	
	@Id
	private Long id;
	
	
	private Key<User>  userKey;
	
	private int quantiy;
	
	private FType  ftType;
	
	private Key<Ns>  nsKey;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.userKey = user.getKey();
	}

	public Key<User> getUserKey() {
		return userKey;
	}
	
	public int getQuantiy() {
		return quantiy;
	}

	public void setQuantiy(int quantiy) {
		this.quantiy = quantiy;
	}

	public FType getFtType() {
		return ftType;
	}

	public void setFtType(FType ftType) {
		this.ftType = ftType;
	}

	public Key<Ns> getNsKey() {
		return nsKey;
	}
	
	public void setNsKey(Ns  ns) {
		this.nsKey= ns.getKey();
	}
	
	public static  enum FType  {
		RICE,
		BEAN,
		POTATO
	}
}
