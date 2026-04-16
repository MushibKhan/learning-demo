package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.request.FcraMuSqEntryRequest;
import com.example.demo.response.FcraMuSqEditableResponse;
import com.example.demo.response.FcraMuSqEntryResponse;

public interface FcraMuSqEntryService {

	public FcraMuSqEntryResponse saveSqEntry(FcraMuSqEntryRequest request);

	public List<Map<String, String>> getAllSqForwardedCases();

	public List<Map<String, String>> loadSelectedCases(List<String> uniqueFilenos);

	public FcraMuSqEditableResponse openCaseForEdit(String uniqueFileno, String rcnSectionFileNo);
}
