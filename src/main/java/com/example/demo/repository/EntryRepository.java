package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.EntryEntity;

@Repository
public interface EntryRepository extends JpaRepository<EntryEntity, Long> {

	List<EntryEntity> findByModuleIdOrderByEntryDateDesc(String moduleId);

}
