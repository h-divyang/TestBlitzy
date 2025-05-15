package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.catering.model.tenant.RawMaterialSupplierModel;

/**
 * Repository interface for accessing and manipulating data related to raw material suppliers.
 * 
 * This interface extends `JpaRepository` to provide CRUD operations and `JpaSpecificationExecutor` to support dynamic queries 
 * using specifications for the `RawMaterialSupplierModel` entities.
 */
public interface RawMaterialSupplierRepository extends JpaRepository<RawMaterialSupplierModel, Long>, JpaSpecificationExecutor<RawMaterialSupplierModel> {

	/**
	 * Finds all raw material suppliers for a given raw material ID where the supplier is marked as default.
	 * 
	 * This query returns a list of raw material suppliers associated with the specified raw material ID where 
	 * the `isDefault` flag is set to true.
	 * 
	 * @param rawMaterialId the ID of the raw material
	 * @return a list of raw material suppliers that are marked as default for the given raw material ID
	 */
	List<RawMaterialSupplierModel> findByRawMaterial_IdAndIsDefaultTrue(Long rawMaterialId);

	/**
	 * Finds all raw material suppliers for a given raw material ID.
	 * 
	 * This query returns a list of all raw material suppliers associated with the specified raw material ID, 
	 * regardless of whether they are marked as default or not.
	 * 
	 * @param rawMaterialId the ID of the raw material
	 * @return a list of raw material suppliers for the given raw material ID
	 */
	List<RawMaterialSupplierModel> findByRawMaterial_Id(Long rawMaterialId);

	/**
	 * Finds a paginated list of raw material suppliers for a given raw material ID.
	 * 
	 * This query returns a paginated list of raw material suppliers associated with the specified raw material ID.
	 * 
	 * @param rawMaterialId the ID of the raw material
	 * @param pageable the pagination information
	 * @return a paginated list of raw material suppliers for the given raw material ID
	 */
	Page<RawMaterialSupplierModel> findByRawMaterial_Id(Long rawMaterialId, Pageable pageable);

	/**
	 * Finds a paginated list of raw material suppliers based on dynamic specifications.
	 * 
	 * This query returns a paginated list of raw material suppliers that match the given dynamic query specification.
	 * 
	 * @param specification the specification used to filter the raw material suppliers
	 * @param pageable the pagination information
	 * @return a paginated list of raw material suppliers matching the given specification
	 */
	Page<RawMaterialSupplierModel> findAll(Specification<RawMaterialSupplierModel> specification, Pageable pageable);

	/**
	 * Deletes all entities associated with the specified raw material ID.
	 *
	 * @param rawMaterialId the ID of the raw material whose associated entities should be deleted
	 */
	void deleteByRawMaterial_Id(Long rawMaterialId);

}