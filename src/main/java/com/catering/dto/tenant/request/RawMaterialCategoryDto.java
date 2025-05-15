package com.catering.dto.tenant.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.math.NumberUtils;

import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RawMaterialCategoryDto extends CommonMultiLanguageDto {

	@NotNull(message = MessagesConstant.VALIDATION_RAW_MATERIAL_CATEGORY_TYPE_NOT_BLANK)
	private RawMaterialCategoryTypeDto rawMaterialCategoryType;

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_ITEM_PRIORITY_VALID)
	private String priority;

	private Boolean isDirectOrder;

	public Integer getPriority() {
		if (NumberUtils.isDigits(priority)) {
			return Integer.valueOf(priority);
		}
		return null;
	}

}