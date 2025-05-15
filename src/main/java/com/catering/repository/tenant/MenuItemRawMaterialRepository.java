package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.catering.model.tenant.MenuItemRawMaterialModel;

/**
 * Repository interface for managing {@link MenuItemRawMaterialModel} entities.
 * 
 * This interface extends {@link JpaRepository} and {@link JpaSpecificationExecutor} to
 * provide standard CRUD operations, pagination, and filtering capabilities for managing
 * menu item raw materials.
 * 
 * Custom query methods include retrieving all raw materials associated with a given
 * menu item ID and supporting pagination for large result sets.
 */
public interface MenuItemRawMaterialRepository extends JpaRepository<MenuItemRawMaterialModel, Long>, JpaSpecificationExecutor<MenuItemRawMaterialModel>{

	/**
	 * Retrieves a list of {@link MenuItemRawMaterialModel} entities associated with the given
	 * menu item ID.
	 *
	 * @param menuItemMaterialId the ID of the menu item
	 * @return a list of {@link MenuItemRawMaterialModel} entities
	 */
	List<MenuItemRawMaterialModel> findByMenuItem_Id(Long menuItemMaterialId);

	/**
	 * Retrieves a paginated list of {@link MenuItemRawMaterialModel} entities associated with the given
	 * menu item ID.
	 *
	 * @param menuItemMaterialId the ID of the menu item
	 * @param pageable pagination information
	 * @return a paginated list of {@link MenuItemRawMaterialModel} entities
	 */
	Page<MenuItemRawMaterialModel> findByMenuItem_Id(Long menuItemMaterialId, Pageable pageable);

	/**
	 * Retrieves a paginated list of {@link MenuItemRawMaterialModel} entities based on the specified
	 * {@link Specification}, allowing for flexible query building.
	 * 
	 * @param specification the {@link Specification} to apply for filtering results
	 * @param pageable pagination information
	 * @return a paginated list of {@link MenuItemRawMaterialModel} entities
	 */
	Page<MenuItemRawMaterialModel> findAll(Specification<MenuItemRawMaterialModel> specification, Pageable pageable);

}