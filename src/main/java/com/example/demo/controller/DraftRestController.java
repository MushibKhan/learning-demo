package com.example.demo.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.DraftRequest;
import com.example.demo.response.DraftResponse;
import com.example.demo.service.DraftService;
import com.example.demo.validation.FetchValidation;
import com.example.demo.validation.SaveValidation;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DraftRestController {

	private final DraftService draftService;

	@PostMapping("/draft")
	public ResponseEntity<Map<String, Object>> getDraft(
			@RequestBody @Validated(FetchValidation.class) DraftRequest request) {

		List<DraftResponse> drafts = draftService.getAllDrafts(request.getModuleId());

		Map<String, Object> response = new HashMap<>();
		if (!drafts.isEmpty()) {
			response.put("latestDraft", drafts.get(0)); 
			response.put("previousDrafts", drafts.subList(1, drafts.size()));
		} else {
			response.put("latestDraft",
					DraftResponse.builder().moduleId(request.getModuleId()).description("").build());
			response.put("previousDrafts", Collections.emptyList());
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/draft/save")
	public ResponseEntity<DraftResponse> saveDraft(@RequestBody @Validated(SaveValidation.class) DraftRequest request) {
		try {
			DraftResponse savedDraft = draftService.saveDraft(request);
			return ResponseEntity.ok(savedDraft);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(DraftResponse.builder().moduleId(request.getModuleId()).description("").build());
		}
	}

}
