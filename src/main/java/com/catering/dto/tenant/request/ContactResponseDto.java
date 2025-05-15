package com.catering.dto.tenant.request;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.catering.annotation.Email;
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
public class ContactResponseDto extends AuditIdDto {

	@NotBlank(message = MessagesConstant.VALIDATION_NAME_NOT_BLANK)
	@Size(max = 50, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_50)
	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	private String nameDefaultLang;

	@Size(max = 50, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_50)
	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	private String namePreferLang;

	@Size(max = 50, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_50)
	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	private String nameSupportiveLang;

	@Size(min = 1, message = MessagesFieldConstants.CONTACT_CATEGORY_FIELD_SHORT_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_1)
	@Size(max = 5, message = MessagesFieldConstants.CONTACT_CATEGORY_FIELD_SHORT_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_5)
	private String shortNameDefaultLang;

	@Size(min = 1, message = MessagesFieldConstants.CONTACT_CATEGORY_FIELD_SHORT_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_1)
	@Size(max = 5, message = MessagesFieldConstants.CONTACT_CATEGORY_FIELD_SHORT_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_5)
	private String shortNamePreferLang;

	@Size(min = 1, message = MessagesFieldConstants.CONTACT_CATEGORY_FIELD_SHORT_NAME + MessagesConstant.VALIDATION_MIN_LENGTH_1)
	@Size(max = 5, message = MessagesFieldConstants.CONTACT_CATEGORY_FIELD_SHORT_NAME + MessagesConstant.VALIDATION_MAX_LENGTH_5)
	private String shortNameSupportiveLang;

	@Size(min = 2, message = MessagesFieldConstants.CONTACT_CATEGORY_FIELD_HOME_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.CONTACT_CATEGORY_FIELD_HOME_ADDRESS + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String homeAddressDefaultLang;

	@Size(min = 2, message = MessagesFieldConstants.CONTACT_CATEGORY_FIELD_HOME_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.CONTACT_CATEGORY_FIELD_HOME_ADDRESS + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String homeAddressPreferLang;

	@Size(min = 2, message = MessagesFieldConstants.CONTACT_CATEGORY_FIELD_HOME_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.CONTACT_CATEGORY_FIELD_HOME_ADDRESS + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String homeAddressSupportiveLang;

	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_VENUE + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_VENUE + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String venueDefaultLang;

	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_VENUE + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_VENUE + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String venuePreferLang;

	@Size(min = 2, message = MessagesFieldConstants.BOOK_ORDER_FIELD_VENUE + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.BOOK_ORDER_FIELD_VENUE + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String venueSupportiveLang;

	@Email(isMandatory = false)
	@Size(max = 70, message = MessagesFieldConstants.COMMON_FIELD_EMAIL + MessagesConstant.VALIDATION_MAX_LENGTH_70)
	private String email;

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_MOBILE_NOT_VALID)
	@NotBlank(message = MessagesFieldConstants.COMMON_FIELD_MOBILE_NUMBER + MessagesConstant.VALIDATION_NOT_BLANK)
	@Size(max = 10, min = 10, message = MessagesFieldConstants.COMMON_FIELD_MOBILE_NUMBER + MessagesConstant.VALIDATION_LENGTH_10_ONLY)
	private String mobileNumber;

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_MOBILE_NOT_VALID)
	@Size(max = 10, min = 10, message = MessagesFieldConstants.CONTACT_CATEGORY_FIELD_ALTERNATIVE_NUMBER + MessagesConstant.VALIDATION_LENGTH_10_ONLY)
	private String alternativeNumber;

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_MOBILE_NOT_VALID)
	@Size(max = 17, message = MessagesConstant.VALIDATION_OFFICE_NUMBER_MAX)
	@Size(min = 10, message = MessagesConstant.VALIDATION_OFFICE_NUMBER_MIN)
	private String officeNumber;

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_MOBILE_NOT_VALID)
	@Size(max = 10, min = 10, message = MessagesFieldConstants.CONTACT_CATEGORY_FIELD_PHONE_NUMBER + MessagesConstant.VALIDATION_LENGTH_10_ONLY)
	private String phoneNumber;

	@Pattern(regexp = RegexConstant.GST, message = MessagesConstant.VALIDATION_GST_INVALID)
	private String gstNumber;

	@Pattern(regexp = RegexConstant.PAN, message = MessagesConstant.VALIDATION_PAN_INVALID)
	private String panNumber;

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_AADHAR_NOT_VALID)
	@Size(min = 12, max = 12, message = MessagesFieldConstants.CONTACT_CATEGORY_FIELD_AADHAR_NUMBER + MessagesConstant.VALIDATION_LENGTH_12_ONLY)
	private String aadharNumber;

	private LocalDate birthDate;

	private LocalDate anniversaryDate;

	@Digits(integer = 8, fraction = 4, message = MessagesConstant.VALIDATION_8_DIGITS_4_DECIMAL)
	private Double labourPrice;

	@Digits(integer = 8, fraction = 4, message = MessagesConstant.VALIDATION_8_DIGITS_4_DECIMAL)
	private Double openingBalance;

	private Integer transactionType;

	private LocalDate lockDate;

	private Integer isCash;

	private Double currentBalanceForCashPayment;

	private List<ContactCategoryMappingDto> categoryMapping;

}