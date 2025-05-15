package com.catering.dto.tenant.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MenuItemCategoryAndMenuItemDto extends MenuItemCategoryDto {

	@JsonManagedReference
	private List<MenuItemForMenuPreparationDto> menuItems;

}