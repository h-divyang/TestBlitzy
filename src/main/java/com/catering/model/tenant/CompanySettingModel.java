package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company_setting")
public class CompanySettingModel extends AuditByIdModel {

	@Column(name = "decimal_limit_for_currency")
	private int decimalLimitForCurrency;

	@Column(name = "date_format")
	private String dateFormat;

	@Column(name = "is_24_hour")
	private Boolean is24Hour;

	@Column(name = "time_zone")
	private String timeZone;

	@Column(name ="counter_name_plat_report_size")
	private Byte reportSize;

	@Column(name ="pagination_page_sizes")
	private int paginationPageSizes;

	@Column(name ="two_language_default_lang")
	private Integer twoLanguageDefaultLang;

	@Column(name ="two_language_preferred_lang")
	private Integer twoLanguagePreferredLang;

	@Column(name = "is_mobile_number_unique")
	private Boolean isMobileNumberUnique;

	@Column(name = "display_crockery_and_general_fix")
	private Boolean displayCrockeryAndGeneralFix;

	@Column(name = "is_auto_time_raw_material")
	private Boolean isAutoTimeRawMaterial;

	@Column(name = "is_menu_preparation_toggle_enabled", updatable = false)
	private Boolean isMenuPreparationToggleEnabled;

	@Column(name = "is_menu_item_price_visible", updatable = false)
	private Boolean isMenuItemPriceVisible;

	@Column(name = "is_adjust_quantity")
	private Boolean isAdjustQuantity;

	@Column(name = "is_end_date")
	private Boolean isEndDate;

	@Column(name = "is_menu_item_unique")
	private Boolean isMenuItemUnique;

	@Column(name = "is_dynamic_design")
	private Boolean isDynamicDesign;

	@Column(name = "font_colour")
	private String fontColour;

	@Column(name = "is_dark_theme", updatable = false)
	private Boolean isDarkTheme;

	@Column(name = "colour_theme", updatable = false)
	private String colourTheme;

	@Column(name = "background_colour")
	private String backgroundColour;

	@Column(name = "extra_left_margin_report")
	private int extraLeftMarginReport;

	@Column(name = "is_display_pop_up")
	private Boolean isDisplayPopUp;

	@Column(name = "is_display_records_priority_wise")
	private Boolean isDisplayRecordsPriorityWise;

}