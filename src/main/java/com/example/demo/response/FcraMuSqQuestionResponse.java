package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcraMuSqQuestionResponse {

	private Integer questionNo;
	private String questionDescription;
	/*
	 * private Integer sectionNo; private Integer displayOrder;
	 */
	private boolean selected;
}
