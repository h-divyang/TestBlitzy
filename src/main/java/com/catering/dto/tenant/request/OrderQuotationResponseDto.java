package com.catering.dto.tenant.request;

import java.util.List;

import com.catering.dto.audit.IdDto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class OrderQuotationResponseDto extends IdDto {

	private Long orderId;

	private Long taxMasterId;

	private Double discount;

	@Setter
	private Double advancePayment;

	private Double roundOff;

	private String grandTotal;

	private Double total;

	private String remark;

	private Boolean isRoughEstimation;

	@Setter
	private List<OrderQuotationFunctionResponseDto> functions;

	@Setter
	private Double functionTotal;

	public OrderQuotationResponseDto(Long id, Long orderId, Long taxMasterId, Double discount, Double advancePayment, Double roundOff, String grandTotal, Double total, String remark, Boolean isRoughEstimation) {
		setId(id);
		this.orderId = orderId;
		this.taxMasterId = taxMasterId;
		this.discount = discount;
		this.advancePayment = advancePayment;
		this.roundOff = roundOff;
		this.grandTotal = grandTotal;
		this.total = total;
		this.remark = remark;
		this.isRoughEstimation = isRoughEstimation;
	}

}