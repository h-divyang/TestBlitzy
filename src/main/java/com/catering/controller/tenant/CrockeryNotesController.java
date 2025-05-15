package com.catering.controller.tenant;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.CrockeryNotesDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.CrockeryNotesService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for managing Crockery Notes, providing endpoints to create and view crockery note details.
 * <p>
 * The following API endpoints are available:
 * <ul>
 *     <li><b>POST /crockery-notes</b> - Creates a new Crockery Note. Requires editing or adding permissions.</li>
 *     <li><b>GET /crockery-notes</b> - Retrieves the details of the existing Crockery Notes. Requires viewing permissions.</li>
 * </ul>
 * </p>
 * This controller handles interactions related to Crockery Notes such as creation and retrieval.
 * The methods leverage CrockeryNotesService to perform the necessary business logic and MessageService for localized success messages.
 * All API endpoints enforce user rights based on roles and permissions defined in {@link ApiUserRightsConstants}.
 * <p>
 * @see CrockeryNotesService
 * @see MessageService
 */
@RestController
@RequestMapping(value = ApiPathConstant.CROCKERY_NOTES)
@Tag(name = SwaggerConstant.CROCKERY_NOTES)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CrockeryNotesController {

	/**
	 * Service for handling the business logic related to Crockery Notes.
	 */
	CrockeryNotesService crockeryNotesService;

	/**
	 * Service for managing messages, typically used for returning localized success/error messages.
	 */
	MessageService messageService;

	/**
	 * Creates a new Crockery Note.
	 * 
	 * @param crockNotesDto The data transfer object containing the details of the crockery note to be created.
	 * @return A response container containing the created Crockery Notes DTO with a success message.
	 * @throws RestException If any exception occurs during the process.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CROCKERY_NOTES + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.CROCKERY_NOTES + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<Optional<CrockeryNotesDto>> create(@Valid @RequestBody CrockeryNotesDto crockNotesDto) throws RestException {
		Optional<CrockeryNotesDto> crockeryNotesDto = crockeryNotesService.saveCrockeryNotes(crockNotesDto);
		return RequestResponseUtils.generateResponseDto(crockeryNotesDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves the details of the Crockery Notes.
	 * 
	 * @return A response container containing the Crockery Notes DTO.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CROCKERY_NOTES + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<CrockeryNotesDto> read() {
		return RequestResponseUtils.generateResponseDto(crockeryNotesService.getCrockeryNotes());
	}


}