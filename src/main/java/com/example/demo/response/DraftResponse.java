package com.example.demo.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DraftResponse {

	private String moduleId;
	private String description;
	private String fileNo;
	private LocalDateTime entryDate;
}
