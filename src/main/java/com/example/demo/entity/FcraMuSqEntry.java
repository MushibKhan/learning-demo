package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "MU_SQ_ENTRY")
public class FcraMuSqEntry {

	@Id
	@Column(name = "ROW_ID", length = 25)
	private String rowId;

	@Column(name = "SECTION_FILENO", length = 50)
	private String sectionFileno;

	@Column(name = "FILENO", length = 10)
	private String fileno;

	@Column(name = "UNIQUE_FILENO", length = 13)
	private String uniqueFileno;

	@Column(name = "GENERATION_DATE")
	private LocalDateTime generationDate;

	@Column(name = "SENT_DATE")
	private LocalDateTime sentDate;

	@Column(name = "RCN_SECTION_FILENO")
	private String rcnSectionFileno;

	@Column(name = "ASSO_NAME")
	private String assoName;

	@Column(name = "ASSO_STATE")
	private String assoState;

	@Column(name = "CHIEF_FUNCTIONARY_NAME")
	private String chiefFunctionaryName;

	@Column(name = "CHIEF_FUNCTIONARY_EMAIL")
	private String chiefFunctionaryEmail;

	@Column(name = "ASSO_EMAIL")
	private String assoEmail;

	@Column(name = "CHIEF_FUNCTIONARY_MOBILE")
	private String chiefFunctionaryMobile;

	@Column(name = "DARPANID")
	private String darpanid;

	@Column(name = "ASSO_PAN_NO")
	private String assoPanNo;

	@Column(name = "BY_USER")
	private String byUser;

	@Column(name = "FILE_STATUS")
	private String fileStatus;

	@Column(name = "RECORD_STATUS")
	private String recordStatus;

	@Column(name = "DELETE_USER")
	private String DELETE_USER;

	@OneToMany(mappedBy = "sqEntry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<FcraMuSqEntryDetails> questions;

}
