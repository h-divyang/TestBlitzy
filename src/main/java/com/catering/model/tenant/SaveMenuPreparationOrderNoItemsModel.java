package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_no_items")
public class SaveMenuPreparationOrderNoItemsModel extends AuditIdModelOnly {

	@ManyToOne
	@JoinColumn(name = "fk_order_menu_preparation_id")
	private SaveMenuPreparationModel menuPreparation;

	@Column(name = "fk_raw_material_id")
	private Long rawMaterial;

}