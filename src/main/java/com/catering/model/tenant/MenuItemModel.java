package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "menu_item")
public class MenuItemModel extends AuditByIdModel implements Priority {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	@Column(name = "slogan")
	private String slogan;

	@Column(name = "price")
	private Double price;

	@Column(name = "priority")
	private Integer priority;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_menu_item_category_id")
	private MenuItemCategoryModel menuItemCategory;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_menu_item_sub_category_id")
	private MenuItemSubCategoryModel menuItemSubCategory;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_kitchen_area_id")
	private KitchenAreaModel kitchenArea;

	@Column(name = "fk_godown_id")
	private Long godown;

	@Column(name = "is_outside_labour")
	private Integer isOutsideLabour;

	@Column(name = "quantity")
	private Double quantity;

	@Column(name = "price_outside_labour")
	private Double priceOutsideLabour;

	@Column(name ="helper")
	private Integer helper;

	@Column(name = "helper_price")
	private Double helperPrice;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_contact_category_id")
	private ContactCategoryModel contactCategory;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_contact_agency_id")
	private ContactModel contactResponse;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_measurement_id")
	private MeasurementModel measurement;

	@Column(name = "is_plate")
	private Boolean isPlate;

	/**
	 * Creates a new instance of {@link MenuItemModel}.
	 *
	 * This method is useful for creating a search model for menu item categories where the category name and priority 
	 * can be dynamically set based on the query input.
	 *
	 * @param query The search query that will populate the name and priority fields of the menu item category model.
	 * @return A new instance of {@link MenuItemModel} with the fields set according to the query value.
	 */
	public static MenuItemModel ofSearchingModel(String query) {
		return MenuItemModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.priority(NumberUtils.isDigits(query) ? Integer.parseInt(query) : null)
			.build();
	}

	/**
	 * Constructor to initialize a MenuItemModel instance with the specified ID.
	 * <p>
	 * This constructor is used to create a MenuItemModel object by setting its ID, 
	 * typically when you need to reference an existing menu item in the system.
	 * </p>
	 * 
	 * @param id The ID of the menu item to be set.
	 */
	public MenuItemModel(Long id) {
		this.setId(id);
	}

}