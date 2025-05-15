package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.catering.constant.FieldConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_menu_preparation_menu_item")
public class MenuItemRupeesModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = FieldConstants.COMMON_FIELD_ID, updatable = false)
	private Long menuPreparationMenuItemId;

	@Column(name = "rupees")
	private Double rupees;

}