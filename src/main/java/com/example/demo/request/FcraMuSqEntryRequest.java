package com.example.demo.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FcraMuSqEntryRequest {

	@NotBlank(message = "RCN Section File No is required")
	private String rcnSectionFileno;

	@NotBlank(message = "Association name is required")
	private String assoName;

	@NotBlank(message = "State is required")
	private String assoState;

	@NotBlank(message = "Chief Functionary Name is required")
	private String chiefFunctionaryName;

	@Email(message = "Invalid Chief Functionary Email")
	@NotBlank(message = "Email is required")
	private String chiefFunctionaryEmail;

	@Email(message = "Invalid Association Email")
	@NotBlank(message = "Email is required")
	private String assoEmail;

	@Size(min = 10, max = 10, message = "Mobile must be 10 digits")
	@NotBlank(message = "Mobile number is required")
	private String chiefFunctionaryMobile;

	@NotBlank(message = "Darpan Id is required")
	private String darpanid;

	@NotBlank(message = "PAN No is required")
	private String assoPanNo;

	private String byUser;
	private String fileStatus;
	/* private String recordStatus; */

	@Valid
	@NotNull(message = "Question details required")
	private FcraMuSqEntryDetailsRequest entryDetailsRequest;

}
