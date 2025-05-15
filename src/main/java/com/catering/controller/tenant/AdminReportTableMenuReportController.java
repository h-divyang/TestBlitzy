package com.catering.controller.tenant;

import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.catering.constant.RegexConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dao.order_reports.admin_reports.AdminReportQueryDao;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.AdminReportTableMenuDto;
import com.catering.repository.tenant.OrderMenuPreparationMenuItemRepository;
import com.catering.service.common.MessageService;
import com.catering.util.DataUtils;
import com.catering.util.RequestResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller responsible for handling operations related to the Table Menu Report in the admin hub of order booking reports.
 * This controller provides end points for fetching and updating table menu details for a specific order.
 */
@RestController
@RequestMapping(value = ApiPathConstant.TABLE_MENU_REPORT)
@Tag(name = SwaggerConstant.TABLE_MENU_REPORT)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminReportTableMenuReportController {

	/**
	 * An instance of the AdminReportQueryDao interface used for executing various query operations related to table menu details.
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
	 * Retrieves a list of menu items grouped per order for a given order ID and function IDs.
	 * The method fetches report details for the table menu based on the input parameters.
	 *
	 * @param orderId A string representing the order ID. Must match the pattern of only numeric characters.
	 * @param functionId An optional array of Long values representing function IDs used to filter the report data.
	 * @return A ResponseContainerDto containing a list of AdminReportTableMenuDto objects that represent the table menu report details.
	 */
	@GetMapping(value = ApiPathConstant.TABLE_MENU_STRING)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<AdminReportTableMenuDto>> getTableMenuMenuItemsPerOrder(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId) {
		List<Long> functionIds = Arrays.asList(functionId);
		return RequestResponseUtils.generateResponseDto(adminReportQueryDao.getTableMenuReportDetails(Long.parseLong(orderId), functionIds));
	}

	/**
	 * Updates or saves the details of table menu items based on the provided list of {@code AdminReportTableMenuDto}.
	 *
	 * @param menuExecutionNamePlateDto A list of {@code AdminReportTableMenuDto} objects containing the details of the
	 * table menu items to be updated or saved.
	 * @return a {@code ResponseContainerDto} containing the updated list of {@code AdminReportTableMenuDto} and a message indicating the success of the operation.
	 */
	@PutMapping(value = ApiPathConstant.UPDATE_TABLE_MENU)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<List<AdminReportTableMenuDto>> saveOrUpdateTableMenuDetails(@RequestBody List<AdminReportTableMenuDto> menuExecutionNamePlateDto) {
		menuExecutionNamePlateDto.forEach(namePlateDto -> orderMenuPreparationMenuItemRepository.updateMenuItemVisiblityInTableMenuReport(namePlateDto.getRepeatNumber(), namePlateDto.getId(), DataUtils.getCurrentAuditor().get().getId(), namePlateDto.getIsMenuItemSelected(), namePlateDto.getCounterPlateMenuItemNameDefaultLang(), namePlateDto.getCounterPlateMenuItemNamePreferLang(), namePlateDto.getCounterPlateMenuItemNameSupportiveLang()));
		return RequestResponseUtils.generateResponseDto(menuExecutionNamePlateDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

}