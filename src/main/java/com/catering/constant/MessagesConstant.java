package com.catering.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class holds constant values for message keys used in the application.
 * These constants are typically used for internationalization (i18n) or retrieving specific messages
 * from properties or resource bundles based on keys.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessagesConstant {

	/**
	 * Core Messages
	 */
	public static final String CORE_SOMETHING_WENT_WRONG = "{core.something-went-wrong}";
	public static final String ACCESS_DENIED = "{core.access-denied}";

	/**
	 * JWT Messages
	 */
	public static final String JWT_EXPIRED = "{jwt.expired}";
	public static final String JWT_INVALID = "{jwt.invalid}";
	public static final String JWT_BAD_CREDENTIALS = "{jwt.bad-credentials}";

	/**
	 * REST Request Messages
	 */
	public static final String REST_REQUEST_ACCESS_DENIED = "{rest.request-access-denied}";
	public static final String REST_REQUEST_METHOD_NOT_ALLOWED = "{rest.request-method-not-allowed}";
	public static final String REST_REQUEST_REQUEST_BODY_MISSING = "{rest.request-request-body-missing}";
	public static final String REST_REQUEST_UNSUPPORTED_MEDIA_TYPE = "{rest.request-unsupported-media-type}";
	public static final String TOKEN_IS_NOT_EXIRED_YET = "{rest.response-token-not-expired}";
	public static final String TOKEN_IS_EXIRED = "{rest.response-token-expired}";


	/**
	 * Date and Time Parsing Messages.
	 */
	public static final String INPUT_DATE_TIME_PARSE_ISSUE = "{rest.date-time-parse-issue}";

	/**
	 * REST Response Messages
	 */
	public static final String REST_RESPONSE_DATA_ADDED = "{rest.response-data-added}";
	public static final String REST_RESPONSE_DATA_UPDATED = "{rest.response-data-updated}";
	public static final String REST_RESPONSE_DATA_DELETED = "{rest.response-data-deleted}";
	public static final String REST_RESPONSE_SEND_RESET_PASSWORD_EMAIL = "{rest.response-send-reset-password-email}";
	public static final String REST_RESPONSE_RESET_PASSWORD_SUCCESSFUL = "{rest.response-reset-password-successful}";
	public static final String REST_RESPONSE_RESET_PASSWORD_FAILED = "{rest.response-reset-password-failed}";

	/**
	 * Validation Messages
	 */
	public static final String VALIDATION_INVALID_FIELD = "{valication.invalid.field}";
	public static final String VALIDATION_INVALID_INPUT = "{valication.invalid.input}";
	public static final String VALIDATION_EMAIL_NOT_VALID = "{validation.email-not-valid}";
	public static final String VALIDATION_EMAIL_NOT_BLANK = "{validation.email-not-blank}";
	public static final String VALIDATION_PERMITTED_IP = "{validation.permitted-ip}";
	public static final String VALIDATION_MOBILE_NOT_VALID = "{validation.mobile-not-valid}";
	public static final String VALIDATION_AADHAR_NOT_VALID = "{validation.aadhar-not-valid}";
	public static final String VALIDATION_WEBSITE_NOT_VALID = "{validation.website-not-valid}";
	public static final String VALIDATION_OFFICE_NUMBER_NOT_VALID = "{validation.office-number-not-valid}";
	public static final String VALIDATION_GST_NUMBER_NOT_VALID = "{validation.gst-number-not-valid}";
	public static final String VALIDATION_PASSWORD_NOT_BLANK = "{validation.password-not-blank}";
	public static final String VALIDATION_USERNAME_NOT_BLANK = "{validation.username-not-blank}";
	public static final String VALIDATION_USERNAME_NOT_VALID = "{validation.username-not-valid}";
	public static final String VALIDATION_FIRST_NAME_DEFAULT_NOT_BLANK = "{validation.first-name-default-language-not-blank}";
	public static final String VALIDATION_LAST_NAME_DEFAULT_NOT_BLANK = "{validation.last-name-default-language-not-blank}";
	public static final String VALIDATION_MANDATORY_FIELD_NOT_BLANK = "{validation.mandatory-field-not-blank}";
	public static final String VALIDATION_8_DIGITS_4_DECIMAL = "{validation.8-digits-4-decimal}";

	/**
	 * Validation for company preferences
	 */
	public static final String VALIDATION_NAME_DEFAULT_LANGUAGE_NOT_BLANK = "{validation.name-default-language-not-blank}";
	public static final String VALIDATION_NAME_MAX_LENGTH = "{validation.maximum-50-character-required}";
	public static final String VALIDATION_NAME_MIN_LENGTH = "{validation.minimum-2-character-required}";
	public static final String VALIDATION_ADDRESS_MAX_LENGTH = "{validation.maximum-250-character-required}";
	public static final String VALIDATION_WEBSITE_MAX_LENGTH = "{validation.maximum-2048-character-required}";
	public static final String VALIDATION_ADDRESS_DEFAULT_NOT_BLANK = "{validation.address-default-language-not-blank}";
	public static final String VALIDATION_MOBILE_NUMBER_NOT_BLANK = "{validation.mobile-number-not-blank}";
	public static final String VALIDATION_OFFICE_NUMBER_MIN = "{validation.office-number-min-length}";
	public static final String VALIDATION_OFFICE_NUMBER_MAX = "{validation.office-number-max-length}";

	/**
	 * Validation for bank detail
	 */
	public static final String VALIDATION_ACCOUNT_NAME_NOT_BLANK = "{validation.account-name-not-blank}";
	public static final String VALIDATION_ACCOUNT_NUMBER_NOT_BLANK = "{validation.account-number-not-blank}";
	public static final String VALIDATION_BRANCH_NAME_NOT_BLANK = "{validation.branch-name-not-blank}";
	public static final String VALIDATION_IFSC_CODE_NOT_BLANK = "{validation.ifsc-code-not-blank}";
	public static final String VALIDATION_ACCOUNT_NUMBER_NOT_VALID = "{validation.account-number-not-valid}";
	public static final String VALIDATION_ACCOUNT_NAME_MIN = "{validation.account-name-min-2-character}";
	public static final String VALIDATION_ACCOUNT_NAME_MAX = "{validation.account-name-max-34-character}";
	public static final String VALIDATION_ACCOUNT_NUMBER_MIN = "{validation.account-number-min-9-character}";
	public static final String VALIDATION_BRANCH_NAME_MAX = "{validation.branch-name-max-30-character}";
	public static final String VALIDATION_BRANCH_NAME_MIN = "{validation.branch-name-min-2-character}";
	public static final String VALIDATION_ACCOUNT_NUMBER_MAX = "{validation.account-number-max-30-character}";
	public static final String VALIDATION_IFSC_MIN = "{validation.ifsc-code-min-11-character}";
	public static final String VALIDATION_IFSC_MAX = "{validation.ifsc-code-max-11-character}";
	/**
	 * Validation message for user
	 */
	public static final String FIRST_NAME_MAX_LENGTH = "{first-name-max-length}";
	public static final String FIRST_NAME_MIN_LENGTH = "{first-name-min-length}";
	public static final String LAST_NAME_MAX_LENGTH = "{last-name-max-length}";
	public static final String LAST_NAME_MIN_LENGTH = "{last-name-min-length}";

	public static final String VALIDATION_UNIQUE_CODE_NOT_BLANK = "{validation.mandatory-unique-code-not-blank}";
	public static final String VALIDATION_UNIQUE_CODE_NOT_VALID = "{validation.mandatory-unique-code-not-valid}";
	public static final String VALIDATION_NAME_NOT_BLANK = "{validation.mandatory-name-not-blank}";

	public static final String VALIDATION_MOBILE_MIN = "{validation.mobile-min-length}";
	public static final String VALIDATION_MOBILE_MAX = "{validation.mobile-max-length}";
	public static final String VALIDATION_PASSWORD_MAX_LENGTH = "{validation.password-max-length}";
	public static final String VALIDATION_EMAIL_MAX_LENGTH = "{validation.email-max-length}";
	public static final String VALIDATION_USERNAME_MAX_LENGTH = "{validation.username-max-length}";
	public static final String VALIDATON_UNIQUE_CODE_MAX_LENGTH = "{validation.unique-code-max-length}";

	public static final String VALIDATION_PASSWORD_MIN_LENGTH = "{validation.password-min-length}";
	public static final String VALIDATION_USERNAME_MIN_LENGTH = "{validation.username-min-length}";
	public static final String VALIDATON_UNIQUE_CODE_MIN_LENGTH = "{validation.unique-code-min-length}";

	public static final String VALIDATION_NAME_NOT_VALID_ALLOW_ONLY_ALPHA_NUMERIC = "{validation.mandatory-name-not-valid-only-alpha-numeric}";
	public static final String VALIDATION_LANGUAGE_DEFAULT_NOT_BLANK = "{validation.language-default-not-blank}";
	public static final String VALIDATION_LANGUAGE_DEFAULT_NOT_VALID = "{validation.language-default-not-valid}";
	public static final String VALIDATION_LANGUAGE_PREFER_NOT_VALID = "{validation.language-prefer-not-valid}";
	public static final String VALIDATION_LANGUAGE_SUPPORTIVE_NOT_VALID = "{validation.language-supportive-not-valid}";
	public static final String VALIDATION_CONTACT_CATEGORY_TYPE_NOT_BLANK = "{validation.contact-category-type-not-blank}";
	public static final String VALIDATION_RAW_MATERIAL_CATEGORY_TYPE_NOT_BLANK = "{validation.item-category-type-not-blank}";
	public static final String VALIDATION_MEASUREMENT_IS_BASE_UNIT_NOT_BLANK = "{validation.measurement-is-base-unit-not-blank}";
	public static final String VALIDATION_ITEM_PRIORITY_VALID = "{validation.priority-name-not-valid-only-numeric}";
	
	public static final String VALIDATION_CONTACT_NOT_BLANK = "{validation.contact-not-blank}";
	public static final String VALIDATION_RAW_MATERIAL_NOT_BLANK = "{validation.item-raw-material-not-blank}";
	public static final String VALIDATION_MEASUREMENT_NOT_BLANK = "{validation.measurement-not-blank}";
	public static final String VALIDATION_MENU_ITEM_CATEGORY_NOT_BLANK = "{validation.menu-item-category-not-blank}";
	public static final String VALIDATION_MENU_MATERIAL_NOT_BLANK = "{validation.menu-material-not-blank}";

	public static final String VALIDATION_LANGUAGE_PREFER_NOT_VALID_FOR_FIELD = "{validation.language-prefer-not-valid-for-field}";
	public static final String VALIDATION_LANGUAGE_SUPPORTIVE_NOT_VALID_FOR_FIELD = "{validation.language-supportive-not-valid-for-field}";

	//=============================================================================
	// Order Validation Messages
	//=============================================================================

	public static final String VALIDATION_BOOK_ORDER_CUSTOMER_REQUIRED = "{validation.book-order-customer-required}";
	public static final String VALIDATION_BOOK_ORDER_EVENT_MAIN_DATE_REQUIRED = "{validation.book-order-event-main-date-required}";
	public static final String VALIDATION_BOOK_ORDER_MEAL_TYPE_REQUIRED = "{validation.book-order-meal-type-required}";
	public static final String VALIDATION_BOOK_ORDER_EVENT_TYPE_REQUIRED = "{validation.book-order-event-type-required}";
	public static final String VALIDATION_BOOK_ORDER_FUNCTION_TYPE_REQUIRED = "{validation.book-order-function-type-required}";
	public static final String VALIDATION_BOOK_ORDER_FUNCTIONS_REQUIRED = "{validation.book-order-functions-required}";
	public static final String VALIDATION_BOOK_ORDER_STATUS_REQUIRED = "{validation.book-order-status-required}";

	//=============================================================================
	// Order Menu Preparation Validation Messages
	//=============================================================================

	public static final String VALIDATION_MENU_PREPARATION_FUNCTION_REQUIRED = "{validation.menu-preparation-function-required}";
	public static final String VALIDATION_MENU_PREPARATION_MENU_TYPE_REQUIRED = "{validation.menu-preparation-menu-type-required}";
	public static final String VALIDATION_MENU_PREPARATION_RATE_REQUIRED = "{validation.menu-preparation-rate-required}";
	public static final String VALIDATION_MENU_PREPARATION_REQUIRED = "{validation.menu-preparation-required}";
	public static final String VALIDATION_MEAL_TYPE_REQUIRED = "{validation.meal-type-required}";
	public static final String VALIDATION_MENU_PREPARATION_MENU_ITEM_REQUIRED = "{validation.menu-preparation-menu-item-required}";
	public static final String VALIDATION_MENU_PREPARATION_MENU_ITEM_CATEGORY_REQUIRED = "{validation.menu-preparation-menu-item-category-required}";

	//=============================================================================
	// Common Length Validation Messages
	//=============================================================================

	public static final String VALIDATION_MIN_VALUE_0 = "{validation-min-value-0}";
	public static final String VALIDATION_MIN_LENGTH_1 = "{validation-min-length-1}";
	public static final String VALIDATION_MIN_LENGTH_2 = "{validation-min-length-2}";
	public static final String VALIDATION_MAX_LENGTH_5 = "{validation-max-length-5}";
	public static final String VALIDATION_MAX_LENGTH_30 = "{validation-max-length-30}";
	public static final String VALIDATION_MAX_LENGTH_50 = "{validation-max-length-50}";
	public static final String VALIDATION_MAX_VALUE_50 = "{validation-max-value-50}";
	public static final String VALIDATION_MAX_LENGTH_70 = "{validation-max-length-70}";
	public static final String VALIDATION_MAX_LENGTH_255 = "{validation-max-length-255}";
	public static final String VALIDATION_LENGTH_10_ONLY = "{validation-number-length-10}";
	public static final String VALIDATION_MAX_LENGTH_20 = "{validation-max-length-20}";
	public static final String VALIDATION_LENGTH_12_ONLY = "{validation-number-length-12}";
	public static final String VALIDATION_GST_INVALID = "{validation-gst-invalid}";
	public static final String VALIDATION_PAN_INVALID = "{validation-pan-invalid}";

	public static final String VALIDATION_NAME_MIN_2_LENGTH = "{validation-name-min-2-length}";

	public static final String VALIDATION_SYMBOL_MAX_10_LENGTH = "{validation-symbol-max-10-length}";
	public static final String VALIDATION_HELPER_MAX_5_LENGTH = "{validation-helper-max-5-length}";

	public static final String VALIDATION_NAME_MAX_100_LENGTH = "{validation-name-max-100-length}";

	public static final String ALREADY_EXIST = "{already-exist}";
	public static final String NOT_EXIST = "{not-exist}";
	public static final String EMAIL_NOT_EXIST = "{email-not-exist}";
	public static final String IN_USE = "{in-use}";
	public static final String NOT_EXIST_BY_ID = "{not-exist-by-id}";
	public static final String INVALID_ID = "{invalid-id}";
	public static final String VALIDATION_NOT_BLANK = "{validation.not-blank}";
	public static final String VALIDATION_IS_REQUIRED = "{validation.is-required}";
	public static final String VALIDATION_IS_INVALID = "{validation.is-invalid}";

	public static final String TENANT_NOT_EXIST = "{tenant-not-exist}";
	public static final String TENANT_MISSING = "{tenant-missing}";
	public static final String UNIQUE_CODE_MISSING = "{unique-code-missing}";

	public static final String USERNAME_OR_PASSWORD_INCORRECT = "{username-or-password-incorrect}";

	public static final String MEASUREMENT_BASE_UNIT_EQUIVALENT_BLANK = "{measurement-base-unit-equivalent-blank}";
	public static final String MEASUREMENT_BASE_UNIT = "{measurement-base-unit-id}";
	public static final String MEASUREMENT_IS_BASE_UNIT = "{measurement-is-base-unit}";
	public static final String MEASUREMENT_BASE_UNIT_ID_NOT_VALID = "{measurement-base-unit-id-not-valid}";
	public static final String MEASUREMENT_BASE_UNIT_EQUIVALENT_NOT_VALID = "{measurement-base-unit-equivalent-not-valid}";
	public static final String MEASUREMENT_IS_BASE_UNIT_CAN_NOT_BE_CHANGE = "{measurement-is-base-unit-can-not-be-change}";

	public static final String RAW_MATERIAL_SUPPLIER_CONTACT_FIELD_NOT_ACCEPTEBLE = "{item-raw-material-cotractor-contact-field-is-not-accepteble}";

	public static final String CONTACT_CATEGORY_INACTIVE = "{contact-category-inactive}";

	public static final String ALREADY_EXITS_FIELD = "{already-exist-field}";
	public static final String VALIDATION_PACKAGE_PRICE_NOT_NULL = "{validation.price-not-null}";
	public static final String VALIDATION_PACKAGE_MENU_ITEM_CATEGORY_NOT_NULL = "{validation.menu-item-category-not-null}";
	public static final String VALIDATION_PACKAGE_NO_OF_ITEMS_NOT_NULL = "{validation.no-of-items-not-null}";
	public static final String VALIDATION_PACKAGE_MENU_ITEM_NOT_NULL = "{validation.menu-item-not-null}";
	public static final String VALIDATION_PACKAGE_MENU_ITEM_CATEGORY_SEQUENCE_NOT_NULL = "{validation.menu-item-category-sequence-not-null}";
	public static final String VALIDATION_PACKAGE_MENU_ITEM_SEQUENCE_NOT_NULL = "{validation.menu-item-sequence-not-null}";

	public static final String FILE_UNSUPPORTED_IMAGE_TYPE = "{file.unsupported-image-type}";
	public static final String FILE_IMAGE_SIZE_EXCEEDED = "{file.image-size-exceeded}";

	//=============================================================================
	// User Admin Message
	//=============================================================================
	public static final String VALIDATION_USER_DESIGNATION_NOT_BLANK = "{validation.designation-not-blank}";
	public static final String VALIDATION_USER_FIELD_ALREADY_EXIST = "{validation.field-already-exist}";

	//=============================================================================
	// Labour and Order Management
	//=============================================================================
	public static final String VALIDATION_PAX_INVALID = "{validation.pax-invalid}";

	//=============================================================================
	// Tax Master
	//=============================================================================
	public static final String VALIDATION_TAX_IS_NOT_VALID = "{validation.tax-is-not-valid}";

	//=============================================================================
	// Purchase Order
	//=============================================================================
	public static final String VALIDATION_PURCHASE_DATE_NOT_BLANK = "{validation.purchase-date-not-blank}";
	public static final String VALIDATION_WEIGHT_NOT_BLANK = "{validation.weight-not-blank}";
	public static final String VALIDATION_PRICE_NOT_BLANK = "{validation.price-not-blank}";
	public static final String VALIDATION_DELIVERY_DATE_NOT_BLANK = "{validation.delivery-date-not-blank}";
	public static final String VALIDATION_TOTAL_AMOUNT_NOT_BLANK = "{validation.total-amount-not-blank}";
	public static final String VALIDATION_HSN_CODE_MAX_8_LENGTH = "{validation.han-code-max-length}";

	//=============================================================================
	// Journal Voucher
	//=============================================================================
	public static final String VALIDATION_JOURNAL_VOUCHER_DETAILS_CR_DR_AMOUNT_MISMATCH = "{validation.journal-voucher-details-cr-dr-amount-is-mismatch}";

	public static final String VALIDATION_CASH_PAYMENT_RECEIPT_TRANSACTION_DATE = "{validation.cash-payment-receipt-transaction-date-required}";
	public static final String VALIDATION_CASH_PAYMENT_RECEIPT_DETAILS_LIST = "{validation.cash-payment-receipt-details-list-required}";

}