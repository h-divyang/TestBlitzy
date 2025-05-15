package com.catering.model.tenant;

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
@Table(name = "meal_type_no_items")
public class MealTypeNoItemsModel extends AuditIdModelOnly {

	@JoinColumn(name = "fk_meal_type_id")
	@ManyToOne
	private MealTypeModel mealType;

	@JoinColumn(name = "fk_raw_material_id")
	@ManyToOne
	private RawMaterialModel rawMaterial;

}