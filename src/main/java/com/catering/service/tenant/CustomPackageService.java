package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;

import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.CustomPackageRecordResponseDto;
import com.catering.dto.tenant.request.SaveCustomPackageRecordRequestDto;
import com.catering.dto.tenant.request.CustomPackageDto;
import com.catering.dto.tenant.request.CustomPackageListResponseDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.CustomPackageModel;
import com.catering.model.tenant.GetMenuPreparationForMenuItemCategoryModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing custom packages.
 * <p>
 * This interface defines the contract for operations related to custom packages, 
 * including creating, updating, reading, and deleting custom package records, 
 * as well as managing their statuses and associated menu item categories.
 * </p>
 * 
 * @param <CustomPackageListResponseDto> the type of the response DTO for listing custom packages.
 * @param <CustomPackageModel> the type of the custom package model.
 * @param <Long> the type of the identifier for custom package models.
 * 
 * @author Priyansh Patel
 */
public interface CustomPackageService extends GenericService<CustomPackageListResponseDto, CustomPackageModel, Long> {

	/**
	 * Creates or updates a custom package record.
	 * 
	 * @param saveCustomPackageRecordRequestDto the request DTO containing the data to create or update the custom package.
	 * @throws RestException if an error occurs during the creation or update process.
	 */
	void createAndUpdate(SaveCustomPackageRecordRequestDto saveCustomPackageRecordRequestDto) throws RestException;

	/**
	 * Retrieves a list of custom packages based on the specified filter(searching, sorting, and pagination)..
	 * 
	 * @param filterDto the filter(searching, sorting, and pagination) criteria used to retrieve custom packages.
	 * @return a response container DTO containing a list of custom package response DTOs.
	 */
	ResponseContainerDto<List<CustomPackageListResponseDto>> read(FilterDto filterDto);

	/**
	 * Retrieves a custom package by its ID.
	 * 
	 * @param id the identifier of the custom package to retrieve.
	 * @return a response DTO representing the custom package.
	 */
	CustomPackageRecordResponseDto getById(Long id);

	/**
	 * Deletes a custom package by its ID.
	 * 
	 * @param id the identifier of the custom package to delete.
	 */
	void deleteById(Long id);

	/**
	 * Updates the status of a custom package by its ID.
	 * 
	 * @param id     the identifier of the custom package to update.
	 * @param status the new status to set for the custom package.
	 * @return an optional response DTO with the updated custom package information.
	 * @throws RestException if an error occurs during the status update process.
	 */
	Optional<CustomPackageListResponseDto> updateStatus(Long id, Boolean status) throws RestException;

	/**
	 * Retrieves a list of menu item categories that are associated with the specified package, filtered by whether or not a menu item exists within the category.
	 * 
	 * @param packageId the identifier of the custom package.
	 * @param string    the string used for filtering (e.g., a category name or menu item).
	 * @return a list of models representing the menu item categories.
	 */
	List<GetMenuPreparationForMenuItemCategoryModel> getMenuItemCategoryByMenuItemExist(Long packageId, String string);

	/**
	 * Retrieves a list of active custom packages.
	 * 
	 * @return a list of active custom package DTOs.
	 */
	List<CustomPackageDto> readActive();

}