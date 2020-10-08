package com.currencyconverter.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "json", name="users_audit_entries")
public class AuditEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String queryString;
	private Date queryDate;
	public AuditEntry() {
		super();
	}
	
	public AuditEntry(String queryString, Date queryDate) {
		super();
		this.queryString = queryString;
		this.queryDate = queryDate;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public Date getQueryDate() {
		return queryDate;
	}
	public void setQueryDate(Date queryDate) {
		this.queryDate = queryDate;
	}

}
