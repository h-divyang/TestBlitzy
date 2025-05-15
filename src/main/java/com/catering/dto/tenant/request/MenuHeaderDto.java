package com.catering.dto.tenant.request;

import com.catering.dto.audit.OnlyIdDto;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a Data Transfer Object (DTO) for the menu header.
 * This class extends the OnlyIdDto class and includes additional properties and functionalities.
 *
 * @author Krushali Talaviya
 * @since July 2023
 *
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MenuHeaderDto extends OnlyIdDto {

	/**
	 * The title of the menu header.
	 */
	private String title;

	/**
	 * The sequence number of the menu header.
	 */
	private int sequence;

	/**
	 * Indicates if the menu header is a group title.
	 */
	@Default
	private boolean groupTitle = false;

}