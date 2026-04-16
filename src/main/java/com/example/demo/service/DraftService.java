package com.example.demo.service;

import com.example.demo.request.DraftRequest;
import com.example.demo.response.DraftResponse;
import java.util.List;

public interface DraftService {

	public List<DraftResponse> getAllDrafts(String searchValue);

	public DraftResponse saveDraft(DraftRequest request);

}
