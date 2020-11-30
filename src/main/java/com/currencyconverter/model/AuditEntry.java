package com.currencyconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "xml_valcurs", name="users_audit_entries")
public class AuditEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String queryString;
	private Date queryDate;

	public AuditEntry(String queryString, Date queryDate) {
		super();
		this.queryString = queryString;
		this.queryDate = queryDate;
	}

}
