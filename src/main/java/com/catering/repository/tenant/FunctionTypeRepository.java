package com.catering.repository.tenant;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.model.tenant.FunctionTypeModel;

/**
 * The FunctionTypeRepository interface provides methods to interact with the database
 * and perform operations on the FunctionTypeModel entity.
 *
 * @author Krushali Talaviya
 * @since June 2023
 */
public interface FunctionTypeRepository extends JpaRepository<FunctionTypeModel, Long>, CommonNameExistenceRepository {

	/**
	 * Retrieves a list of FunctionTypeModels that are active.
	 *
	 * @return A list of active FunctionTypeModels.
	 */
	List<FunctionTypeModel> findByIsActiveTrue(); 

	/**
	 * Retrieves a paginated list of FunctionTypeModel entities sorted by time (converted to company time zone) in ascending order,
	 * and by ID in descending order as a tie-breaker.
	 *
	 * @param pageable the pagination and sorting information
	 * @return an Optional containing a Page of FunctionTypeModel sorted by time ASC, ID DESC
	 */
	@Query(value = "SELECT ft.* FROM function_type ft ORDER BY TIME(CONVERT_TZ(ft.time, (SELECT cp.time_zone FROM catering.catering_preferences cp LIMIT 1), (SELECT cs.time_zone FROM company_setting cs LIMIT 1))) ASC, ft.id DESC", countQuery = "SELECT COUNT(*) FROM function_type ft", nativeQuery = true)
	Optional<Page<FunctionTypeModel>> findAllSortedByCompanyTimeASC(Pageable pageable);

	/**
	 * Retrieves a paginated list of FunctionTypeModel entities sorted by time (converted to company time zone) in descending order,
	 * and by ID in descending order as a tie-breaker.
	 *
	 * @param pageable the pagination and sorting information
	 * @return an Optional containing a Page of FunctionTypeModel sorted by time DESC, ID DESC
	 */
	@Query(value = "SELECT ft.* FROM function_type ft ORDER BY TIME(CONVERT_TZ(ft.time, (SELECT cp.time_zone FROM catering.catering_preferences cp LIMIT 1), (SELECT cs.time_zone FROM company_setting cs LIMIT 1))) DESC, ft.id DESC", countQuery = "SELECT COUNT(*) FROM function_type ft", nativeQuery = true)
	Optional<Page<FunctionTypeModel>> findAllSortedByCompanyTimeDESC(Pageable pageable);

	/**
	 * Retrieves a paginated list of FunctionTypeModel entities filtered by search value on name fields or time (if isTime is true),
	 * sorted by time (converted to company time zone) in descending order, and by ID in descending order.
	 *
	 * @param searchValue the value to search for in name fields or time
	 * @param pageable the pagination and sorting information
	 * @param isTime flag indicating whether to include time in the search criteria
	 * @return an Optional containing a Page of FunctionTypeModel matching the criteria
	 */
	@Query(value = "SELECT ft.* FROM function_type ft WHERE (:searchValue IS NULL OR lower(ft.name_default_lang) LIKE :searchValue OR lower(ft.name_supportive_lang) LIKE :searchValue OR lower(ft.name_prefer_lang) LIKE :searchValue OR (CASE WHEN :isTime THEN ft.time LIKE :searchValue ELSE FALSE END )) ORDER BY TIME(CONVERT_TZ(ft.time, (SELECT cp.time_zone FROM catering.catering_preferences cp LIMIT 1), (SELECT cs.time_zone FROM company_setting cs LIMIT 1))) DESC, ft.id DESC", countQuery = "SELECT COUNT(*) FROM function_type ft WHERE (:searchValue IS NULL OR lower(ft.name_default_lang) LIKE :searchValue OR lower(ft.name_supportive_lang) LIKE :searchValue OR lower(ft.name_prefer_lang) LIKE :searchValueOR (CASE WHEN :isTime THEN ft.time LIKE :searchValue ELSE FALSE END ))", nativeQuery = true)
	Optional<Page<FunctionTypeModel>> findAllSortedByCompanyTimeDESCWithExample(String searchValue, Pageable pageable, boolean isTime);

	/**
	 * Retrieves a paginated list of FunctionTypeModel entities filtered by search value on name fields or time (if isTime is true),
	 * sorted by time (converted to company time zone) in ascending order, and by ID in descending order.
	 *
	 * @param searchValue the value to search for in name fields or time
	 * @param pageable the pagination and sorting information
	 * @param isTime flag indicating whether to include time in the search criteria
	 * @return an Optional containing a Page of FunctionTypeModel matching the criteria
	 */
	@Query(value = "SELECT ft.* FROM function_type ft WHERE (:searchValue IS NULL OR lower(ft.name_default_lang) LIKE :searchValue OR lower(ft.name_supportive_lang) LIKE :searchValue OR lower(ft.name_prefer_lang) LIKE :searchValue OR (CASE WHEN :isTime THEN ft.time LIKE :searchValue ELSE FALSE END )) ORDER BY TIME(CONVERT_TZ(ft.time, (SELECT cp.time_zone FROM catering.catering_preferences cp LIMIT 1), (SELECT cs.time_zone FROM company_setting cs LIMIT 1))) ASC, ft.id DESC", countQuery = "SELECT COUNT(*) FROM function_type ft WHERE (:searchValue IS NULL OR lower(ft.name_default_lang) LIKE :searchValue OR lower(ft.name_supportive_lang) LIKE :searchValue OR lower(ft.name_prefer_lang) LIKE :searchValue OR (CASE WHEN :isTime THEN ft.time LIKE :searchValue ELSE FALSE END ))", nativeQuery = true)
	Optional<Page<FunctionTypeModel>> findAllSortedByCompanyTimeASCWithExample(String searchValue, Pageable pageable, boolean isTime);

}