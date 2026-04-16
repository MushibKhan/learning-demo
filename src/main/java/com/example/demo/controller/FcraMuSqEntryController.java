package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.FcraMuSqEntryRequest;
import com.example.demo.response.FcraMuSqEditableResponse;
import com.example.demo.response.FcraMuSqEntryResponse;
import com.example.demo.service.FcraMuSqEntryService;
import com.example.demo.utility.ApiResponseDTO;
import com.example.demo.utility.ApiResponseService;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sq")
public class FcraMuSqEntryController {

	private static final String SAVED_SUCCESSFULLY = "SQ Entry saved successfully";
	private static final String FORWARDED_FETCH_SUCCESS = "Forwarded cases fetched successfully.";
	private static final String SELECTED_FETCH_SUCCESS = "Selected cases fetched successfully.";
	private static final String EDITABLE_FETCH_SUCCESS = "SQ editable case fetched successfully.";

	private final ApiResponseService apiResponse;
	private final FcraMuSqEntryService fcraMuSqEntryServiceObj;

	@PostMapping("/saveMuSqEntry")
	public ResponseEntity<ApiResponseDTO<?>> saveMuSqEntry(@Valid @RequestBody FcraMuSqEntryRequest requestObj,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			if (bindingResult.hasErrors()) {
				FieldError error = bindingResult.getFieldErrors().get(0);
				Map<String, String> fieldError = new HashMap<>();
				fieldError.put(error.getField(), error.getDefaultMessage());
				ApiResponseDTO<?> resp = apiResponse.failure("Validation failed", "VALIDATION_ERROR",
						error.getDefaultMessage(), fieldError, request.getRequestURI());
				return ResponseEntity.badRequest().body(resp);
			}
			FcraMuSqEntryResponse sqResponseDto = fcraMuSqEntryServiceObj.saveSqEntry(requestObj);
			ApiResponseDTO<Object> response = apiResponse.success(sqResponseDto, SAVED_SUCCESSFULLY,
					request.getRequestURI());
			return ResponseEntity.ok(response);
		} catch (Exception _EX) {
			Map<String, String> fieldErrors = new HashMap<>();
			fieldErrors.put("applicationId", _EX.getMessage());
			ApiResponseDTO<Object> resp = apiResponse.failure(_EX.getMessage().toString(), "Sq Entry Saving Failed",
					"Sq Entry isn't being saved.", fieldErrors, request.getRequestURI());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
		}
	}

	@GetMapping("/getForwardedCases")
	public ResponseEntity<ApiResponseDTO<?>> getForwardedCases(HttpServletRequest httpRequest) {
		try {
			List<Map<String, String>> forwardedCases = fcraMuSqEntryServiceObj.getAllSqForwardedCases();
			ApiResponseDTO<Object> response = apiResponse.success(forwardedCases, FORWARDED_FETCH_SUCCESS,
					httpRequest.getRequestURI());
			return ResponseEntity.ok(response);
		} catch (Exception ex) {
			Map<String, String> fieldErrors = new HashMap<>();
			fieldErrors.put("Error", ex.getMessage());
			ApiResponseDTO<Object> resp = apiResponse.failure(ex.getMessage(), "FETCH_ERROR",
					"Forwarded cases could not be fetched", fieldErrors, httpRequest.getRequestURI());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
		}
	}

	@GetMapping("/getSelectedCases")
	public ResponseEntity<ApiResponseDTO<?>> loadSelectedCases(@RequestParam("uniqueFileno") List<String> uniqueFilenos,
			HttpServletRequest httpRequest) {
		try {
			List<Map<String, String>> cases = fcraMuSqEntryServiceObj.loadSelectedCases(uniqueFilenos);
			ApiResponseDTO<Object> response = apiResponse.success(cases, SELECTED_FETCH_SUCCESS,
					httpRequest.getRequestURI());
			return ResponseEntity.ok(response);
		} catch (Exception ex) {
			Map<String, String> error = new HashMap<>();
			error.put("Error", ex.getMessage());
			ApiResponseDTO<Object> resp = apiResponse.failure(ex.getMessage(), "Fetched_FAILED",
					"Unable to fetch selected cases", error, httpRequest.getRequestURI());
			return ResponseEntity.badRequest().body(resp);
		}
	}

	@GetMapping("/getCaseForEdit")
	public ResponseEntity<ApiResponseDTO<?>> openEditableSq(@RequestParam String uniqueFileno,
			@RequestParam String rcnSectionFileNo, HttpServletRequest httpRequest) {
		try {
			FcraMuSqEditableResponse sqResponse = fcraMuSqEntryServiceObj.openCaseForEdit(uniqueFileno,
					rcnSectionFileNo);
			ApiResponseDTO<Object> response = apiResponse.success(sqResponse, EDITABLE_FETCH_SUCCESS,
					httpRequest.getRequestURI());
			return ResponseEntity.ok(response);

		} catch (Exception ex) {
			Map<String, String> error = new HashMap<>();
			error.put("Error", ex.getMessage());
			ApiResponseDTO<Object> resp = apiResponse.failure(ex.getMessage(), "Fetched_FAILED",
					"Unable to open editable SQ cases", error, httpRequest.getRequestURI());
			return ResponseEntity.badRequest().body(resp);
		}
	}
}
