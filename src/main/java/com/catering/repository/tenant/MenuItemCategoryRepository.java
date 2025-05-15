package com.catering.repository.tenant;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.MenuItemCategoryJoinWithPackageMenuItemCategoryDto;
import com.catering.model.tenant.MenuItemCategoryModel;

/**
 * Repository interface for managing {@link MenuItemCategoryModel} entities.
 * 
 * This interface extends {@link JpaRepository} and provides additional query methods
 * for managing menu item categories, including:
 * - Retrieving active menu item categories ordered by priority.
 * - Retrieving menu item categories with associated package menu item categories.
 * - Fetching menu item categories with active menu items, ordered by priority.
 * 
 * Additionally, it extends {@link PriorityRepository} and {@link CommonNameExistenceRepository}
 * to support common repository functionality for priority and name existence checks.
 */
public interface MenuItemCategoryRepository extends JpaRepository<MenuItemCategoryModel, Long>, PriorityRepository<MenuItemCategoryModel>, CommonNameExistenceRepository {

	/**
	 * Retrieves all active menu item categories, ordered by priority.
	 * If priority is null, those categories are placed first in the order.
	 *
	 * @return a list of active menu item categories ordered by priority
	 */
	@Query("SELECT "
			+ "DISTINCT mic "
			+ "FROM MenuItemCategoryModel mic "
			+ "WHERE mic.isActive = true "
			+ "ORDER BY CASE WHEN mic.priority IS NULL THEN 0 ELSE 1 END ASC, mic.priority ASC, mic.id ASC")
	List<MenuItemCategoryModel> findByIsActiveTrueOrderByPriority();

	/**
	 * Retrieves all menu item categories associated with a given package, along with
	 * their associated package menu item categories.
	 * 
	 * @param id the package id to filter by
	 * @return a list of {@link MenuItemCategoryJoinWithPackageMenuItemCategoryDto} containing
	 *         menu item categories with package item category details
	 */
	@Query("SELECT NEW com.catering.dto.tenant.request.MenuItemCategoryJoinWithPackageMenuItemCategoryDto(mtc, pfc.numberOfItems, pfc) "
			+ "FROM MenuItemCategoryModel mtc LEFT JOIN PackageMenuItemCategoryModel pfc "
			+ "ON mtc.id = pfc.menuItemCategory.id AND pfc.customPackage.id = :id ORDER BY mtc.priority ")
	List<MenuItemCategoryJoinWithPackageMenuItemCategoryDto> getAllMenuItemCategoryWithPackageMenuItemCategory(Long id);

	/**
	 * Finds a menu item category by its ID.
	 *
	 * @param id the ID of the menu item category
	 * @return an {@link Optional} containing the found menu item category, or empty if not found
	 */
	Optional<MenuItemCategoryModel> findById(Long id);

	/**
	 * Retrieves all distinct active menu item categories that have active menu items,
	 * ordered by priority.
	 *
	 * @return a list of distinct menu item categories with active menu items, ordered by priority
	 */
	@Query("SELECT "
			+ "DISTINCT mic "
			+ "FROM MenuItemCategoryModel mic "
			+ "LEFT JOIN FETCH mic.menuItems fm "
			+ "WHERE "
			+ "mic.isActive = true AND "
			+ "fm.isActive = true "
			+ "ORDER BY "
			+ "CASE WHEN mic.priority IS NULL THEN 0 ELSE 1 END ASC, "
			+ "mic.priority ASC, mic.id ASC")
	List<MenuItemCategoryModel> findDistinctByMenuItemsIsNotNullAndIsActiveTrueOrderByPriority();

	/**
	 * Updates the priority, updated date, and updated by fields of a menu item category entity.
	 *
	 * <p>This method performs a native SQL update on the {@code menu_item_category} table based on the
	 * ID provided in the {@link MenuItemCategoryModel}.</p>
	 *
	 * <p>The fields updated are:
	 * <ul>
	 *		<li>{@code priority}</li>
	 *		<li>{@code updated_at}</li>
	 *		<li>{@code updated_by}</li>
	 * </ul>
	 * </p>
	 *
	 * <p>Marked as {@code @Modifying} and {@code @Transactional} to execute the update operation in a transactional context.</p>
	 *
	 * @param menuItemCategoryModel the {@link MenuItemCategoryModel} object containing the new priority and audit information
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE menu_item_category mic "
			+ "SET mic.priority = :#{#menuItemCategoryModel.priority}, "
			+ "mic.updated_at = :#{#menuItemCategoryModel.updatedAt}, "
			+ "mic.updated_by = :#{#menuItemCategoryModel.updatedBy} "
			+ "WHERE mic.id = :#{#menuItemCategoryModel.id}", nativeQuery = true)
	void updatePriority(MenuItemCategoryModel menuItemCategoryModel);

	/**
	 * Retrieves the highest priority value from the {@code menu_item_category} table.
	 * <p>
	 * This method executes a native SQL query to return the maximum value of the {@code priority} column.
	 *
	 * @return the highest priority as a {@code long}. Returns 0 if no records are found.
	 */
	@Query(value = "SELECT MAX(mic.priority) FROM menu_item_category mic", nativeQuery = true)
	long getHighestPriority();

}