package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.model.tenant.TermsAndConditionsModel;

/**
 * Repository interface for accessing terms and conditions data in the database.
 *
 * @extends JpaRepository Interface for basic CRUD operations with JPA.
 *
 * @since 2023-12-18
 * @author Krushali Talaviya
 */
public interface TermsAndConditionsRepository extends JpaRepository<TermsAndConditionsModel, Long> {

	@Query(value = "SELECT "
			+ "CASE "
			+ "WHEN :langType = 1 AND tac.content_prefer_lang IS NOT NULL AND tac.content_prefer_lang != '' THEN tac.content_prefer_lang "
			+ "WHEN :langType = 2 AND tac.content_supportive_lang IS NOT NULL AND tac.content_supportive_lang != '' THEN tac.content_supportive_lang "
			+ "ELSE tac.content_default_lang "
			+ "END AS content "
			+ "FROM terms_and_conditions tac", nativeQuery = true)
	public String getTermsAndConditionsReportData(Integer langType);

}