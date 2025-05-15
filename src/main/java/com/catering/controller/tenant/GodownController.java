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
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.GodownDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.GodownService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * REST Controller for managing Godown records. Provides APIs for creating, reading, updating, 
 * and deleting Godown records. Each API also has associated permissions to ensure users 
 * have the appropriate rights for performing these actions.
 * 
 * This controller handles:
 * 1. Creating a new Godown record.
 * 2. Retrieving a list of Godown records based on filters.
 * 3. Updating an existing Godown record.
 * 4. Deleting a Godown record by its ID.
 * 5. Retrieving a list of active Godown records.
 * 
 * Services involved:
 * - {@link GodownService}
 * - {@link MessageService}
 * 
 * Components involved:
 * - godownRecordComponent
 * - godownListComponent
 * 
 * All operations are secured with user rights validation via {@link AuthorizeUserRights}.
 */
@RestController
@RequestMapping(value = ApiPathConstant.GODOWN)
@Tag(name = SwaggerConstant.GODOWN)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GodownController {

	/**
	 * Service responsible for handling messages, including validation and response messages.
	 */
	MessageService messageService;

	/**
	 * Service for managing Godown records, including CRUD operations.
	 */
	GodownService godownService;

	/**
	 * Creates a new Godown record.
	 *
	 * @param godownDto The GodownDto object containing the data for creation.
	 * @return A ResponseContainerDto containing an Optional GodownDto, indicating the result of the creation.
	 * @throws RestException If an exception occurs during the operation.
	 * 
	 * PostAPI is used in Godown module. Which is located in main_menu called other.
	 * Service: GodownService
	 * Component: godownRecordComponent
	 * 
	 */ 
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.GODOWN + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<GodownDto>> create(@Valid @RequestBody GodownDto godownDto) throws RestException {
		Optional<GodownDto> godownResponseDto = godownService.createAndUpdate(godownDto);
		return RequestResponseUtils.generateResponseDto(godownResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	* Retrieves a list of Godown records based on the provided filter criteria.
	*
	* @param filterDto The FilterDto object containing the filter criteria.
	* @return A ResponseContainerDto containing a list of GodownDto objects matching the filter criteria.
	* 
	* GetAPI is used in Godown module. Which is located in main_menu called other.
	* Service: GodownService
	* Component: godownListComponent 
	*/
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.GODOWN + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<GodownDto>> read(FilterDto filterDto) {
		return godownService.read(filterDto);
	}

	/**
	 * Updates an existing Godown record.
	 *
	 * @param godownDto The GodownDto object containing the data for update.
	 * @return A ResponseContainerDto containing an Optional GodownMasterDto, indicating the result of the update.
	 * @throws RestException If an exception occurs during the operation.
	 * 
	 * GetAPI is used in Godown module. Which is located in main_menu called other.
	 * Service: GodownService
	 * Component: godownRecordComponent 
	*/
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.GODOWN + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<GodownDto>> update(@Valid @RequestBody GodownDto godownDto) throws RestException {
		Optional<GodownDto> godownMasterResponseDto = godownService.createAndUpdate(godownDto);
		return RequestResponseUtils.generateResponseDto(godownMasterResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Deletes a Godown record by its ID.
	 *
	 * @param id The ID of the Godown record to delete.
	 * @return A ResponseContainerDto containing null, indicating the successful deletion of the record.
	 * @throws RestException If an exception occurs during the operation or if the ID is invalid.
	 * 
	 * GetAPI is used in Godown module. Which is located in main_menu called other.
	 * Service: GodownService
	 * Component: godownListComponent 
	*/
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.GODOWN + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id) throws RestException {
		godownService.deleteById(id);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Retrieves a list of active Godown records.
	 *
	 * @return A ResponseContainerDto containing a list of active GodownDto objects.
	 * 
	 * GetAPI is used in Godown module. Which is located in main_menu called other.
	 * Service: GodownService
	 * Component: godownListComponent 
	*/
	@GetMapping(value = ApiPathConstant.ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.GODOWN + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<GodownDto>> readActive() {
		return RequestResponseUtils.generateResponseDto(godownService.findByIsActiveTrue());
	}

}