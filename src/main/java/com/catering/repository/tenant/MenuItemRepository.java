package com.catering.repository.tenant;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.MenuItemDto;
import com.catering.model.tenant.MenuItemModel;

/**
 * Repository interface for managing {@link MenuItemModel} entities.
 * 
 * This interface extends {@link JpaRepository} to provide standard CRUD operations
 * for managing menu items. It also provides methods for checking if a menu item exists
 * by its subcategory and retrieving all active menu items.
 * 
 * Custom queries include fetching menu items with selected materials based on a given
 * package ID and ordering them by priority.
 */
public interface MenuItemRepository extends JpaRepository<MenuItemModel, Long>, PriorityRepository<MenuItemModel>, CommonNameExistenceRepository, JpaSpecificationExecutor<MenuItemModel> {

	/**
	 * Retrieves a list of {@link MenuItemDto} representing all menu items associated
	 * with a given package ID, ordered by menu item priority.
	 *
	 * @param id the package ID
	 * @return a list of {@link MenuItemDto} objects
	 */
	@Query("SELECT NEW com.catering.dto.tenant.request.MenuItemDto(fm) FROM MenuItemModel fm "
			+ "LEFT JOIN PackageMenuItemModel pfm ON pfm.menuItem.id = fm.id "
			+ "WHERE pfm.customPackage.id = :id ORDER BY pfm.menuItem.priority ")
	List<MenuItemDto> getAllMenuItemWithSelectedMaterial(Long id);

	/**
	 * Retrieves a list of all active menu items.
	 * 
	 * @return a list of active {@link MenuItemModel} objects
	 */
	List<MenuItemModel> findByIsActiveTrue();

	/**
	 * Checks if a menu item exists based on its associated subcategory ID.
	 * 
	 * @param id the subcategory ID
	 * @return true if a menu item exists with the given subcategory ID, false otherwise
	 */
	boolean existsByMenuItemSubCategory_Id(Long id);

	/**
	 * Checks if a menu item with the given default language name and specified category exists or not, excluding a specific menu item by ID.
	 *
	 * @param nameDefaultLang The default language name of the menu item.
	 * @param menuItemCategoryId The ID of the menu item category.
	 * @param menuItemId The ID of the menu item to exclude from the check.
	 * @return {@code true} If a menu item with the same name and category (excluding the given ID), otherwise {@code false}.
	 */
	boolean existsByNameDefaultLangIgnoreCaseAndMenuItemCategoryIdAndIdNot(String nameDefaultLang, Long menuItemCategoryId, Long menuItemId);

	/**
	 * Checks if a menu item with the given default language name and specified category exists or not.
	 *
	 * @param nameDefaultLang The default language name of the menu item.
	 * @param menuItemCategoryId The ID of the menu item category.
	 * @return {@code true} If a menu item with the same name and category, otherwise {@code false}.
	 */
	boolean existsByNameDefaultLangIgnoreCaseAndMenuItemCategoryId(String nameDefaultLang, Long menuItemCategoryId);

	/**
	 * Checks if a menu item with the given preferred language name and specified category exists or not, excluding a specific menu item by ID.
	 *
	 * @param namePreferLang The preferred language name of the menu item.
	 * @param menuItemCategoryId The ID of the menu item category.
	 * @param menuItemId The ID of the menu item to exclude from the check.
	 * @return {@code true} If a menu item with the same name and category exists (excluding the given ID), otherwise {@code false}.
	 */
	boolean existsByNamePreferLangIgnoreCaseAndMenuItemCategoryIdAndIdNot(String namePreferLang, Long menuItemCategoryId, Long menuItemId);

	/**
	 * Checks if a menu item with the given preferred language name and specified category exists or not.
	 *
	 * @param namePreferLang The preferred language name of the menu item.
	 * @param menuItemCategoryId The ID of the menu item category.
	 * @return {@code true} If a menu item with the same name and category exists, otherwise {@code false}.
	 */
	boolean existsByNamePreferLangIgnoreCaseAndMenuItemCategoryId(String namePreferLang, Long menuItemCategoryId);

	/**
	 * Checks if a menu item with the given supportive language name and specified category exists or not, excluding a specific menu item by ID.
	 *
	 * @param nameSupportiveLang The supportive language name of the menu item.
	 * @param menuItemCategoryId The ID of the menu item category.
	 * @param menuItemId The ID of the menu item to exclude from the check.
	 * @return {@code true} If a menu item with the same name and category exists (excluding the given ID), otherwise {@code false}.
	 */
	boolean existsByNameSupportiveLangIgnoreCaseAndMenuItemCategoryIdAndIdNot(String nameSupportiveLang, Long menuItemCategoryId, Long menuItemId);

	/**
	 * Checks if a menu item with the given supportive language name and specified category exists or not.
	 *
	 * @param nameSupportiveLang The supportive language name of the menu item.
	 * @param menuItemCategoryId The ID of the menu item category.
	 * @return {@code true} If a menu item with the same name and category exists, otherwise {@code false}.
	 */
	boolean existsByNameSupportiveLangIgnoreCaseAndMenuItemCategoryId(String nameSupportiveLang, Long menuItemCategoryId);

	/**
	 * Updates the priority, updated date, and updated by fields of a menu item entity.
	 *
	 * <p>This method performs a native SQL update on the {@code menu_item} table based on the
	 * ID provided in the {@link MenuItemModel}.</p>
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
	 * @param menuItemModel the {@link MenuItemModel} object containing the new priority and audit information
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE menu_item mi "
			+ "SET mi.priority = :#{#menuItemModel.priority}, "
			+ "mi.updated_at = :#{#menuItemModel.updatedAt}, "
			+ "mi.updated_by = :#{#menuItemModel.updatedBy} "
			+ "WHERE mi.id = :#{#menuItemModel.id}", nativeQuery = true)
	void updatePriority(MenuItemModel menuItemModel);

	/**
	 * Retrieves the highest priority value from the {@code menu_item} table.
	 * <p>
	 * This method executes a native SQL query to return the maximum value of the {@code priority} column.
	 *
	 * @return the highest priority as a {@code long}. Returns 0 if no records are found.
	 */
	@Query(value = "SELECT MAX(mi.priority) FROM menu_item mi", nativeQuery = true)
	long getHighestPriority();

}