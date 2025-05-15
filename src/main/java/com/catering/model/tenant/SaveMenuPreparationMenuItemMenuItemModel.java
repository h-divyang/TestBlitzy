package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "menu_item")
public class SaveMenuPreparationMenuItemMenuItemModel extends AuditIdModelOnly {

	@Column(name = "is_outside_labour")
	private Integer isOutsideLabour;

	@Column(name = "fk_contact_agency_id")
	private Long contactAgencyId;

	@Column(name = "quantity")
	private Double quantity;

	@Column(name = "price_outside_labour")
	private Double priceOutsideLabour;

	@Column(name ="helper")
	private Integer helper;

	@Column(name = "helper_price")
	private Double helperPrice;

	@Column(name = "fk_measurement_id")
	private Long measurementId;

	@Column(name = "price")
	private Double price;

	@Column(name = "is_plate")
	private Boolean isPlate = false;

}