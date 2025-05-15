package com.catering.dto.tenant.request;

import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.math.NumberUtils;

import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.model.tenant.MenuItemModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuItemDto extends CommonMultiLanguageDto {

	private String slogan;

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_ITEM_PRIORITY_VALID)
	private String priority;

	@Digits(integer = 8, fraction = 4, message = MessagesConstant.VALIDATION_8_DIGITS_4_DECIMAL)
	private Double price;

	@NotNull(message = MessagesConstant.VALIDATION_MENU_ITEM_CATEGORY_NOT_BLANK)
	private MenuItemCategoryDto menuItemCategory;

	private MenuItemSubCategoryDto menuItemSubCategory;

	private KitchenAreaDto kitchenArea;

	private Long godown;

	private List<MenuItemRawMaterialDto> menuItemRawMaterialList;

	private List<NameDto> rawMaterials;

	private Integer isOutsideLabour;

	@Digits(integer = 8, fraction = 4, message = MessagesConstant.VALIDATION_8_DIGITS_4_DECIMAL)
	private Double quantity;

	@Digits(integer = 8, fraction = 4, message = MessagesConstant.VALIDATION_8_DIGITS_4_DECIMAL)
	private Double priceOutsideLabour;

	@Digits(integer = 5, fraction = 0, message = MessagesConstant.VALIDATION_HELPER_MAX_5_LENGTH)
	private Integer helper;

	@Digits(integer = 8, fraction = 4, message = MessagesConstant.VALIDATION_8_DIGITS_4_DECIMAL)
	private Double helperPrice;

	private ContactCategoryDto contactCategory;

	private ContactResponseDto contactResponse;

	private MeasurementDto measurement;

	// Order Menu Preparation fields
	private Long orderMenuPreparationFinalMaterialId;

	private String orderMenuPreparationNote;

	private boolean isSelected;

	private String image;

	private Boolean isPlate;

	public Integer getPriority() {
		if (NumberUtils.isDigits(priority)) {
			return Integer.valueOf(priority);
		}
		return null;
	}

	public MenuItemDto(MenuItemModel menuMaterialModel) {
		this.nameDefaultLang = menuMaterialModel.getNameDefaultLang();
		this.namePreferLang = menuMaterialModel.getNamePreferLang();
		this.nameSupportiveLang = menuMaterialModel.getNameSupportiveLang();
		this.setId(menuMaterialModel.getId());
	}

}