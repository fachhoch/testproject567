package org.sairam.ns.service.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.googlecode.objectify.Key;

@Entity
public class Ns implements Serializable {
	
	@Id
	private Long id;
	
	private Date  nsOn;
	
	private String name;
	
	private com.googlecode.objectify.Key<User>  leadKey;
	
	private Status  status;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getNsOn() {
		return nsOn;
	}

	public void setNsOn(Date nsOn) {
		this.nsOn = nsOn;
	}

	public com.googlecode.objectify.Key<User> getLeadKey() {
		return leadKey;
	}

	public void setLeadKey(User  user) {
		this.leadKey =user.getKey();
	}
	
	public Key<Ns>  getKey(){
		return new Key<Ns>(Ns.class, this.getId());
	}
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static enum Status{
		OPEN,
		CLOSED;
	}
	
}
