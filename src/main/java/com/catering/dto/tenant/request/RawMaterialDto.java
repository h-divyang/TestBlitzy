package com.catering.dto.tenant.request;

import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.math.NumberUtils;

import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class RawMaterialDto extends CommonMultiLanguageDto {

	@NotNull(message = MessagesConstant.VALIDATION_MENU_ITEM_CATEGORY_NOT_BLANK)
	private RawMaterialCategoryDto rawMaterialCategory;

	@Digits(integer = 8, fraction = 4, message = MessagesConstant.VALIDATION_8_DIGITS_4_DECIMAL)
	private Double weightPer100Pax;

	@NotNull(message = MessagesConstant.VALIDATION_MEASUREMENT_NOT_BLANK)
	private MeasurementDto measurement;

	private List<RawMaterialSupplierDto> rawMaterialSupplierList;

	@Digits(integer = 8, fraction = 4, message = MessagesConstant.VALIDATION_8_DIGITS_4_DECIMAL)
	private Double supplierRate;

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_ITEM_PRIORITY_VALID)
	private String priority;

	private Boolean isGeneralFixRawMaterial;

	private Double openingBalance;

	private MeasurementDto opbMeasurement;

	private TaxMasterDto tax;

	@Size(max = 8, message = MessagesConstant.VALIDATION_HSN_CODE_MAX_8_LENGTH)
	private String hsnCode;

	public Integer getPriority() {
		if (NumberUtils.isDigits(priority)) {
			return Integer.valueOf(priority);
		}
		return null;
	}

}