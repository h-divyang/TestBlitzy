package com.catering.dto.tenant.request;

import java.util.ArrayList;
import java.util.Objects;

import com.catering.model.tenant.MenuItemCategoryModel;
import com.catering.model.tenant.PackageMenuItemCategoryModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuItemCategoryJoinWithPackageMenuItemCategoryDto {
	/**
	 * The identifier of the object.
	 */
	private Long id;

	/**
	 * The menu item category associated with this object.
	 */
	private MenuItemCategoryAndMenuItemDto menuItemCategory;

	/**
	 * The number of items in this category.
	 */
	private Long numberOfItems;

	public MenuItemCategoryJoinWithPackageMenuItemCategoryDto(MenuItemCategoryModel menuItemCategoryModel, Long numberOfItems, PackageMenuItemCategoryModel packagMenuItemCategoryModel) {
		// Initialize the final item category DTO
		this.menuItemCategory = MenuItemCategoryAndMenuItemDto.builder().id(menuItemCategoryModel.getId())
																.nameDefaultLang(menuItemCategoryModel.getNameDefaultLang())
																.namePreferLang(menuItemCategoryModel.getNamePreferLang())
																.nameSupportiveLang(menuItemCategoryModel.getNameSupportiveLang())
																.build();
		this.numberOfItems = numberOfItems;
		this.id = Objects.nonNull(packagMenuItemCategoryModel) ? packagMenuItemCategoryModel.getId() : null;

		menuItemCategoryModel.getMenuItems().forEach(menuItem -> {
			if (Objects.nonNull(this.menuItemCategory)) {
				if (Objects.isNull(this.menuItemCategory.getMenuItems())) {
					this.menuItemCategory.setMenuItems(new ArrayList<>());
				}
				this.menuItemCategory.getMenuItems().add(MenuItemForMenuPreparationDto.builder()
						.nameDefaultLang(menuItem.getNameDefaultLang())
						.namePreferLang(menuItem.getNamePreferLang())
						.nameSupportiveLang(menuItem.getNameSupportiveLang())
						.id(menuItem.getId())
						.build());
			}
		});
	}

}