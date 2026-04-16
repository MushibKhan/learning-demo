package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.FcraMuSqEntry;

@Repository
public interface FcraMuSqEntryRepository extends JpaRepository<FcraMuSqEntry, String> {

	/**
	 * Get next ROW_ID (PostgreSQL compatible)
	 */
	@Query(value = """
			SELECT COALESCE(MAX(CAST(ROW_ID AS BIGINT)), 0) + 1
			FROM MU_SQ_ENTRY
			""", nativeQuery = true)
	Long findMaxRowId();

	/**
	 * Get next File Number based on year (PostgreSQL compatible) Assumes FILENO
	 * format: XXXXXXYYYY First 6 digits = running number Next 4 digits = year
	 */
	@Query(value = """
			SELECT COALESCE(
			    MAX(CAST(SUBSTRING(FILENO, 1, 6) AS BIGINT)),
			    0
			) + 1
			FROM MU_SQ_ENTRY
			WHERE SUBSTRING(FILENO, 7, 4) = :year
			""", nativeQuery = true)
	Long findMaxFileNo(@Param("year") String year);

	/**
	 * Find by RCN Section File Number
	 */
	Optional<FcraMuSqEntry> findByRcnSectionFileno(String rcnSectionFileno);

	/**
	 * Fetch all forwarded SQ cases
	 */
	@Query(value = """
			SELECT
			    sq.ROW_ID,
			    sq.UNIQUE_FILENO,
			    sq.ASSO_NAME,
			    sq.ASSO_STATE,
			    sq.CHIEF_FUNCTIONARY_NAME,
			    sq.ASSO_EMAIL,
			    TO_CHAR(sq.GENERATION_DATE,'DD/MM/YYYY'),
			    fs.FILESTATUS_DESC
			FROM MU_SQ_ENTRY sq
			JOIN TM_FILESTATUS fs
			     ON fs.FILESTATUS_ID = sq.FILE_STATUS
			WHERE sq.RECORD_STATUS = '0'
			ORDER BY sq.GENERATION_DATE DESC
			""", nativeQuery = true)
	List<Object[]> findAllForwardedSqCases();

	/**
	 * Load selected cases
	 */
	@Query(value = """
			SELECT
			    sq.ROW_ID,
			    sq.FILENO,
			    sq.UNIQUE_FILENO,
			    sq.ASSO_NAME,
			    sq.ASSO_STATE,
			    sq.CHIEF_FUNCTIONARY_NAME,
			    sq.ASSO_EMAIL,
			    TO_CHAR(sq.GENERATION_DATE,'DD/MM/YYYY'),
			    fs.FILESTATUS_DESC
			FROM MU_SQ_ENTRY sq
			JOIN TM_FILESTATUS fs
			     ON fs.FILESTATUS_ID = sq.FILE_STATUS
			WHERE sq.UNIQUE_FILENO IN (:uniqueFilenos)
			  AND sq.RECORD_STATUS = '0'
			ORDER BY sq.GENERATION_DATE DESC
			""", nativeQuery = true)
	List<Object[]> loadSelectedCases(@Param("uniqueFilenos") List<String> uniqueFilenos);

	/**
	 * Fetch case for edit
	 */
	@Query(value = """
			SELECT
			    sq.UNIQUE_FILENO,
			    sq.ASSO_NAME,
			    sq.RCN_SECTION_FILENO,
			    sq.ASSO_STATE,
			    q.QUESTION_NO,
			    q.QUESTION_DESCRIPTION,
			    CASE WHEN d.ROW_ID IS NOT NULL THEN 1 ELSE 0 END AS SELECTED
			FROM MU_SQ_ENTRY sq
			CROSS JOIN TM_SQ_QUESTION q
			LEFT JOIN MU_SQ_ENTRY_DETAILS d
			     ON d.UNIQUE_FILENO = sq.UNIQUE_FILENO
			     AND d.SQ_QUESTION_ID = CAST(q.QUESTION_NO AS TEXT)
			WHERE sq.UNIQUE_FILENO = :uniqueFileno
			  AND sq.RCN_SECTION_FILENO = :rcnSectionFileNo
			  AND q.RECORD_STATUS = 0
			ORDER BY q.SECTION_NO, q.DISPLAY_ORDER
			""", nativeQuery = true)
	List<Object[]> fetchCaseForEdit(@Param("uniqueFileno") String uniqueFileno,
			@Param("rcnSectionFileNo") String rcnSectionFileNo);

}
