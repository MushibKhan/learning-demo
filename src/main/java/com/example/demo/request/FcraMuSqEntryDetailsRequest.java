package com.example.demo.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FcraMuSqEntryDetailsRequest {

	// private String rowId;
	/* private String uniqueFileno; */
	@NotEmpty(message = "At least one question must be selected")
	private List<String> sqQuestionId;

}
