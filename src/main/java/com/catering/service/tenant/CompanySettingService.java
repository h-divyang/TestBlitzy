package com.catering.service.tenant;

import org.springframework.web.multipart.MultipartFile;

import com.catering.dto.tenant.request.CompanySettingDto;
import com.catering.model.tenant.CompanySettingModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing company settings. Provides methods to retrieve, create,
 * and update configurations relevant to company settings.
 * This service extends the GenericService interface for additional CRUD operations.
 */
public interface CompanySettingService extends GenericService<CompanySettingDto, CompanySettingModel, Long> {

	/**
	 * Retrieves the company settings configuration.
	 *
	 * @return An instance of CompanySettingDto containing the details of the company's settings.
	 */
	CompanySettingDto getCompannySetting(Boolean isBackgroundImageRequired);

	/**
	 * Retrieves the pagination size for the company settings.
	 *
	 * @return An Integer representing the size of pagination to be used in company settings.
	 */
	Integer getPaginationSize();

	/**
	 * Creates or updates the company settings based on the provided CompanySettingDto.
	 *
	 * @param companySettingDto The CompanySettingDto object containing the settings data to be created or updated.
	 * @return The updated or newly created CompanySettingDto object.
	 */
	CompanySettingDto createAndUpdate(CompanySettingDto companySettingDto, MultipartFile backgroundImage);

	/**
	 * Updates the menu preparation toggle and tree visibility settings.
	 * 
	 * @param isMenuPreparationToggleEnabled Optional Boolean flag to enable/disable menu preparation feature. If null, the existing toggle value will not be modified.
	 * @param isMenuItemPriceVisible Required Boolean flag to control menu item price visibility. True to show the tree, false to hide it.
	 */
	void updateIsMenuPreparationToggleEnabledAndIsMenuItemPrice(Boolean isMenuPreparationToggleEnabled, Boolean isMenuItemPriceVisible);

	/**
	 * Deletes the currently set background image for the application.
	 * <p>
	 * This operation removes the background image and reverts to the default background.
	 * Requires appropriate permissions to execute.
	 */
	void deleteBackgroundImage();

}