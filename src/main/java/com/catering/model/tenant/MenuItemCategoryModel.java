package com.catering.model.tenant;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang3.math.NumberUtils;

import com.catering.interfaces.Priority;
import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menu_item_category")
public class MenuItemCategoryModel extends AuditByIdModel implements Priority {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	@Column(name = "priority")
	private Integer priority;

	@OneToMany(mappedBy = "menuItemCategory", fetch = FetchType.LAZY)
	@OrderBy("priority")
	private List<MenuItemModel> menuItems;

	/**
	 * Creates a new instance of {@link MenuItemCategoryModel}.
	 *
	 * This method is useful for creating a search model for menu item categories where the category name and priority 
	 * can be dynamically set based on the query input.
	 *
	 * @param query The search query that will populate the name and priority fields of the menu item category model.
	 * @return A new instance of {@link MenuItemCategoryModel} with the fields set according to the query value.
	 */
	public static MenuItemCategoryModel ofSearchingModel(String query) {
		return MenuItemCategoryModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.priority(NumberUtils.isDigits(query) ? Integer.parseInt(query) : null)
			.build();
	}

	/**
	 * Constructor to initialize a MenuItemCategoryModel instance with the specified ID.
	 * <p>
	 * This constructor is used to create a MenuItemCategoryModel object by setting its ID, 
	 * typically when you need to reference an existing menu item category in the system.
	 * </p>
	 * 
	 * @param id The ID of the menu item category to be set.
	 */
	public MenuItemCategoryModel(Long id) {
		this.setId(id);
	}

}