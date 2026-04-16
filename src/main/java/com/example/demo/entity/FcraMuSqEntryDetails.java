package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "MU_SQ_ENTRY_DETAILS")
public class FcraMuSqEntryDetails {

	@Id
	@Column(name = "ROW_ID", length = 25)
	private String rowId;

	@Column(name = "UNIQUE_FILENO", length = 13)
	private String uniqueFileno;

	@Column(name = "SQ_QUESTION_ID")
	private String sqQuestionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UNIQUE_FILENO", referencedColumnName = "UNIQUE_FILENO", insertable = false, updatable = false)
	private FcraMuSqEntry sqEntry;

}
