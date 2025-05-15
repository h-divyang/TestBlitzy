package com.catering.dto.audit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * The IdDto class represents a data transfer object (DTO) for holding an identifier with an active status.
 * It provides getters and setters for accessing and modifying the id and isActive properties.
 *
 * @author Krushali Talaviya
 * @since June 2023
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class IdDto {

	private Long id;

	private Boolean isActive;

}