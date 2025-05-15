package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.catering.model.tenant.OrderMenuPreparationMenuItemModel;

/**
 * Repository interface for managing {@link OrderMenuPreparationMenuItemModel} entities.
 * 
 * This interface extends {@link JpaRepository} to provide standard CRUD operations on {@link OrderMenuPreparationMenuItemModel}.
 * It includes custom methods for updating the repeat number and visibility of menu items, 
 * as well as querying by related order IDs.
 */
public interface OrderMenuPreparationMenuItemRepository extends JpaRepository<OrderMenuPreparationMenuItemModel, Long> {

	/**
	 * Updates the repeat number and counter plate menu item names for a specific menu item in the preparation menu.
	 * 
	 * This method updates the repeat number, counter plate menu item names in various languages, 
	 * and the updated timestamp and user for a specific menu item based on its ID and the associated customer order details.
	 * 
	 * @param repeatNumber the new repeat number for the menu item
	 * @param id the ID of the menu item to update
	 * @param updatedBy the ID of the user who updated the record
	 * @param counterPlateMenuItemNameDefaultLang the name in the default language
	 * @param counterPlateMenuItemNamePreferLang the name in the preferred language
	 * @param counterPlateMenuItemNameSupportiveLang the name in the supportive language
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE order_menu_preparation_menu_item ompmi "
			+ "INNER JOIN order_menu_preparation omp ON omp.id = ompmi.fk_menu_preparation_id "
			+ "INNER JOIN order_function of2 ON of2.id = omp.fk_order_function_id "
			+ "SET ompmi.repeat_number = :repeatNumber, ompmi.counter_plate_menu_item_name_default_lang = :counterPlateMenuItemNameDefaultLang, ompmi.counter_plate_menu_item_name_supportive_lang = :counterPlateMenuItemNameSupportiveLang, ompmi.counter_plate_menu_item_name_prefer_lang = :counterPlateMenuItemNamePreferLang, ompmi.updated_by = :updatedBy, ompmi.updated_at = NOW() "
			+ "WHERE ompmi.fk_menu_item_id = (SELECT * FROM (SELECT _ompmi.fk_menu_item_id FROM order_menu_preparation_menu_item _ompmi INNER JOIN order_menu_preparation _omp ON _omp.id = _ompmi.fk_menu_preparation_id INNER JOIN order_function _of2 ON _of2.id = _omp.fk_order_function_id WHERE _ompmi.id = :id) AS t) AND of2.fk_customer_order_details_id = (SELECT * FROM (SELECT _of2.fk_customer_order_details_id FROM order_menu_preparation_menu_item _ompmi INNER JOIN order_menu_preparation _omp ON _omp.id = _ompmi.fk_menu_preparation_id INNER JOIN order_function _of2 ON _of2.id = _omp.fk_order_function_id WHERE _ompmi.id = :id) AS t)", nativeQuery = true)
	void updateRepeatNumber(int repeatNumber, Long id, Long updatedBy, String counterPlateMenuItemNameDefaultLang, String counterPlateMenuItemNamePreferLang, String counterPlateMenuItemNameSupportiveLang);

	/**
	 * Finds menu preparation menu items based on the given order ID.
	 * 
	 * This method retrieves a list of {@link OrderMenuPreparationMenuItemModel} entities associated with the provided order ID.
	 * 
	 * @param orderId the order ID to filter by
	 * @return a list of menu preparation menu items associated with the specified order ID
	 */
	List<OrderMenuPreparationMenuItemModel> findByMenuPreparationOrderFunctionBookOrderId(Long orderId);

	/**
	 * Updates the visibility and repeat number for a menu item in the table menu report.
	 * 
	 * This method updates the repeat number, menu item visibility, counter plate menu item names in various languages,
	 * and the updated timestamp and user for a specific menu item in the preparation menu.
	 * 
	 * @param repeatNumber the new repeat number for the menu item
	 * @param id the ID of the menu item to update
	 * @param updatedBy the ID of the user who updated the record
	 * @param isVisible the visibility flag (1 for visible, 0 for hidden)
	 * @param counterPlateMenuItemNameDefaultLang the name in the default language
	 * @param counterPlateMenuItemNamePreferLang the name in the preferred language
	 * @param counterPlateMenuItemNameSupportiveLang the name in the supportive language
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE order_menu_preparation_menu_item ompmi "
			+ "INNER JOIN order_menu_preparation omp ON omp.id = ompmi.fk_menu_preparation_id "
			+ "INNER JOIN order_function of2 ON of2.id = omp.fk_order_function_id "
			+ "SET ompmi.repeat_number = :repeatNumber, ompmi.isMenuItemSelected = :isVisible, ompmi.counter_plate_menu_item_name_default_lang = :counterPlateMenuItemNameDefaultLang, ompmi.counter_plate_menu_item_name_supportive_lang = :counterPlateMenuItemNameSupportiveLang, ompmi.counter_plate_menu_item_name_prefer_lang = :counterPlateMenuItemNamePreferLang, ompmi.updated_by = :updatedBy, ompmi.updated_at = NOW() "
			+ "WHERE ompmi.fk_menu_item_id = (SELECT * FROM (SELECT _ompmi.fk_menu_item_id FROM order_menu_preparation_menu_item _ompmi INNER JOIN order_menu_preparation _omp ON _omp.id = _ompmi.fk_menu_preparation_id INNER JOIN order_function _of2 ON _of2.id = _omp.fk_order_function_id WHERE _ompmi.id = :id) AS t) AND of2.fk_customer_order_details_id = (SELECT * FROM (SELECT _of2.fk_customer_order_details_id FROM order_menu_preparation_menu_item _ompmi INNER JOIN order_menu_preparation _omp ON _omp.id = _ompmi.fk_menu_preparation_id INNER JOIN order_function _of2 ON _of2.id = _omp.fk_order_function_id WHERE _ompmi.id = :id) AS t)", nativeQuery = true)
	void updateMenuItemVisiblityInTableMenuReport(int repeatNumber, Long id, Long updatedBy, int isVisible, String counterPlateMenuItemNameDefaultLang, String counterPlateMenuItemNamePreferLang, String counterPlateMenuItemNameSupportiveLang);

}