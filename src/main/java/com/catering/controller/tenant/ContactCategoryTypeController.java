package com.catering.controller.tenant;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.ContactCategoryTypeDto;
import com.catering.service.tenant.ContactCategoryTypeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for managing Contact Category Types, providing endpoints to retrieve contact category type details.
 * <p>
 * The following API endpoints are available:
 * <ul>
 *     <li><b>GET /contact-category-type-tenant</b> - Retrieves a list of all Contact Category Types. Requires add or edit permissions.</li>
 * </ul>
 * </p>
 * This controller handles interactions related to Contact Category Types, such as retrieving their details based on filters.
 * The methods leverage {@link ContactCategoryTypeService} to perform the necessary business logic and retrieve the data.
 * All API endpoints enforce user rights based on roles and permissions defined in {@link ApiUserRightsConstants}.
 * <p>
 * @see ContactCategoryTypeService
 * </p>
 */
@RestController
@RequestMapping(value = ApiPathConstant.CONTACT_CATEGORY_TYPE_TENANT)
@Tag(name = SwaggerConstant.CONTACT_CATEGORY_TYPE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactCategoryTypeController {

	/**
	 * Service to manage operations related to Contact Category Types.
	 * <p>
	 * This service is responsible for handling the business logic associated with retrieving, creating, updating, and deleting contact category types.
	 * It interacts with the repository or data access layer to fetch and manipulate the data related to contact category types.
	 * </p>
	 */
	ContactCategoryTypeService contactCategoryTypeService;

	/**
	 * Retrieves all Contact Category Types.
	 * <p>
	 * This endpoint retrieves a list of all contact category types. The user must have permission to add or edit contact categories, as specified in {@link ApiUserRightsConstants}.
	 * </p>
	 * 
	 * @param filterDto an optional filter object to filter the contact category types.
	 * @return a response containing the list of contact category types.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CONTACT_CATEGORY + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.CONTACT_CATEGORY + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<ContactCategoryTypeDto>> read(FilterDto filterDto) {
		return contactCategoryTypeService.read(filterDto);
	}

}