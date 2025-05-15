package com.catering.controller.tenant;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.bean.ErrorGenerator;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.HallMasterDto;
import com.catering.properties.FileProperties;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.HallMasterService;
import com.catering.util.FileUtils;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class for managing Hall Master records.
 * 
 * This class provides the following REST APIs for Hall Master management:
 * 
 * <ul>
 * <li><b>POST /hall-master</b>: Creates a new Hall Master record.</li>
 * <li><b>GET /hall-master</b>: Retrieves a list of Hall Master records based on filter data provided in {@link FilterDto}.</li>
 * <li><b>PUT /hall-master</b>: Updates an existing Hall Master record.</li>
 * <li><b>DELETE /hall-master/{id}/{isImage}</b>: Deletes a Hall Master record by ID. Optionally, it can delete the associated image if specified.</li>
 * <li><b>PATCH /hall-master/{id}/status</b>: Updates the status of a Hall Master record (active/inactive).</li>
 * <li><b>GET /hall-master/active</b>: Retrieves a list of all Hall Master records where the status is active.</li>
 * </ul>
 * 
 * <p>The APIs utilize authorization checks to ensure the user has the necessary permissions to perform the respective actions.</p>
 */
@RestController
@RequestMapping(value = ApiPathConstant.HALL_MASTER)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = SwaggerConstant.HALL_MASTER)
public class HallMasterController {

	/**
	 * Service for handling messages, providing internationalization and validation messages throughout the application.
	 */
	MessageService messageService;

	/**
	 * Service responsible for managing Hall Master records, including creating, reading, updating, and deleting Hall Master data.
	 */
	HallMasterService hallMasterService;

	/**
	 * Configuration properties related to file handling, including image size limits and other file-related properties.
	 */
	FileProperties fileProperties;

	/**
	 * Service for handling exceptions and throwing appropriate exceptions when validation or other errors occur.
	 */
	ExceptionService exceptionService;

	/**
	 * Creates a new Hall Master record with the provided data.
	 * 
	 * The provided image is validated before saving the Hall Master record. 
	 * If there are any validation errors with the image, a BadRequestException is thrown.
	 *
	 * @param hallMaster the data for the Hall Master to be created
	 * @param image      an optional image to associate with the Hall Master
	 * @return a response containing the created Hall Master DTO
	 * @throws BadRequestException if the image validation fails
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.HALL_MASTER + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<HallMasterDto>> create(@Valid HallMasterDto hallMaster, @RequestParam(name= Constants.IMG, required = false) MultipartFile image) {
		ErrorGenerator errors = ErrorGenerator.builder();
		errors.getErrors().putAll(FileUtils.imageValidate(image, fileProperties.getImageMaxSize(), messageService).getErrors());
		if (errors.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
		Optional<HallMasterDto> hallMasterResponseDto = hallMasterService.save(hallMaster, image);
		return RequestResponseUtils.generateResponseDto(hallMasterResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves a list of Hall Master records based on the filter data provided.
	 * 
	 * The filter data is provided in the {@link FilterDto} and can include various search parameters like 
	 * status, location, and other hall-specific details.
	 *
	 * @param filterDto the filter criteria to apply when fetching Hall Master records
	 * @return a response containing a list of Hall Master DTOs
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.HALL_MASTER + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<HallMasterDto>> read(FilterDto filterDto) {
		return hallMasterService.read(filterDto);
	}

	/**
	 * Updates an existing Hall Master record with the provided data.
	 * 
	 * The provided image is validated before updating the Hall Master record. 
	 * If there are any validation errors with the image, a BadRequestException is thrown.
	 *
	 * @param hallMaster the updated data for the Hall Master
	 * @param img        an optional new image to associate with the Hall Master
	 * @param request    the HTTP request object, used for additional context (e.g., headers, session)
	 * @return a response containing the updated Hall Master DTO
	 * @throws BadRequestException if the image validation fails
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.HALL_MASTER + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<HallMasterDto>> update(@Valid HallMasterDto hallMaster, @RequestParam(name= Constants.IMG, required = false) MultipartFile img, HttpServletRequest request) {
		ErrorGenerator errors = ErrorGenerator.builder();
		errors.getErrors().putAll(FileUtils.imageValidate(img, fileProperties.getImageMaxSize(), messageService).getErrors());
		if (errors.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
		return RequestResponseUtils.generateResponseDto(hallMasterService.save(hallMaster, img), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Deletes a Hall Master record by its ID.
	 * 
	 * If the {@code isImage} parameter is true, the associated image for the Hall Master record will also be deleted.
	 *
	 * @param id     the ID of the Hall Master record to delete
	 * @param isImage a flag indicating whether the associated image should also be deleted
	 * @return a response indicating the result of the deletion
	 */
	@DeleteMapping(value = ApiPathConstant.ID + ApiPathConstant.IS_IMAGE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.HALL_MASTER + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(value = FieldConstants.COMMON_FIELD_ID) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) Long id, @PathVariable(name = FieldConstants.IS_IMAGE) Boolean isImage) {
		hallMasterService.deleteById(id, isImage);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Updates the status (active/inactive) of a Hall Master record.
	 * 
	 * This operation allows the status of a Hall Master to be changed, making it either active or inactive based on the provided status.
	 *
	 * @param id     the ID of the Hall Master record to update
	 * @param status the new status for the Hall Master record
	 * @return a response containing the updated Hall Master DTO
	 */
	@PatchMapping(value = ApiPathConstant.ID + ApiPathConstant.STATUS)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.HALL_MASTER + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<HallMasterDto>> updateStatus(@PathVariable(value = FieldConstants.COMMON_FIELD_ID) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) Long id, @PathVariable(name = FieldConstants.COMMON_FIELD_STATUS) Boolean status) {
		return RequestResponseUtils.generateResponseDto(hallMasterService.updateStatus(id, status), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves a list of all active Hall Master records.
	 * 
	 * This endpoint fetches Hall Master records that are marked as active, which can be used in scenarios where only 
	 * active halls are relevant, such as during hall booking or transfers.
	 *
	 * @return a response containing a list of all active Hall Master DTOs
	 */
	@GetMapping(value = ApiPathConstant.IS_ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.INPUT_TRANSFER_TO_HALL + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.INPUT_TRANSFER_TO_HALL + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<HallMasterDto>> getAllHallMaster() {
		return RequestResponseUtils.generateResponseDto(hallMasterService.getAllHallMaster());
	}

}