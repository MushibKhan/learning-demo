package com.example.demo.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcraMuSqEditableResponse {

	private String uniqueFileno;
	private String associationName;
	private String rcnSectionFileNo;
	private String state;
	private List<FcraMuSqQuestionResponse> questions;

}
