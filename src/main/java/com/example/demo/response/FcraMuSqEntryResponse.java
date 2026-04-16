package com.example.demo.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcraMuSqEntryResponse {

	private String rowId;
	private String rcnSectionFileno;
	private String sectionFileno;
	private String fileno;
	private String uniqueFileno;

	private String assoName;
	private String assoState;

	private String chiefFunctionaryName;
	private String chiefFunctionaryEmail;
	private String chiefFunctionaryMobile;

	private String darpanid;
	private String assoPanNo;

	private String assoEmail;

	private String fileStatus;
	private String recordStatus;
	private String byUser;

	private LocalDateTime generationDate;
	private LocalDateTime sentDate;

	private FcraMuSqEntryDetailsResponse entryDetailsResponse;

}
