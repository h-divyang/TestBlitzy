package com.catering.controller.tenant;

import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.VoucherPaymentHistoryDto;
import com.catering.service.common.CommonService;
import com.catering.util.RequestResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class providing end points for common operations and utilities across the application.
 */
@RestController
@Tag(name = SwaggerConstant.COMMON)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommonController {

	/**
	 * Service providing common utilities and functionalities shared across the application.
	 */
	CommonService commonService;

	/**
	 * Retrieves the set of available time zone IDs.
	 *
	 * @return A ResponseContainerDto containing a set of strings representing available time zone IDs.
	 */
	@GetMapping(ApiPathConstant.TIME_ZONES)
	public ResponseContainerDto<Set<String>> getTimeZones() {
		return RequestResponseUtils.generateResponseDto(ZoneId.getAvailableZoneIds());
	}

	/**
	 * Updates the side bar caching for a specific unique code.
	 *
	 * @param uniqueCode The unique identifier used to update the side bar caching.
	 */
	@GetMapping(ApiPathConstant.UPDATE_SIDEBAR_CACHING)
	public void updateSidebarCaching(@PathVariable String uniqueCode) {
		commonService.updateCaching(uniqueCode);
	}

	/**
	 * Retrieves the payment history for a specific voucher.
	 *
	 * @param voucherType The type of the voucher to be queried.
	 * @param voucherNumber The number of the voucher to be queried.
	 * @return A ResponseContainerDto containing a list of VoucherPaymentHistoryDto objects representing the payment history of the specified voucher.
	 */
	@GetMapping(ApiPathConstant.VOUCHER_PAYMENT_HISTORY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DASHBOARD + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<VoucherPaymentHistoryDto>> readVoucherPaymentHistory(@RequestParam(required = true) int voucherType, @RequestParam(required = true) long voucherNumber) {
		return RequestResponseUtils.generateResponseDto(commonService.readVoucherPaymentHistory(voucherType, voucherNumber));
	}

}