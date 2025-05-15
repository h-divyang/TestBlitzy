package com.catering.repository.tenant;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.catering.model.tenant.LabourShiftModel;

/**
 * LabourShiftRepository is an interface for managing LabourShiftModel entities,
 * providing methods to interact with the database through the JpaRepository interface.
 * This repository supports common CRUD operations and includes functionality for
 * filtering active labour shifts. It also extends CommonNameExistenceRepository
 * to perform case-insensitive checks on name fields across multiple languages.
 *
 * The repository enables seamless integration with database queries,
 * facilitating the management and retrieval of LabourShiftModel entities.
 */
public interface LabourShiftRepository extends JpaRepository<LabourShiftModel, Long>, CommonNameExistenceRepository {

	/**
	 * Retrieves a list of LabourShiftModel entities where the 'isActive' flag is set to true.
	 *
	 * @return A list of LabourShiftModel entities with 'isActive' set to true.
	 */
	List<LabourShiftModel> findByIsActiveTrue();

	/**
	 * Retrieves all labour shifts sorted by company time in ascending order.
	 * Time is converted from catering preference timezone to company setting timezone.
	 *
	 * @param pageable the pagination and sorting information
	 * @return an optional page of {@link LabourShiftModel} sorted by time (ASC) and ID (DESC)
	 */
	@Query(value = "SELECT ls.* FROM labour_shift ls ORDER BY TIME(CONVERT_TZ(ls.time, (SELECT cp.time_zone FROM catering.catering_preferences cp LIMIT 1), (SELECT cs.time_zone FROM company_setting cs LIMIT 1))) ASC, ls.id DESC", countQuery = "SELECT COUNT(*) FROM labour_shift ls", nativeQuery = true)
	Optional<Page<LabourShiftModel>> findAllSortedByCompanyTimeASC(Pageable pageable);

	/**
	 * Retrieves all labour shifts sorted by company time in descending order.
	 * Time is converted from catering preference timezone to company setting timezone.
	 *
	 * @param pageable the pagination and sorting information
	 * @return an optional page of {@link LabourShiftModel} sorted by time (DESC) and ID (DESC)
	 */
	@Query(value = "SELECT ls.* FROM labour_shift ls ORDER BY TIME(CONVERT_TZ(ls.time, (SELECT cp.time_zone FROM catering.catering_preferences cp LIMIT 1), (SELECT cs.time_zone FROM company_setting cs LIMIT 1))) DESC, ls.id DESC", countQuery = "SELECT COUNT(*) FROM labour_shift ls", nativeQuery = true)
	Optional<Page<LabourShiftModel>> findAllSortedByCompanyTimeDESC(Pageable pageable);

	/**
	 * Retrieves filtered labour shifts sorted by company time in descending order.
	 * Applies LIKE search on names or time if `isTime` is true.
	 *
	 * @param searchValue the search value to match against names or time
	 * @param pageable    the pagination information
	 * @param isTime      flag to indicate if searchValue should also match the time field
	 * @return an optional page of {@link LabourShiftModel} matching the criteria
	 */
	@Query(value = "SELECT ls.* FROM labour_shift ls WHERE (:searchValue IS NULL OR lower(ls.name_default_lang) LIKE :searchValue OR lower(ls.name_supportive_lang) LIKE :searchValue OR lower(ls.name_prefer_lang) LIKE :searchValue OR (CASE WHEN :isTime THEN ls.time LIKE :searchValue ELSE FALSE END )) ORDER BY TIME(CONVERT_TZ(ls.time, (SELECT cp.time_zone FROM catering.catering_preferences cp LIMIT 1), (SELECT cs.time_zone FROM company_setting cs LIMIT 1))) DESC, ls.id DESC", countQuery = "SELECT COUNT(*) FROM labour_shift ls WHERE (:searchValue IS NULL OR lower(ls.name_default_lang) LIKE :searchValue OR lower(ls.name_supportive_lang) LIKE :searchValue OR lower(ls.name_prefer_lang) LIKE :searchValueOR (CASE WHEN :isTime THEN ls.time LIKE :searchValue ELSE FALSE END ))", nativeQuery = true)
	Optional<Page<LabourShiftModel>> findAllSortedByCompanyTimeDESCWithExample(String searchValue, Pageable pageable, boolean isTime);

	/**
	 * Retrieves filtered labour shifts sorted by company time in ascending order.
	 * Applies LIKE search on names or time if `isTime` is true.
	 *
	 * @param searchValue the search value to match against names or time
	 * @param pageable    the pagination information
	 * @param isTime      flag to indicate if searchValue should also match the time field
	 * @return an optional page of {@link LabourShiftModel} matching the criteria
	 */
	@Query(value = "SELECT ls.* FROM labour_shift ls WHERE (:searchValue IS NULL OR lower(ls.name_default_lang) LIKE :searchValue OR lower(ls.name_supportive_lang) LIKE :searchValue OR lower(ls.name_prefer_lang) LIKE :searchValue OR (CASE WHEN :isTime THEN ls.time LIKE :searchValue ELSE FALSE END )) ORDER BY TIME(CONVERT_TZ(ls.time, (SELECT cp.time_zone FROM catering.catering_preferences cp LIMIT 1), (SELECT cs.time_zone FROM company_setting cs LIMIT 1))) ASC, ls.id DESC", countQuery = "SELECT COUNT(*) FROM labour_shift ls WHERE (:searchValue IS NULL OR lower(ls.name_default_lang) LIKE :searchValue OR lower(ls.name_supportive_lang) LIKE :searchValue OR lower(ls.name_prefer_lang) LIKE :searchValue OR (CASE WHEN :isTime THEN ls.time LIKE :searchValue ELSE FALSE END ))", nativeQuery = true)
	Optional<Page<LabourShiftModel>> findAllSortedByCompanyTimeASCWithExample(String searchValue, Pageable pageable, boolean isTime);

}