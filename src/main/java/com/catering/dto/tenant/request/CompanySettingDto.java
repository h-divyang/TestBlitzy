package com.catering.dto.tenant.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.catering.constant.MessagesConstant;
import com.catering.constant.MessagesFieldConstants;
import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CompanySettingDto extends AuditIdDto {

	private int decimalLimitForCurrency;

	private String dateFormat;

	private Boolean is24Hour;

	private String timeZone;

	private Byte reportSize;

	private int paginationPageSizes;

	private int twoLanguageDefaultLang;

	private int twoLanguagePreferredLang;

	@Min(value = 0, message = MessagesFieldConstants.EXTRA_LEFT_MARGIN_REPORT + MessagesConstant.VALIDATION_MIN_VALUE_0)
	@Max(value = 50, message = MessagesFieldConstants.EXTRA_LEFT_MARGIN_REPORT + MessagesConstant.VALIDATION_MAX_VALUE_50)
	private int extraLeftMarginReport;

	private Boolean isMobileNumberUnique;

	private Boolean displayCrockeryAndGeneralFix;

	private Boolean isAutoTimeRawMaterial;

	private Boolean isMenuPreparationToggleEnabled;

	private Boolean isMenuItemPriceVisible;

	private Boolean isAdjustQuantity;

	private Boolean isEndDate;

	private Boolean isMenuItemUnique;

	private Boolean isDynamicDesign;

	private String fontColour;

	private Boolean isDarkTheme;

	private String colourTheme;

	private String backgroundColour;

	private String backgroundImage;

	private Boolean isDisplayPopUp;

	private Boolean isDisplayRecordsPriorityWise;

}