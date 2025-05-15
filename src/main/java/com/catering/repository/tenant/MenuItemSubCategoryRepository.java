package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.MenuItemSubCategoryModel;

/**
 * Repository interface for managing {@link MenuItemSubCategoryModel} entities.
 * 
 * This interface extends {@link JpaRepository} and {@link CommonNameExistenceRepository} to provide standard CRUD operations 
 * and custom query methods for managing menu item subcategories.
 * 
 * Custom methods:
 * - {@link #findByIsActiveTrue()}: Retrieves all active menu item subcategories.
 */
public interface MenuItemSubCategoryRepository extends JpaRepository<MenuItemSubCategoryModel, Long>, CommonNameExistenceRepository {

	/**
	 * Finds all active menu item subcategories.
	 * 
	 * @return a list of {@link MenuItemSubCategoryModel} that are active (i.e., have isActive set to true).
	 */
	List<MenuItemSubCategoryModel> findByIsActiveTrue();

}