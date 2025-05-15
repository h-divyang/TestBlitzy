package com.catering.controller.tenant;

import java.util.List;
import javax.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dao.order_reports.admin_reports.AdminReportQueryDao;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.AdminReportNamePlateDto;
import com.catering.repository.tenant.OrderMenuPreparationMenuItemRepository;
import com.catering.service.common.MessageService;
import com.catering.util.DataUtils;
import com.catering.util.RequestResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller responsible for handling operations related to the Name Plate Report in the admin hub of order booking reports.
 * This controller provides end points for fetching and updating name plate details for a specific order.
 */
@RestController
@RequestMapping(value = ApiPathConstant.NAME_PLATE)
@Tag(name = SwaggerConstant.NAME_PLATE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminReportNamePlateReportController {

	/**
	 * An instance of the AdminReportQueryDao interface used for executing various query operations related to name plate details.
	 */
	AdminReportQueryDao adminReportQueryDao;

	/**
	 * The repository for interacting with the persistence layer of the OrderMenuPreparationMenuItemModel entity.
	 */
	OrderMenuPreparationMenuItemRepository orderMenuPreparationMenuItemRepository;

	/**
	 * Service interface responsible for managing and retrieving localized messages.
	 */
	MessageService messageService;

	/**
	 * Retrieves a list of MenuExecutionNamePlateDto objects representing final materials for a Name Plate Report
	 * for a specific order.
	 *
	 * @param orderId The ID of the order for which the final materials are retrieved.
	 * @return A ResponseContainerDto containing a list of MenuExecutionNamePlateDto objects.
	 */
	@GetMapping(value = ApiPathConstant.NAME_PLATE_STRING)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<AdminReportNamePlateDto>> getNamePlateMenuItemsPerOrder(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId) {
		return RequestResponseUtils.generateResponseDto(adminReportQueryDao.getMenuItemsForNamePlateReport(Long.parseLong(orderId)));
	}

	/**
	 * Saves or updates a list of MenuExecutionNamePlateDto objects and updates the repeat number for each item.
	 *
	 * @param menuExecutionNamePlateDto The list of MenuExecutionNamePlateDto objects to save or update.
	 * @return A ResponseContainerDto containing the updated list of MenuExecutionNamePlateDto objects and a success message.
	 */
	@PutMapping(value = ApiPathConstant.UPDATE_NAME_PLATE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<List<AdminReportNamePlateDto>> saveOrUpdateNamePlateDetails(@RequestBody List<AdminReportNamePlateDto> menuExecutionNamePlateDto) {
		menuExecutionNamePlateDto.forEach(namePlateDto -> orderMenuPreparationMenuItemRepository.updateRepeatNumber(namePlateDto.getRepeatNumber(), namePlateDto.getId(), DataUtils.getCurrentAuditor().get().getId(), namePlateDto.getCounterPlateMenuItemNameDefaultLang(), namePlateDto.getCounterPlateMenuItemNamePreferLang(), namePlateDto.getCounterPlateMenuItemNameSupportiveLang()));
		return RequestResponseUtils.generateResponseDto(menuExecutionNamePlateDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

}