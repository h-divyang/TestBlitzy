package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.MenuItemRupeesModel;

/**
 * Repository interface for managing {@link MenuItemRupeesModel} entities.
 * 
 * This interface extends {@link JpaRepository} to provide standard CRUD operations
 * for managing menu item rupees entities.
 */
public interface MenuItemRupeesRepository extends JpaRepository<MenuItemRupeesModel, Long> {
}