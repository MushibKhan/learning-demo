package com.example.demo.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.entity.EntryEntity;
import com.example.demo.repository.EntryRepository;
import com.example.demo.repository.MasterRepository;
import com.example.demo.request.DraftRequest;
import com.example.demo.response.DraftResponse;
import com.example.demo.service.DraftService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DraftServiceImpl implements DraftService {

	private final EntryRepository entryRepo;
	private final MasterRepository masterRepo;

/**This method is for to fetch all the drafts */
	@Override
	public List<DraftResponse> getAllDrafts(String searchValue) {

		List<DraftResponse> drafts = entryRepo.findByModuleIdOrderByEntryDateDesc(searchValue).stream()
				.map(entry -> DraftResponse.builder().moduleId(entry.getModuleId()).description(entry.getDescription())
						.fileNo(entry.getFileNo()).entryDate(entry.getEntryDate()).build())
				.collect(Collectors.toList());

		if (drafts.isEmpty()) {
			masterRepo.findByModuleId(searchValue).ifPresent(master -> {
				drafts.add(DraftResponse.builder().moduleId(master.getModuleId()).description(master.getDescription())
						.build());
			});
		}

		return drafts;
	}

	@Override
	public DraftResponse saveDraft(DraftRequest request) {

		EntryEntity entry = new EntryEntity();
		entry.setFileNo(request.getModuleId() + "_" + LocalDateTime.now());
		entry.setModuleId(request.getModuleId());
		entry.setDescription(request.getDescription());
		entry.setEntryDate(LocalDateTime.now());

		entryRepo.save(entry);

		return DraftResponse.builder().moduleId(entry.getModuleId()).description(entry.getDescription()).build();
	}

}
