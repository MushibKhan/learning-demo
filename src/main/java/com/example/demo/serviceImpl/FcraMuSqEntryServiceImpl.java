package com.example.demo.serviceImpl;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.demo.entity.FcraMuSqEntry;
import com.example.demo.entity.FcraMuSqEntryDetails;
import com.example.demo.repository.FcraMuSqEntryDetailsRepository;
import com.example.demo.repository.FcraMuSqEntryRepository;
import com.example.demo.request.FcraMuSqEntryRequest;
import com.example.demo.response.FcraMuSqEditableResponse;
import com.example.demo.response.FcraMuSqEntryDetailsResponse;
import com.example.demo.response.FcraMuSqEntryResponse;
import com.example.demo.response.FcraMuSqQuestionResponse;
import com.example.demo.service.FcraMuSqEntryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FcraMuSqEntryServiceImpl implements FcraMuSqEntryService {

	private final FcraMuSqEntryRepository sqEntryRepo;
	private final FcraMuSqEntryDetailsRepository sqEntryDetailsRepo;
	private final ModelMapper modelMapper;

	/*
	 * @Transactional
	 * 
	 * @Override public FcraMuSqEntryResponse saveSqEntry(FcraMuSqEntryRequest
	 * requestObj) {
	 * 
	 * FcraMuSqEntry sqEntryObj = modelMapper.map(requestObj, FcraMuSqEntry.class);
	 * try {
	 * 
	 * String year = String.valueOf(Year.now().getValue()); String rowid =
	 * sqEntryRepo.findMaxRowId(); Long seq = sqEntryRepo.findMaxFileNo(year);
	 * 
	 * // 10-digit zero-padded sequence String sequenceFileno =
	 * String.format("%06d", seq); // 13-digit zero-padded sequence String
	 * sequenceUniqueFileno = String.format("%07d", seq);
	 * 
	 * sqEntryObj.setRowId(rowid); sqEntryObj.setSectionFileno("SEC-2026-01");
	 * sqEntryObj.setFileno(sequenceFileno + year); sqEntryObj.setUniqueFileno("34"
	 * + sequenceUniqueFileno + year); sqEntryObj.setSentDate(LocalDateTime.now());
	 * sqEntryObj.setGenerationDate(LocalDateTime.now());
	 * sqEntryRepo.save(sqEntryObj); } catch (Exception _EX) { throw new
	 * RuntimeException("Error occurred while saving: " + _EX.getMessage(), _EX); }
	 * FcraMuSqEntryResponse responseDto = modelMapper.map(sqEntryObj,
	 * FcraMuSqEntryResponse.class); return responseDto; }
	 */

	@Transactional
	@Override
	public FcraMuSqEntryResponse saveSqEntry(FcraMuSqEntryRequest request) {

		// Fetch existing entry or create new
		FcraMuSqEntry sqEntry = sqEntryRepo.findByRcnSectionFileno(request.getRcnSectionFileno()).orElseGet(() -> {

			String year = String.valueOf(Year.now().getValue());
			Long seq = sqEntryRepo.findMaxFileNo(year);

			String sequenceFileno = String.format("%06d", seq);
			String sequenceUniqueFileno = String.format("%07d", seq);

			FcraMuSqEntry newEntry = modelMapper.map(request, FcraMuSqEntry.class);

			Long nextRowId = sqEntryRepo.findMaxRowId();
			newEntry.setRowId(String.valueOf(nextRowId));

			newEntry.setSectionFileno("SEC-2027-02");
			newEntry.setFileno(sequenceFileno + year);
			newEntry.setUniqueFileno("34" + sequenceUniqueFileno + year);
			newEntry.setGenerationDate(LocalDateTime.now());
			newEntry.setSentDate(LocalDateTime.now());

			return newEntry;
		});

		// Update fields
		sqEntry.setAssoName(request.getAssoName());
		sqEntry.setAssoState(request.getAssoState());
		sqEntry.setChiefFunctionaryName(request.getChiefFunctionaryName());
		sqEntry.setChiefFunctionaryEmail(request.getChiefFunctionaryEmail());
		sqEntry.setChiefFunctionaryMobile(request.getChiefFunctionaryMobile());
		sqEntry.setAssoEmail(request.getAssoEmail());
		sqEntry.setDarpanid(request.getDarpanid());
		sqEntry.setAssoPanNo(request.getAssoPanNo());
		sqEntry.setFileStatus(request.getFileStatus());
		sqEntry.setRecordStatus("0");
		sqEntry.setByUser(request.getByUser());
		sqEntry.setSentDate(LocalDateTime.now());

		sqEntryRepo.save(sqEntry);

		// Collect incoming question IDs
		Set<String> incomingQuestionIds = new HashSet<>(request.getEntryDetailsRequest().getSqQuestionId());

		// Delete old questions
		sqEntryDetailsRepo.deleteByUniqueFileno(sqEntry.getUniqueFileno());

		Long maxDetailRowId = sqEntryDetailsRepo.findMaxRowId();
		AtomicLong nextRowId = new AtomicLong(maxDetailRowId);

		List<FcraMuSqEntryDetails> newDetails = incomingQuestionIds.stream().map(qId -> {
			FcraMuSqEntryDetails detail = new FcraMuSqEntryDetails();

			// Convert Long → String
			detail.setRowId(String.valueOf(nextRowId.getAndIncrement()));

			detail.setUniqueFileno(sqEntry.getUniqueFileno());
			detail.setSqQuestionId(qId);
			return detail;
		}).collect(Collectors.toList());

		if (!newDetails.isEmpty()) {
			sqEntryDetailsRepo.saveAll(newDetails);
		}

		// Fetch saved details for response
		List<FcraMuSqEntryDetails> savedDetails = sqEntryDetailsRepo.findByUniqueFileno(sqEntry.getUniqueFileno());

		List<String> questionIds = savedDetails.stream().map(FcraMuSqEntryDetails::getSqQuestionId)
				.collect(Collectors.toList());

		FcraMuSqEntryDetailsResponse detailsResponse = new FcraMuSqEntryDetailsResponse();
		detailsResponse.setSqQuestionId(questionIds);

		FcraMuSqEntryResponse response = modelMapper.map(sqEntry, FcraMuSqEntryResponse.class);

		response.setEntryDetailsResponse(detailsResponse);

		return response;
	}

	@Override
	public List<Map<String, String>> getAllSqForwardedCases() {
		List<Object[]> rows = sqEntryRepo.findAllForwardedSqCases();
		return rows.stream().map(row -> {
			int i = 0;

			String rowId = (String) row[i++];
			String uniqueFileno = (String) row[i++];
			String assoName = (String) row[i++];
			String state = (String) row[i++];
			String chiefName = (String) row[i++];
			String email = (String) row[i++];
			String generationDate = (String) row[i++];
			String fileStatus = (String) row[i++];

			Map<String, String> displayRow = new LinkedHashMap<>();

			displayRow.put("SQ Case ID", rowId);
			displayRow.put("Reference No.", uniqueFileno);
			displayRow.put("Association Name", assoName);
			displayRow.put("State", state);
			displayRow.put("Chief Functionary", chiefName);
			displayRow.put("Email", email);
			displayRow.put("Generation Date", generationDate);
			displayRow.put("Status", fileStatus);

			return displayRow;
		}).toList();
	}

	@Override
	public List<Map<String, String>> loadSelectedCases(List<String> uniqueFilenos) {
		List<Object[]> rows = sqEntryRepo.loadSelectedCases(uniqueFilenos);

		return rows.stream().map(row -> {
			Map<String, String> data = new LinkedHashMap<>();

			int i = 0;
			data.put("SQ Case ID", (String) row[i++]);
			data.put("File No", (String) row[i++]);
			data.put("Reference No", (String) row[i++]);
			data.put("Association Name", (String) row[i++]);
			data.put("State", (String) row[i++]);
			data.put("Chief Functionary", (String) row[i++]);
			data.put("Email", (String) row[i++]);
			data.put("Generation Date", (String) row[i++]);
			data.put("Status", (String) row[i++]);

			return data;
		}).toList();
	}

	@Override
	public FcraMuSqEditableResponse openCaseForEdit(String uniqueFileno, String rcnSectionFileNo) {

		List<Object[]> rows = sqEntryRepo.fetchCaseForEdit(uniqueFileno, rcnSectionFileNo);

		Object[] first = rows.stream().findFirst().orElseThrow(() -> new RuntimeException("No SQ case found"));

		FcraMuSqEditableResponse response = new FcraMuSqEditableResponse();

		response.setUniqueFileno((String) first[0]);
		response.setAssociationName((String) first[1]);
		response.setRcnSectionFileNo((String) first[2]);
		response.setState((String) first[3]);

		List<FcraMuSqQuestionResponse> questions = rows.stream().map(row -> {
			int i = 4;

			FcraMuSqQuestionResponse q = new FcraMuSqQuestionResponse();

			q.setQuestionNo(((Number) row[i++]).intValue());
			q.setQuestionDescription((String) row[i++]);
			/*
			 * q.setSectionNo(((Number) row[i++]).intValue()); q.setDisplayOrder(((Number)
			 * row[i++]).intValue());
			 */
			q.setSelected(((Number) row[i++]).intValue() == 1);

			return q;
		}).toList();

		response.setQuestions(questions);

		return response;
	}
}