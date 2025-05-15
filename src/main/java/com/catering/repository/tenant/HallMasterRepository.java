package com.catering.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.HallMasterModel;

/**
 * HallMasterRepository is an interface responsible for managing HallMasterModel entities,
 * providing methods to perform CRUD operations and custom queries.
 *
 * This interface extends JpaRepository to inherit data access operations
 * and CommonNameExistenceRepository to include methods for checking the existence
 * of entities based on name fields in different languages.
 *
 * @author Rohan Parmar
 * @since February 2024
 * @see JpaRepository
 * @see HallMasterModel
 */
public interface HallMasterRepository extends JpaRepository<HallMasterModel, Long>, CommonNameExistenceRepository {

	/**
	 * Retrieves a list of HallMasterModel entities where the 'isActive' flag is set to true.
	 *
	 * @return A list of HallMasterModel entities with 'isActive' set to true.
	 */
	List<HallMasterModel> findByIsActiveTrue();

}