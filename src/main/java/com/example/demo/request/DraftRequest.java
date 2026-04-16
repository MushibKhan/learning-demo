package com.example.demo.request;

import com.example.demo.validation.FetchValidation;
import com.example.demo.validation.SaveValidation;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DraftRequest {

	@NotBlank(message = "Module ID is required", groups = { SaveValidation.class, FetchValidation.class })
	private String moduleId;

	@NotBlank(message = "Description is required", groups = SaveValidation.class)
	private String description;
}
