package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.MenuItemCategoryInfoModel;

/**
 * Repository interface for accessing and managing {@link MenuItemCategoryInfoModel} entities.
 * <p>
 * This interface extends {@link JpaRepository}, which provides basic CRUD operations such as
 * save, find, delete, and update for {@link MenuItemCategoryInfoModel}. Additional custom queries
 * can be added here as needed.
 * </p>
 * <p>
 * The repository is typically used to manage the lifecycle of {@link MenuItemCategoryInfoModel} entities
 * related to menu item categories and associated information.
 * </p>
 * 
 * @see JpaRepository
 * @see MenuItemCategoryInfoModel
 */
public interface MenuItemCategoryInfoRepository extends JpaRepository<MenuItemCategoryInfoModel, Long> {
}