package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderFunctionRawMaterialTimeDto {

	private Long id;

	private LocalDateTime rawMaterialTime;

}
