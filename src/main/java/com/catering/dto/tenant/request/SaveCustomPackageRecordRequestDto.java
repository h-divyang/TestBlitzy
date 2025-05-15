package com.catering.dto.tenant.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a Data Transfer Object (DTO) for Custom Package request for data save or update.
 * This DTO is used for transferring data related to Custom Package request for data save or update.
 *
 * @author Priyansh Patel
 *
 */
@Getter
@Setter
public class SaveCustomPackageRecordRequestDto extends CommonMultiLanguageDto {

	@NotNull(message = MessagesConstant.VALIDATION_PACKAGE_PRICE_NOT_NULL)
	private Double price;

	private List<SaveCustomPackageRecordMenuItemCategoryRequestDto> packageMenuItemCategoryList;

	private List<SaveCustomPackageRecordMenuItemRequestDto> packageMenuItemsList;

}