package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name = "order_menu_allocation_type")
public class SaveCustomMenuAllocationTypeModel extends AuditIdModelOnly {

	@ManyToOne
	@JoinColumn(name = "fk_order_menu_preparation_menu_item_id")
	private SaveMenuAllocationOrderMenuPreparationMenuItemModel menuPreparationMenuItem;

	@Column(name = "total")
	private Double total;

	@Column(name = "fk_contact_id")
	private Long fkContactId;

	@Column(name = "counter_no")
	private Integer counterNo;

	@Column(name = "counter_price")
	private Double counterPrice;

	@Column(name = "helper_no")
	private Integer helperNo;

	@Column(name = "helper_price")
	private Double helperPrice;

	@Column(name = "price")
	private Double price;

	@Column(name = "quantity")
	private Double quantity;

	@Column(name = "fk_unit_id")
	private Long unit;

	@Column(name = "is_plate")
	private Boolean isPlate;

}