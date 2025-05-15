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
import com.catering.dto.tenant.request.RawMaterialCategoryTypeDto;
import com.catering.service.tenant.RawMaterialCategoryTypeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * REST controller for managing raw material category types.
 * This controller exposes an endpoint for retrieving all raw material category types,
 * with optional filtering. It ensures that only authorized users can access the data.
 */
@RestController
@RequestMapping(value = ApiPathConstant.RAW_MATERIAL_CATEGORY_TYPE_TENANT)
@Tag(name = SwaggerConstant.RAW_MATERIAL_CATEGORY_TYPE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RawMaterialCategoryTypeController {

	/**
	 * Service for managing raw material category types, including retrieval
	 * with filtering options.
	 */
	RawMaterialCategoryTypeService rawMaterialCategoryTypeService;

	/**
	 * Endpoint to retrieve all raw material category types, optionally filtered.
	 * Only users with appropriate permissions can access the raw material category types.
	 * 
	 * @param filterDto the filter criteria to apply when retrieving raw material category types.
	 * @return A ResponseContainerDto containing the list of raw material category types.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_CATEGORY + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.RAW_MATERIAL_CATEGORY + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<RawMaterialCategoryTypeDto>> read(FilterDto filterDto) {
		return rawMaterialCategoryTypeService.read(filterDto);
	}

}