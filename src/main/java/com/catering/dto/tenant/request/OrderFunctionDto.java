package com.catering.dto.tenant.request;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.catering.constant.MessagesConstant;
import com.catering.constant.MessagesFieldConstants;
import com.catering.dto.audit.AuditIdDto;
import com.catering.dto.audit.IdDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class OrderFunctionDto extends AuditIdDto {

	@NotNull(message = MessagesConstant.VALIDATION_BOOK_ORDER_FUNCTION_TYPE_REQUIRED)
	private FunctionTypeDto functionType;

	@NotNull(message = MessagesFieldConstants.BOOK_ORDER_FIELD_PERSON + MessagesConstant.VALIDATION_NOT_BLANK)
	private Long person;

	@NotNull(message = MessagesFieldConstants.BOOK_ORDER_FIELD_DATE + MessagesConstant.VALIDATION_IS_REQUIRED)
	private LocalDateTime date;

	private LocalDateTime endDate;

	private Double rate;

	private Integer sequence;

	private Integer copiedFunctionSequence;

	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_NOTE + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_NOTE + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String noteDefaultLang;

	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_NOTE + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_NOTE + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String notePreferLang;

	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_NOTE + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_NOTE + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String noteSupportiveLang;

	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String functionAddressDefaultLang;

	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String functionAddressPreferLang;

	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MAX_LENGTH_255)	
	private String functionAddressSupportiveLang;

	private LocalDateTime rawMaterialTime;

	@JsonManagedReference
	List<OrderLabourDistributionDto> labours;

	@JsonManagedReference
	List<ExtraExpenseDto> extra;

	List<IdDto> orderMenuPreparations;

}