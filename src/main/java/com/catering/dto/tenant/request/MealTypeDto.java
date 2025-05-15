package com.catering.dto.tenant.request;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a Meal Type DTO (Data Transfer Object) used for transferring meal type data between layers.
 * It contains information about the meal type name.
 * 
 * @author Krushali Talaviya
 * @since June 2023
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class MealTypeDto extends CommonMultiLanguageDto {

	@Valid
	@JsonManagedReference
	private List<MealTypeNoItemsDto> mealTypeNoItems;

}