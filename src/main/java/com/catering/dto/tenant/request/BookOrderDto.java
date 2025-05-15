package com.catering.dto.tenant.request;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.catering.constant.MessagesConstant;
import com.catering.constant.MessagesFieldConstants;
import com.catering.constant.RegexConstant;
import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class BookOrderDto extends AuditIdDto {

	@NotNull(message = MessagesConstant.VALIDATION_BOOK_ORDER_CUSTOMER_REQUIRED)
	private GetBookOrderContactDto contactCustomer;

	private CommonDataForDropDownDto contactManager;

	private CommonDataForDropDownDto contactChef;

	@NotBlank(message = MessagesFieldConstants.BOOK_ORDER_FIELD_VENUE + MessagesConstant.VALIDATION_NOT_BLANK)
	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_VENUE + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_VENUE + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String venueDefaultLang;

	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_VENUE + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_VENUE + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String venuePreferLang;

	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_VENUE + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_VENUE + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String venueSupportiveLang;

	@Size(min = 2, message = MessagesFieldConstants.ORDER_PARTY_PLOT_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 50, message = MessagesFieldConstants.ORDER_PARTY_PLOT_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_50)
	private String partyPlotNameDefaultLang;

	@Size(min = 2, message = MessagesFieldConstants.ORDER_PARTY_PLOT_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 50, message = MessagesFieldConstants.ORDER_PARTY_PLOT_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_50)
	private String partyPlotNamePreferLang;

	@Size(min = 2, message = MessagesFieldConstants.ORDER_PARTY_PLOT_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 50, message = MessagesFieldConstants.ORDER_PARTY_PLOT_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_50)
	private String partyPlotNameSupportiveLang;

	@Pattern(regexp = RegexConstant.URL, message = MessagesConstant.VALIDATION_MOBILE_NOT_VALID)
	private String locationUrl;

	@NotNull(message = MessagesConstant.VALIDATION_BOOK_ORDER_EVENT_TYPE_REQUIRED)
	private EventTypeDto eventType;

	@Size(min = 2, message = MessagesFieldConstants.REFERENCE_BY + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 50, message = MessagesFieldConstants.REFERENCE_BY + MessagesConstant.VALIDATION_MAX_LENGTH_50)
	private String referenceBy;

	private LocalDate meetingDate;

	@NotNull(message = MessagesConstant.VALIDATION_BOOK_ORDER_EVENT_MAIN_DATE_REQUIRED)
	private LocalDate eventMainDate;

	@NotNull(message = MessagesConstant.VALIDATION_BOOK_ORDER_MEAL_TYPE_REQUIRED)
	private MealTypeDto mealType;

	@NotNull(message = MessagesConstant.VALIDATION_BOOK_ORDER_STATUS_REQUIRED)
	private CommonMultiLanguageDto status;

	private Boolean isAdjustQuantity;

	private Boolean isMenuPrepared;

	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_MEAL_NOTE + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_MEAL_NOTE + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String mealNoteDefaultLang;

	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_MEAL_NOTE + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_MEAL_NOTE + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String mealNotePreferLang;

	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_MEAL_NOTE + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_MEAL_NOTE + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String mealNoteSupportiveLang;

	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_GROOM_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_GROOM_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String groomName;

	private LocalDate groomBirthDate;

	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_GROOM_COMMUNITY + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_GROOM_COMMUNITY + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String groomCommunity;

	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_BRIDE_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_BRIDE_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String brideName;

	private LocalDate brideBirthDate;

	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_BRIDE_COMMUNITY + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_BRIDE_COMMUNITY + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String brideCommunity;

	@Valid
	private List<OrderFunctionDto> functions;

	private Long copiedOrderId;

}