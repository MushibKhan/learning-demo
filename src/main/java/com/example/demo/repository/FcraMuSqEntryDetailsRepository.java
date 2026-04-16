package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.FcraMuSqEntryDetails;

import jakarta.transaction.Transactional;

@Repository
public interface FcraMuSqEntryDetailsRepository extends JpaRepository<FcraMuSqEntryDetails, String> {

	/**
	 * Get next ROW_ID (PostgreSQL compatible)
	 */
	@Query(value = """
			SELECT COALESCE(MAX(CAST(ROW_ID AS BIGINT)), 0) + 1
			FROM MU_SQ_ENTRY_DETAILS
			""", nativeQuery = true)
	Long findMaxRowId();

	/**
	 * Delete all records by uniqueFileno
	 */
	@Transactional
	@Modifying
	@Query("DELETE FROM FcraMuSqEntryDetails d WHERE d.uniqueFileno = :uniqueFileno")
	void deleteByUniqueFileno(@Param("uniqueFileno") String uniqueFileno);

	/**
	 * Find records by uniqueFileno
	 */
	List<FcraMuSqEntryDetails> findByUniqueFileno(String uniqueFileno);
}
