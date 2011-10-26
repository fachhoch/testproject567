package org.seva.dc.ns.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name="ns")
public class Ns implements Serializable {
	
	private Long id;
	
	
	private Date  nsOn;
	
	private String name;
	
	private Status  status;
	
	private List<FItem>  fItems=new ArrayList<FItem>();
	
	
	private List<User>  disTeamMembers= new ArrayList<User>();
	
	private  User  owner;
	
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(nullable=false)
	public Date getNsOn() {
		return nsOn;
	}

	public void setNsOn(Date nsOn) {
		this.nsOn = nsOn;
	}
	
	@Column(nullable=false)
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)	
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

	@OneToOne()
	@JoinColumn(name="owner_id", nullable=false)
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
}
