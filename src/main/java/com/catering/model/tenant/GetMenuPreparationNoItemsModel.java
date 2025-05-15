package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "order_no_items")
public class GetMenuPreparationNoItemsModel extends AuditIdModelOnly {

	@Column(name = "fk_order_menu_preparation_id")
	private Long menuPreparation;

	@Column(name = "fk_raw_material_id")
	private Long rawMaterial;

}