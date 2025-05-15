package com.catering.dto.tenant.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
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

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class CompanyPreferencesDto extends AuditIdDto {

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_LANGUAGE_DEFAULT_NOT_VALID)
	private String defaultLang;

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_LANGUAGE_PREFER_NOT_VALID)
	private String preferLang;

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_LANGUAGE_SUPPORTIVE_NOT_VALID)
	private String supportiveLang;

	@NotBlank(message = MessagesConstant.VALIDATION_NAME_DEFAULT_LANGUAGE_NOT_BLANK)
	@Size(max = 50, message = MessagesConstant.VALIDATION_MAX_LENGTH_50)
	@Size(min = 2, message = MessagesConstant.VALIDATION_NAME_MIN_LENGTH)
	private String nameDefaultLanguage;

	@Size(max = 50, message = MessagesConstant.VALIDATION_MAX_LENGTH_50)
	@Size(min = 2, message = MessagesConstant.VALIDATION_NAME_MIN_LENGTH)
	private String namePreferLanguage;

	@Size(max = 50, message = MessagesConstant.VALIDATION_MAX_LENGTH_50)
	@Size(min = 2, message = MessagesConstant.VALIDATION_NAME_MIN_LENGTH)
	private String nameSupportiveLanguage;

	private String tagLine;

	@NotBlank(message = MessagesConstant.VALIDATION_ADDRESS_DEFAULT_NOT_BLANK)
	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 250, message = MessagesConstant.VALIDATION_ADDRESS_MAX_LENGTH)
	private String addressDefaultLanguage;

	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 250, message = MessagesConstant.VALIDATION_ADDRESS_MAX_LENGTH)
	private String addressPreferLanguage;

	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 250, message = MessagesConstant.VALIDATION_ADDRESS_MAX_LENGTH)
	private String addressSupportiveLanguage;

	@Pattern(regexp = RegexConstant.WEBSITE_VALIDATION, message = MessagesConstant.VALIDATION_WEBSITE_NOT_VALID)
	@Size(max = 2048, message = MessagesConstant.VALIDATION_WEBSITE_MAX_LENGTH)
	private String website;

	private String email;

	@NotBlank(message = MessagesConstant.VALIDATION_MOBILE_NUMBER_NOT_BLANK)
	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_MOBILE_NOT_VALID)
	@Size(max = 17, message = MessagesConstant.VALIDATION_MOBILE_MAX)
	@Size(min = 10, message = MessagesConstant.VALIDATION_MOBILE_MIN)
	private String mobileNumber;

	@Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.VALIDATION_OFFICE_NUMBER_NOT_VALID)
	@Size(max = 17, message = MessagesConstant.VALIDATION_OFFICE_NUMBER_MAX)
	@Size(min = 10, message = MessagesConstant.VALIDATION_OFFICE_NUMBER_MIN)
	private String officeNumber;

	@Pattern(regexp = RegexConstant.GST_NUMBER_VALIDATION, message = MessagesConstant.VALIDATION_GST_NUMBER_NOT_VALID)
	private String gst;

	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String invoiceAddressDefaultLanguage;

	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String invoiceAddressPreferLanguage;

	@Size(min = 2, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MIN_LENGTH_2)
	@Size(max = 255, message = MessagesFieldConstants.COMMON_FIELD_ADDRESS + MessagesConstant.VALIDATION_MAX_LENGTH_255)
	private String invoiceAddressSupportiveLanguage;

	private String logo;

	private LocalDate activeDate;

	private LocalDate dueDate;

	private Integer users;

	private Integer extraUsers;

	private Long subscriptionId;

	private int subscriptionType;

	private String foodLicenceNumber;

}