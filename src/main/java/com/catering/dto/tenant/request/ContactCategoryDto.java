package com.catering.dto.tenant.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
public class ContactCategoryDto extends CommonMultiLanguageDto {

	@NotNull(message = MessagesConstant.VALIDATION_CONTACT_CATEGORY_TYPE_NOT_BLANK)
	private ContactCategoryTypeDto contactCategoryType;

	private Boolean displayLabourRecord;

	private Integer isNonUpdatable;

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_ITEM_PRIORITY_VALID)
	private String priority;

	public Integer getPriority() {
		if (NumberUtils.isDigits(priority)) {
			return Integer.valueOf(priority);
		}
		return null;
	}

}