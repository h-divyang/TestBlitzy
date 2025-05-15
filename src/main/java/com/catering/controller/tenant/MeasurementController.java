package com.catering.controller.tenant;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.CustomRangeDto;
import com.catering.dto.tenant.request.MeasurementDto;
import com.catering.dto.tenant.request.MeasurementWithCustomRangeDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.MeasurementService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * REST Controller for handling Measurement-related operations.
 * 
 * This controller exposes endpoints for creating, retrieving, updating, and deleting measurements in the system.
 * It leverages the {@link MeasurementService} for handling the business logic and communicates through DTOs to the client.
 * The endpoints also ensure that the appropriate user rights are checked using the {@link AuthorizeUserRights} annotation
 * to prevent unauthorized access to certain operations.
 * 
 * The following operations are provided by this controller:
 * 
 * 1. **Create Measurement**: Allows creating a new measurement by sending a {@link MeasurementDto} to the server.
 * 2. **Read Measurements**: Retrieves a list of measurements based on filter criteria.
 * 3. **Get Measurements by Base Unit**: Fetches measurements that are marked as base units.
 * 4. **Get Active Measurements**: Fetches measurements that are marked as active.
 * 5. **Update Measurement**: Updates an existing measurement with the provided {@link MeasurementDto}.
 * 6. **Delete Measurement**: Deletes a measurement by its ID.
 * 
 * Each of these methods is secured by the appropriate user rights to ensure that only authorized users can perform certain actions.
 */
@RestController
@RequestMapping(value = ApiPathConstant.MEASUREMENT)
@Tag(name = SwaggerConstant.MEASUREMENT)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MeasurementController {

	/**
	 * Service responsible for managing messages in the application.
	 */
	MessageService messageService;

	/**
	 * Service responsible for managing measurement-related business logic.
	 */
	MeasurementService measurementService;

	/**
	 * Create a new Measurement entry.
	 * 
	 * This method handles creating a new measurement record by taking in the provided {@link MeasurementDto}.
	 * It validates and processes the DTO and returns a response containing the created measurement data.
	 * 
	 * @param measurementDto The data transfer object containing measurement information to be created.
	 * @return A response container containing the created measurement data.
	 * @throws RestException If an error occurs during the creation of the measurement.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MEASUREMENT + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<MeasurementWithCustomRangeDto>> create(@Valid @RequestBody MeasurementWithCustomRangeDto measurementDto) throws RestException {
		Optional<MeasurementWithCustomRangeDto> measurementResponseDto = measurementService.createAndUpdate(measurementDto);
		return RequestResponseUtils.generateResponseDto(measurementResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieve a list of all Measurements.
	 * 
	 * This method retrieves all measurements based on the provided filter criteria. It fetches the data
	 * from the service and returns it in a response container.
	 * 
	 * @param filterDto The filter criteria used to retrieve measurements.
	 * @return A response container containing the list of measurements.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MEASUREMENT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<MeasurementDto>> read(FilterDto filterDto) {
		return measurementService.read(filterDto);
	}

	/**
	 * Retrieve all Measurements marked as base units.
	 * 
	 * This method retrieves measurements where the 'isBaseUnit' flag is set to true. It calls the service
	 * to get this specific data and returns it in a response container.
	 * 
	 * @return A response container containing a list of measurements that are base units.
	 * @throws RestException If there is an issue retrieving the base unit measurements.
	 */
	@GetMapping(value = ApiPathConstant.MEASUREMENT_BASE_UNIT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MEASUREMENT + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MEASUREMENT + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<MeasurementDto>> getAllByBaseUnit() {
		return RequestResponseUtils.generateResponseDto(measurementService.getAllByIsBaseUnitTrue());
	}

	/**
	 * Retrieves a list of custom ranges associated with a given measurement unit ID.
	 * This endpoint allows fetching custom ranges that are linked to the specified measurement.
	 * 
	 * @param measurementId The ID of the measurement unit for which the custom ranges need to be retrieved.
	 * @return A {@link ResponseContainerDto} containing a list of {@link CustomRangeDto} objects representing the custom ranges.
	 */
	@GetMapping(value = ApiPathConstant.CUSTOM_RANGE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MEASUREMENT + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MEASUREMENT + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<CustomRangeDto>> getCustomRangeByUnitId(@RequestParam Long measurementId) {
		return RequestResponseUtils.generateResponseDto(measurementService.getCustomRangeByMeasurementId(measurementId));
	}

	/**
	 * Retrieve Measurements marked as active.
	 * 
	 * This method fetches the measurements where the 'isActive' flag is true and returns them
	 * in a response container. It ensures the relevant data is available for the active measurements.
	 * 
	 * @return A response container containing a list of active measurements.
	 * @throws RestException If there is an error retrieving the active measurements.
	 */
	@GetMapping(value = ApiPathConstant.IS_ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_EDIT,
			ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_EDIT,
			ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_EDIT,
			ApiUserRightsConstants.RAW_MATERIAL_ALLOCATION + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.RAW_MATERIAL_ALLOCATION + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.RAW_MATERIAL_ALLOCATION + ApiUserRightsConstants.CAN_EDIT,
			ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<MeasurementDto>> getByIsActive() {
		return RequestResponseUtils.generateResponseDto(measurementService.readDataByIsActive());
	}

	/**
	 * Update a Measurement by its ID.
	 * 
	 * This method deletes a measurement by its ID. It calls the service to perform the deletion
	 * and returns a response indicating whether the deletion was successful.
	 * 
	 * @param idStr The ID of the measurement to be deleted.
	 * @return A response container confirming the deletion of the measurement.
	 * @throws RestException If an error occurs while deleting the measurement.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MEASUREMENT + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<MeasurementWithCustomRangeDto>> update(@Valid @RequestBody MeasurementWithCustomRangeDto measurementDto) throws RestException {
		Optional<MeasurementWithCustomRangeDto> measurementResponseDto = measurementService.createAndUpdate(measurementDto);
		return RequestResponseUtils.generateResponseDto(measurementResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Delete Measurement by ID ({@link Long})
	 * */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MEASUREMENT + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String idStr) throws RestException {
		measurementService.deleteById(Long.valueOf(idStr));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

}