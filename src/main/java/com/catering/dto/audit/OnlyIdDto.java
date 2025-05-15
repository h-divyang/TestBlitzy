package com.catering.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * This class represents a Data Transfer Object (DTO) with only an ID field.
 * It is used for transferring data related to entities that have only an ID attribute.
 *
 * @author Krushali Talaviya
 * @since June 2023
 *
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OnlyIdDto {

	/**
	 * The ID of the entity.
	 */
	private Long id;

}