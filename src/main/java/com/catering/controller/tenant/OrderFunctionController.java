package com.catering.controller.tenant;

import com.catering.constant.ApiPathConstant;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.CommonNotesDto;
import com.catering.dto.tenant.request.FunctionAddressDto;
import com.catering.dto.tenant.request.OrderFunctionRawMaterialTimeDto;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.OrderFunctionService;
import com.catering.util.RequestResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for managing Order Function operations.
 * <p>
 * This controller provides APIs to handle various order function-related operations, 
 * including updating notes and function addresses.
 * </p>
 * 
 * <p>
 * The endpoints are secured with specific user rights, ensuring only authorized users 
 * can access or modify data.
 * </p>
 */
@RestController
@RequestMapping(value = ApiPathConstant.ORDER_FUNCTION)
@Tag(name = SwaggerConstant.ORDER_FUNCTION)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderFunctionController {

	/**
	 * Service to manage and retrieve localized messages for API responses.
	 */
	MessageService messageService;

	/**
	 * Service to handle order function-related operations.
	 */
	OrderFunctionService orderFunctionService;

	 /**
	 * Updates notes associated with an order function.
	 * <p>
	 * This endpoint allows users to update the notes for a specific order function, 
	 * ensuring accurate and up-to-date information.
	 * </p>
	 *
	 * @param commonNotesDto the notes data to update, encapsulated in a {@link CommonNotesDto}.
	 * @return a {@link ResponseContainerDto} with a success message upon completion.
	 */
	@PutMapping(value = ApiPathConstant.UPDATE_NOTE)
	public ResponseContainerDto<Object> updateNotes(@RequestBody CommonNotesDto commonNotesDto) {
		orderFunctionService.updateNotes(commonNotesDto);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Updates the address associated with a specific order function.
	 * <p>
	 * This endpoint allows users to update the function address for an order, 
	 * ensuring all location details are current and accurate.
	 * </p>
	 *
	 * @param functionAddressDto the new address data to update, encapsulated in a {@link FunctionAddressDto}.
	 * @return a {@link ResponseContainerDto} with a success message upon completion.
	 */
	@PutMapping(value = ApiPathConstant.UPDATE_FUNCTION_ADDRESS)
	public ResponseContainerDto<Object> updateFunctionAddress(@RequestBody FunctionAddressDto functionAddressDto) {
		orderFunctionService.updateFunctionAddress(functionAddressDto);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Updates the raw material time associated with a specific order function.
	 * <p>
	 * This end-point allows users to update the raw material time for an order specific function,
	 * ensuring accurate and up-to-date information.
	 * </p>
	 *
	 * @param orderFunctionRawMaterialTimeDto the new raw material time data to update, encapsulated in a {@link OrderFunctionRawMaterialTimeDto}.
	 * @return a {@link ResponseContainerDto} with a success message upon completion.
	 */
	@PutMapping(value = ApiPathConstant.UPDATE_RAW_MATERIAL_TIME)
	public ResponseContainerDto<Object> updateRawMaterialTime(@RequestBody OrderFunctionRawMaterialTimeDto orderFunctionRawMaterialTimeDto) {
		orderFunctionService.updateRawMaterialTime(orderFunctionRawMaterialTimeDto);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

}