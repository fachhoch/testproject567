package org.seva.dc.ns.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="fitem")
public class FItem {
	
	private Long id;
	
	private User  user;
	
	private int quantiy;
	
	private FType  ftType;
	
	private Ns  ns;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@OneToOne()
	@JoinColumn(name="userId", nullable=false)
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(nullable=false)
	public int getQuantiy() {
		return quantiy;
	}

	public void setQuantiy(int quantiy) {
		this.quantiy = quantiy;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	public FType getFtType() {
		return ftType;
	}

	public void setFtType(FType ftType) {
		this.ftType = ftType;
	}
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name="nsId", nullable=false)
	public Ns getNs() {
		return ns;
	}
	public void setNs(Ns ns) {
		this.ns = ns;
	}
	
	public static  enum FType  {
		RICE,
		BEAN,
		POTATO
	}
}
