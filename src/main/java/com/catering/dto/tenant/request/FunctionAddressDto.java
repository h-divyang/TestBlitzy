package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FunctionAddressDto {

	private Long id;

	private String functionAddressDefaultLang;

	private String functionAddressPreferLang;

	private String functionAddressSupportiveLang;

}