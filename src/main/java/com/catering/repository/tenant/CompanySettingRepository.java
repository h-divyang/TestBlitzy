package com.catering.repository.tenant;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.catering.model.tenant.CompanySettingModel;

/**
 * CompanySettingRepository is an interface for managing CompanySettingModel entities,
 * providing methods to interact with the underlying database through the JpaRepository interface.
 * This interface enables CRUD operations as well as custom query execution for
 * handling CompanySettingModel data.
 */
public interface CompanySettingRepository extends JpaRepository<CompanySettingModel, Long> {

	/**
	 * Retrieves the pagination size configuration value from the company settings.
	 *
	 * @return An Integer representing the pagination page size.
	 */
	@Query(value = "SELECT cs.pagination_page_sizes FROM company_setting cs ", nativeQuery = true)
	Integer getPaginationSize();

	/**
	 * Updates both menu preparation toggle and menu item price visibility settings for all company settings.
	 * <p>
	 * This bulk update operation modifies both settings simultaneously across all records.
	 * 
	 * @param isMenuPreparationToggleEnabled the new toggle value for menu preparation feature (true = enabled, false = disabled)
	 * @param isMenuItemPriceVisible the new visibility value for the menu preparation tree (true = visible, false = hidden)
	 * @return the number of affected rows
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE company_setting SET is_menu_item_price_visible = :isMenuItemPriceVisible, is_menu_preparation_toggle_enabled = :isMenuPreparationToggleEnabled", nativeQuery = true)
	int updateIsMenuPreparationToggleEnabledAndIsMenuItemPrice(Boolean isMenuPreparationToggleEnabled, Boolean isMenuItemPriceVisible);

	/**
	 * Updates the dark theme and color theme settings for a company by ID.
	 *
	 * @param id The company setting ID.
	 * @param isDarkTheme Whether dark mode is enabled.
	 * @param colourTheme The selected color theme.
	 * @param updatedBy The ID of the user performing the update.
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE company_setting cs SET cs.is_dark_theme = :isDarkTheme, cs.colour_theme = :colourTheme, cs.updated_at = NOW(), cs.updated_by = :updatedBy WHERE cs.id = :id", nativeQuery = true)
	void updateLayoutTheme(@Param("id") Long id, @Param("isDarkTheme") Boolean isDarkTheme, @Param("colourTheme") String colourTheme, @Param("updatedBy") Long updatedBy);

	/**
	 * Updates the color theme for a company setting by ID.
	 *
	 * @param id The company setting ID.
	 * @param colourTheme The new color theme.
	 * @param updatedBy The ID of the user performing the update.
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE company_setting cs SET cs.colour_theme = :colourTheme, cs.updated_at = NOW(), cs.updated_by = :updatedBy WHERE cs.id = :id", nativeQuery = true)
	void updateColourTheme(@Param("id") Long id, @Param("colourTheme") String colourTheme,  @Param("updatedBy") Long updatedBy);

}