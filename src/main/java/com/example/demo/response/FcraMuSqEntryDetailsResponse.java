package com.example.demo.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcraMuSqEntryDetailsResponse {
	/*
	 * private String rowId; private String uniqueFileno;
	 */
	private List<String> sqQuestionId;

}
