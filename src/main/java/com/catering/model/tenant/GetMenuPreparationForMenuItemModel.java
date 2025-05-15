package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.catering.interfaces.Priority;
import com.catering.model.audit.AuditIdModelOnly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menu_item")
public class GetMenuPreparationForMenuItemModel extends AuditIdModelOnly implements Priority {

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "priority")
	private Integer priority;

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	@Column(name = "fk_menu_item_category_id")
	private Long menuItemCategory;

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

	@Column(name = "fk_contact_agency_id")
	private Long contactAgencyId;

	@Column(name = "fk_godown_id")
	private Long godown;

	@Column(name = "price")
	private Double price;

	@Column(name = "fk_measurement_id")
	private Long measurementId;

	@Column(name = "is_plate")
	private Boolean isPlate;

	@Transient
	private String image;

}