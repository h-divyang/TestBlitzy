package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.catering.model.tenant.MeasurementModel;

/**
 * Repository interface for accessing and managing {@link MeasurementModel} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide basic CRUD operations such as
 * save, find, delete, and update for {@link MeasurementModel}. It also extends 
 * {@link JpaSpecificationExecutor} to support dynamic queries and {@link CommonNameExistenceRepository}
 * for checking name existence across various languages.
 * </p>
 * <p>
 * The repository is used to manage measurement units, including their symbols and active states.
 * It offers additional functionality to check for the existence of measurements by their symbols
 * in different languages.
 * </p>
 * 
 * @see JpaRepository
 * @see JpaSpecificationExecutor
 * @see CommonNameExistenceRepository
 * @see MeasurementModel
 */
public interface MeasurementRepository extends JpaRepository<MeasurementModel, Long>, JpaSpecificationExecutor<MeasurementModel>, CommonNameExistenceRepository {

	/**
	 * Checks if a measurement with the given default language symbol exists, excluding the measurement with the specified ID.
	 * @param symbolDefaultLang the symbol in the default language
	 * @param id the ID to exclude from the check
	 * @return true if a measurement exists with the given symbol and a different ID, false otherwise
	 */
	boolean existsBySymbolDefaultLangIgnoreCaseAndIdNot(String symbolDefaultLang, Long id);

	/**
	 * Checks if a measurement with the given default language symbol exists.
	 * @param symbolDefaultLang the symbol in the default language
	 * @return true if a measurement exists with the given symbol, false otherwise
	 */
	boolean existsBySymbolDefaultLangIgnoreCase(String symbolDefaultLang);

	/**
	 * Checks if a measurement with the given preferred language symbol exists, excluding the measurement with the specified ID.
	 * @param symbolDefaultLang the symbol in the preferred language
	 * @param id the ID to exclude from the check
	 * @return true if a measurement exists with the given symbol and a different ID, false otherwise
	 */
	boolean existsBySymbolPreferLangIgnoreCaseAndIdNot(String symbolDefaultLang, Long id);

	/**
	 * Checks if a measurement with the given preferred language symbol exists.
	 * @param symbolDefaultLang the symbol in the preferred language
	 * @return true if a measurement exists with the given symbol, false otherwise
	 */
	boolean existsBySymbolPreferLangIgnoreCase(String symbolDefaultLang);

	/**
	 * Checks if a measurement with the given supportive language symbol exists, excluding the measurement with the specified ID.
	 * @param symbolDefaultLang the symbol in the supportive language
	 * @param id the ID to exclude from the check
	 * @return true if a measurement exists with the given symbol and a different ID, false otherwise
	 */
	boolean existsBySymbolSupportiveLangIgnoreCaseAndIdNot(String symbolDefaultLang, Long id);

	/**
	 * Checks if a measurement with the given supportive language symbol exists.
	 * @param symbolDefaultLang the symbol in the supportive language
	 * @return true if a measurement exists with the given symbol, false otherwise
	 */
	boolean existsBySymbolSupportiveLangIgnoreCase(String symbolDefaultLang);

	/**
	 * Retrieves a list of measurement records that are not being used as a parent unit.
	 * 
	 * @return A list of {@link MeasurementModel} objects that are not used as parent units.
	 */
	@Query("SELECT m FROM MeasurementModel m WHERE m.id NOT IN (SELECT DISTINCT m2.baseUnitId FROM MeasurementModel m2 WHERE m2.baseUnitId IS NOT NULL)")
	List<MeasurementModel> findRecordsNotUsedAsParentUnit();

	/**
	 * Counts the total number of measurements in the repository.
	 * @return the count of all measurements
	 */
	long count();

	/**
	 * Retrieves all active measurements, ordered by their ID in ascending order.
	 * @param isActive the active status to filter measurements by
	 * @return a list of active measurements ordered by ID
	 */
	List<MeasurementModel> findAllByIsActiveOrderByIdAsc(boolean isActive);

	/**
	 * Finds the list of table names that contain a column named 'fk_measurement_id' within the current database schema.
	 * @return a list of table names that have the 'fk_measurement_id' column
	 */
	@Query(value = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME = 'fk_measurement_id' AND TABLE_SCHEMA = DATABASE()", nativeQuery = true)
	List<String> findTableNamesWithMeasurementColumn();

}