package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.MenuItemInfoModel;

/**
 * Repository interface for managing {@link MenuItemInfoModel} entities.
 * 
 * This interface extends {@link JpaRepository} to provide standard CRUD operations
 * for managing menu item information in the database.
 */
public interface MenuItemInfoRepository extends JpaRepository<MenuItemInfoModel, Long>{
}