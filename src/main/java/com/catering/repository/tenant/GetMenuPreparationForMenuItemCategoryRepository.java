package com.catering.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.catering.model.tenant.GetMenuPreparationForMenuItemCategoryModel;
import com.catering.model.tenant.GetMenuPreparationForMenuItemModel;

/**
 * Provides repository methods for managing and querying GetMenuPreparationForMenuItemCategoryModel entities.
 * Extends JpaRepository to handle basic CRUD operations and PriorityRepository for custom priority-based queries.
 *
 * This repository focuses on retrieving and manipulating menu item categories,
 * along with the associated menu preparations, based on priority, activity status,
 * and order-specific conditions.
 */
public interface GetMenuPreparationForMenuItemCategoryRepository extends JpaRepository<GetMenuPreparationForMenuItemCategoryModel, Long>, PriorityRepository<GetMenuPreparationForMenuItemCategoryModel> {

	/**
	 * Retrieves a distinct list of GetMenuPreparationForMenuItemCategoryModel entities
	 * where the menu item categories are not null and the entities are active,
	 * ordering the results by a priority value. The method applies specific logic
	 * to include inactive categories if linked to the provided order ID.
	 *
	 * @param orderId The ID of the order for which inactive menu item categories may be included in the results.
	 * @return A list of distinct GetMenuPreparationForMenuItemCategoryModel entities ordered by priority and filtered by the specified criteria.
	 */
	@Query("SELECT DISTINCT mic "
		+ "FROM GetMenuPreparationForMenuItemCategoryModel mic "
		+ "LEFT JOIN OrderMenuPreparationMenuItemModel ompmi ON ompmi.menuItemCategory.id = mic.id "
		+ "LEFT JOIN ompmi.menuPreparation omp "
		+ "LEFT JOIN omp.orderFunction ofun "
		+ "LEFT JOIN ofun.bookOrder bo "
		+ "WHERE (mic.isActive = true OR bo.id = :orderId) "
		+ "ORDER BY "
		+ "CASE WHEN mic.priority IS NULL THEN 0 ELSE 1 END ASC, "
		+ "mic.priority ASC, mic.id ASC")
	List<GetMenuPreparationForMenuItemCategoryModel> findDistinctByMenuItemCategoriesIsNotNullAndIsActiveTrueOrderByPriority(Long orderId);

	/**
	 * Retrieves a distinct list of GetMenuPreparationForMenuItemModel entities where
	 * the menu items are not null, are marked as active, or are associated with a specific order ID.
	 * The results are ordered by priority, with null priorities given the least precedence.
	 *
	 * @param orderId The ID of the order used to include menu items associated with it.
	 * @return A list of distinct GetMenuPreparationForMenuItemModel entities ordered by priority and filtered by the specified criteria.
	 */
	@Query("SELECT DISTINCT fm "
		+ "FROM GetMenuPreparationForMenuItemModel fm "
		+ "LEFT JOIN OrderMenuPreparationMenuItemModel ompmi ON ompmi.menuItem.id = fm.id "
		+ "LEFT JOIN ompmi.menuPreparation omp "
		+ "LEFT JOIN omp.orderFunction ofun "
		+ "LEFT JOIN ofun.bookOrder bo "
		+ "LEFT JOIN GetMenuPreparationForMenuItemCategoryModel mic ON mic.id = fm.menuItemCategory "
		+ "WHERE ((mic.isActive = false AND bo.id = :orderId) OR ((mic.isActive = true) AND (fm.isActive = true OR bo.id = :orderId))) "
		+ "ORDER BY "
		+ "CASE WHEN fm.priority IS NULL THEN 0 ELSE 1 END ASC, "
		+ "fm.priority ASC, fm.id ASC")
	List<GetMenuPreparationForMenuItemModel> findDistinctByMenuItemsIsNotNullAndIsActiveTrueOrderByPriority(Long orderId);

}