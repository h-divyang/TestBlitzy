package com.catering.dto.tenant.request;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderQuotationRequestDto extends AuditIdDto {

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private Long orderId;

	private Long taxMasterId;

	private Double discount;

	private Double advancePayment;

	private Double roundOff;

	private String grandTotal;

	private Boolean isRoughEstimation;

	@Size(max = 255, message = MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String remark;

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	List<OrderQuotationFunctionRequestDto> functions;

}