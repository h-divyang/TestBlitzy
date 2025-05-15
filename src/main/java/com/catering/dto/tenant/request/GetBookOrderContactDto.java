package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetBookOrderContactDto extends CommonDataForDropDownDto {

	private String mobileNumber;

}