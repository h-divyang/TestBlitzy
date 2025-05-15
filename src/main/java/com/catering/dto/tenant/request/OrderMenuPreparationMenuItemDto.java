package com.catering.dto.tenant.request;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.AuditIdDto;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class OrderMenuPreparationMenuItemDto extends AuditIdDto {

	@JsonBackReference
	@NotNull(message = MessagesConstant.VALIDATION_MENU_PREPARATION_REQUIRED)
	private OrderMenuPreparationDto menuPreparation;

	@NotNull(message = MessagesConstant.VALIDATION_MENU_PREPARATION_MENU_ITEM_REQUIRED)
	private MenuItemForMenuPreparationDto menuItem;

	@NotNull(message = MessagesConstant.VALIDATION_MENU_PREPARATION_MENU_ITEM_CATEGORY_REQUIRED)
	private MenuItemCategoryDto menuItemCategory;

	private String noteDefaultLang;

	private String notePreferLang;

	private String noteSupportiveLang;

	private String menuItemNameDefaultLang;

	private String menuItemNamePreferLang;

	private String menuItemNameSupportiveLang;

	private Double rupees;

	private Integer orderType;

	private LocalDateTime orderDate;

	private Integer person;

	private Double total;

	private ContactResponseDto fkContactId;

	private Integer counterNo;

	private Double counterPrice;

	private Integer helperNo;

	private Double helperPrice;

	private Double price;

	private Double quantity;

	private MeasurementDto unit;

	private GodownDto godown;

	private Double calculatedMenuItemAndRawMaterial;

	private Integer menuItemCategorySequence;

	private Integer menuItemSequence;

	private Boolean isPlate;

	public MenuItemForMenuPreparationDto getMenuItem() {
		if (Objects.nonNull(menuItem)) {
			menuItem.setOrderMenuPreparationMenuItemId(getId());
			menuItem.setNoteDefaultLang(noteDefaultLang);
			menuItem.setNotePreferLang(notePreferLang);
			menuItem.setNoteSupportiveLang(noteSupportiveLang);
			menuItem.setMenuItemNameDefaultLang(menuItemNameDefaultLang);
			menuItem.setMenuItemNamePreferLang(menuItemNamePreferLang);
			menuItem.setMenuItemNameSupportiveLang(menuItemNameSupportiveLang);
			menuItem.setRupees(rupees);
		}
		return menuItem;
	}

}