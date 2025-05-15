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
@Table(name = "order_menu_allocation_type")
public class GetMenuAllocationTypeModel extends AuditIdModelOnly {

	@Column(name = "fk_order_menu_preparation_menu_item_id")
	private Long menuPreparationMenuItem;

	@Column(name = "fk_contact_id", updatable = false)
	private Long fkContactId;

	@Column(name = "counter_no", updatable = false)
	private Integer counterNo;

	@Column(name = "counter_price", updatable = false)
	private Double counterPrice;

	@Column(name = "helper_no", updatable = false)
	private Integer helperNo;

	@Column(name = "helper_price", updatable = false)
	private Double helperPrice;

	@Column(name = "price", updatable = false)
	private Double price;

	@Column(name = "quantity", updatable = false)
	private Double quantity;

	@Column(name = "total", updatable = false)
	private Double total;

	@Column(name = "fk_unit_id", updatable = false)
	private Long unit;

	@Column(name = "is_plate", updatable = false)
	private Boolean isPlate;

}