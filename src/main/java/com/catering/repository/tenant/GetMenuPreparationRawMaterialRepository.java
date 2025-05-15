package com.catering.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.catering.dto.tenant.request.CommonDataForDropDownDto;
import com.catering.model.tenant.GetMenuPreparationRawMaterialModel;

/**
 * GetMenuPreparationRawMaterialRepository is an interface for performing database operations
 * related to the GetMenuPreparationRawMaterialModel entity. It extends JpaRepository, which
 * provides a range of methods for CRUD operations.
 *
 * This repository includes a custom query for retrieving raw materials associated with
 * a specific menu item, represented as CommonDataForDropDownDto objects.
 */
public interface GetMenuPreparationRawMaterialRepository extends JpaRepository<GetMenuPreparationRawMaterialModel, Long> {

	/**
	 * Retrieves a list of CommonDataForDropDownDto objects representing raw materials associated with a specific menu item.
	 *
	 * @param menuItemId The ID of the menu item to find associated raw materials for.
	 * @return a list of CommonDataForDropDownDto containing the ID, default language name,
	 *		   preferred language name, and supportive language name of each associated raw material.
	 */
	@Query(value = "SELECT new com.catering.dto.tenant.request.CommonDataForDropDownDto(rm.id, rm.nameDefaultLang, rm.namePreferLang, rm.nameSupportiveLang) FROM GetMenuPreparationRawMaterialModel rm INNER JOIN MenuItemRawMaterialModel mirm ON mirm.rawMaterial.id = rm.id WHERE mirm.menuItem.id = :menuItemId")
	List<CommonDataForDropDownDto> findByMenuItemId(Long menuItemId);

}