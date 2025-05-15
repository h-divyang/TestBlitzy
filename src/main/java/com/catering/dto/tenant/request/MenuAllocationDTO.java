package com.catering.dto.tenant.request;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.constant.MessagesFieldConstants;
import com.catering.dto.audit.IdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuAllocationDTO extends IdDto {

	private Integer orderType;

	@NotNull(message = MessagesFieldConstants.BOOK_ORDER_FIELD_DATE + MessagesConstant.VALIDATION_IS_REQUIRED)
	private LocalDateTime orderDate;

	@NotNull(message = MessagesFieldConstants.BOOK_ORDER_FIELD_PERSON + MessagesConstant.VALIDATION_NOT_BLANK)
	private Integer person;

	private Long godown;

	private List<MenuAllocationTypeDto> allocationType;

}