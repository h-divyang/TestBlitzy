package com.catering.model.tenant;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a Meal Type Model.
 * It contains information about the meal type name.
 *
 * @author Krushali Talaviya
 * @since June 2023
 *
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meal_type")
public class MealTypeModel extends AuditByIdModel {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	@OneToMany(mappedBy = "mealType", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MealTypeNoItemsModel> mealTypeNoItems;

	/**
	 * Creates a new instance of the MealTypeModel class based on a search query.
	 *
	 * @param query The search query used to initialize the model's name fields.
	 * @return A new MealTypeModel object with name fields populated using the provided query.
	 */
	public static MealTypeModel ofSearchingModel(String query) {
		return MealTypeModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.build();
	}

}