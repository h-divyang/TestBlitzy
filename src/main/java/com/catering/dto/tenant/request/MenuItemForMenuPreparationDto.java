package com.catering.dto.tenant.request;

import java.util.List;

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
public class MenuItemForMenuPreparationDto extends AuditIdDto {

	@JsonBackReference
	private MenuItemCategoryDto menuItemCategory;

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

	private Double price;

	private Double rupees;

	private Long godown;

	private Integer isOutsideLabour;

	private Double quantity;

	private Double priceOutsideLabour;

	private Integer helper;

	private Double helperPrice;

	private Long orderMenuPreparationMenuItemId;

	private String noteDefaultLang;

	private String notePreferLang;

	private String noteSupportiveLang;

	private String menuItemNameDefaultLang;

	private String menuItemNamePreferLang;

	private String menuItemNameSupportiveLang;

	private ContactResponseDto contactResponse;

	private boolean isSelected;

	private String image;

	private MeasurementDto measurement;

	private List<NameDto> rawMaterials;

	private Long priority;

}