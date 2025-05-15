package com.catering.dto.tenant.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.catering.constant.MessagesConstant;
import com.catering.constant.MessagesFieldConstants;
import com.catering.dto.audit.AuditIdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a DTO (Data Transfer Object) for package details.
 * This class contains information about a package, including its name, price, and item categories.
 *
 * It extends the AuditByIdModel class to inherit the auditing properties.
 *
 * @author Krushali Talaviya
 * @since July 2023
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPackageDetailsDto extends AuditIdDto {

	/**
	 * The default language name of the package.
	 */
	@NotBlank(message = MessagesConstant.VALIDATION_NAME_NOT_BLANK)
	@Size(max = 255, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	private String nameDefaultLang;

	/**
	 * The preferred language name of the package.
	 */
	@Size(max = 255, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	private String namePreferLang;

	/**
	 * The supportive language name of the package.
	 */	
	@Size(max = 255, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	private String nameSupportiveLang;

	/**
	 * The price of the package.
	 */
	@NotNull(message = MessagesConstant.VALIDATION_PACKAGE_PRICE_NOT_NULL)
	private Double price;

	/**
	 * The total number of items in the package.
	 */
	private Long totalItems;

	private List<MenuItemCategoryJoinWithPackageMenuItemCategoryDto> packageMenuItemCategoryList;

}