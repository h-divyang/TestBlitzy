package com.catering.model.tenant;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;

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
@Table(name = "order_no_items")
public class OrderNoItemsModel extends AuditIdModelOnly {

	@JoinColumn(name = "fk_order_menu_preparation_id")
	@ManyToOne
	private OrderMenuPreparationModel menuPreparation;

	@JoinColumn(name = "fk_raw_material_id")
	@ManyToOne
	private RawMaterialModel rawMaterial;

}