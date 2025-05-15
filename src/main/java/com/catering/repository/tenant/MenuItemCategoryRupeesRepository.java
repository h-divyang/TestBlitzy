package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.MenuItemCategoryRupeesModel;

/**
 * Repository interface for managing {@link MenuItemCategoryRupeesModel} entities.
 * 
 * This interface extends {@link JpaRepository} to provide standard CRUD operations
 * for managing menu item category rupees in the database.
 */
public interface MenuItemCategoryRupeesRepository extends JpaRepository<MenuItemCategoryRupeesModel, Long>{
}