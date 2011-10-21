package org.seva.dc.ns.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="ns")
public class Ns implements Serializable {
	
	private Long id;
	
	
	private Date  nsOn;
	
	private String name;
	
	private Status  status;
	
	private List<FItem>  fItems=new ArrayList<FItem>();
	
	
	private List<User>  disTeamMembers= new ArrayList<User>();
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
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
	@OneToMany(cascade=CascadeType.ALL)
	public List<FItem> getfItems() {
		return fItems;
	}
	public void setfItems(List<FItem> fItems) {
		this.fItems = fItems;
	}
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<User> getDisTeamMembers() {
		return disTeamMembers;
	}
	
	public void setDisTeamMembers(List<User> disTeamMembers) {
		this.disTeamMembers = disTeamMembers;
	}

	public static enum Status{
		OPEN,
		CLOSED;
	}
	
}
