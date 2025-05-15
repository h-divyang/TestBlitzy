package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.model.tenant.CustomPackageModel;
import com.catering.model.tenant.GetMenuPreparationForMenuItemCategoryModel;
import com.catering.model.tenant.GetMenuPreparationForMenuItemModel;

/**
 * This interface represents a repository for Package entities.
 * It extends the {@link JpaRepository} interface, providing CRUD operations for PackageModel entities with Long IDs.
 *
 * @author Krushali Talaviya
 * @since June 2023
 *
 */
public interface CustomPackageRepository extends JpaRepository<CustomPackageModel, Long>, CommonNameExistenceRepository {

	List<CustomPackageModel> findByIsActiveTrue();

	@Query("SELECT DISTINCT mic "
			+ "FROM GetMenuPreparationForMenuItemCategoryModel mic "
			+ "LEFT JOIN PackageMenuItemModel pmi ON pmi.menuItemCategory.id = mic.id "
			+ "LEFT JOIN pmi.customPackage cp "
			+ "WHERE (mic.isActive = true OR cp.id = :packageId) "
			+ "ORDER BY "
			+ "CASE WHEN mic.priority IS NULL THEN 0 ELSE 1 END ASC, "
			+ "mic.priority ASC, mic.id ASC")
	List<GetMenuPreparationForMenuItemCategoryModel> findDistinctByMenuItemCategoriesIsNotNullAndIsActiveTrueOrderByPriority(Long packageId);

	@Query("SELECT DISTINCT fm "
			+ "FROM GetMenuPreparationForMenuItemModel fm "
			+ "LEFT JOIN PackageMenuItemModel pmi ON pmi.menuItem.id = fm.id "
			+ "LEFT JOIN pmi.customPackage cp "
			+ "LEFT JOIN GetMenuPreparationForMenuItemCategoryModel mic ON mic.id = fm.menuItemCategory "
			+ "WHERE ((mic.isActive = false AND cp.id = :packageId) OR ((mic.isActive = true) AND (fm.isActive = true OR cp.id = :packageId))) "
			+ "ORDER BY "
			+ "CASE WHEN fm.priority IS NULL THEN 0 ELSE 1 END ASC, "
			+ "fm.priority ASC, fm.id ASC")
	List<GetMenuPreparationForMenuItemModel> findDistinctByMenuItemsIsNotNullAndIsActiveTrueOrderByPriority(Long packageId);

}