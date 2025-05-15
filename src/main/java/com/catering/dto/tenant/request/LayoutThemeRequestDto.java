package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LayoutThemeRequestDto extends ColorThemeRequestDto {

	private Boolean isDarkTheme;

}