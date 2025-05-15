package com.catering.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class holds constant values for parameter names used in Jasper report templates.
 * These constants are typically used to reference parameters that are passed to the Jasper reports
 * for generating reports in various formats.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JasperParameterConstants {

	public static final String MENU_PREPARATION_REPORT = "menu-preparation-report";
	public static final String MENU_ALLOCATION_REPORT = "menu-allocation-report";
	public static final String ORDER_REPORT = "order-report";
	public static final String GENERAL_FIX_AND_CROCKERY_ALLOCATION_REPORT = "general-fix-and-crockery-allocation-report";
	public static final String LABOUR_AND_AGENCY_REPORT = "labour-and-agency-report";
	public static final String ADMIN_REPORT = "admin-report";
	public static final String DATE_WISE_REPORT = "date-wise-reports";
	public static final String TRANSACTION_REPORTS = "transaction-reports";
	public static final String ALL_DATA_REPORT = "all-data-reports";
	public static final String INDIVIDUAL_RECORD_REPORTS = "individual-record-reports";

	// Inner class for menu preparation report constants.
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public class MenuPreprationReport {

		public static final String CUSTOM_MENU_REPORT = "custom-menu-report";
		public static final String EXCLUSIVE_MENU_REPORT = "exclusive-menu-report";
		public static final String MANAGER_MENU_REPORT = "manager-menu-report";
		public static final String SLOGAN_MENU_REPORT = "slogan-menu-report";
		public static final String PREMIUM_IMAGE_MENU_REPORT = "premium-image-menu-report";
		public static final String IMAGE_MENU_REPORT = "image-menu-report";
		public static final String IMAGE_AND_SLOGAN_MENU_REPORT = "image-and-slogan-menu-report";
		public static final String TWO_LANGUAGE_MENU_REPORT = "two-language-menu-report";
		public static final String MENU_REPORT_WITH_INSTRUCTION = "menu-report-with-instruction";
		public static final String IMAGE_MENU_CATEGORY_REPORT = "image-menu-category-report";

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class CustomMenuReport {
			public static final String TITLE = "title";
			public static final String EVENT_NAME = "event-name";
			public static final String MEETING_DATE = "meeting-date";
			public static final String EVENT_DATE = "event-date";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String GUEST_NAME = "guest-name";
			public static final String MOBILE_NO = "mobile-no";
			public static final String PAX = "pax";
			public static final String TYPE = "type";
			public static final String VENUE = "venue";
			public static final String FUNCTION = "function";
			public static final String PERSON = "person";
			public static final String RATE = "rate";
			public static final String SIGN = "sign";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String DATE = "date";
			public static final String TIME = "time";
			public static final String COMPANY_ADMIN_NUMBER = "company-admin-number";
			public static final String NOTES = "notes";
			public static final String SENTENCE_OCCASION = "static-sentence-occasion";
			public static final String SENTENCE_BEHALF = "static-sentence-behalf";
			public static final String ESTIMATE_SENTENCE = "estimate-sentence";
			public static final String ORDER_NUMBER = "order-number";
			public static final String BOOKING_DATE = "booking-date";
			public static final String BILL_DATE = "bill-date";
			public static final String HALL_NAME = "hall-name";
			public static final String SR_NO = "sr-no";
			public static final String AMOUNT = "amount";
			public static final String MOBILE = "mobile";
			public static final String TOTAL_RATE = "total-rate";
			public static final String TOTAL_BILL = "total-bill";
			public static final String DISH = "dish";
			public static final String PAGE_NUMBER = "page-number";
			public static final String PRINT_DATE = "print-date";
			public static final String MEAL_TYPE_LABEL = "meal-type-label";
			public static final String MANAGER_NAME = "manager-name";
			public static final String MANAGER_NUMBER = "manager-number";
			public static final String EXTRA = "extra";
			public static final String SUBTOTAL_LABEL = "subtotal-label";
			public static final String SGST_LABEL = "sgst-label";
			public static final String CGST_LABEL = "cgst-label";
			public static final String IGST_LABEL = "igst-label";
			public static final String DISCOUNT_LABEL = "discount-label";
			public static final String ADVANCE_PAYMENT_LABEL = "advance-payment-label";
			public static final String ROUND_OFF_LABEL = "round-off-label";
			public static final String GRAND_TOTAL_LABEL = "grand-total";
			public static final String REMAINING_AMOUNT = "remaining-amount";
			public static final String EMAIL = "email";
			public static final String ORDER_DATE = "order-date";
			public static final String HOST = "host";
			public static final String NATURE_OF_EVENT = "event-name";
			public static final String HOTEL_HARIDWAR_TITLE = "hotel-haridwar-title";
			public static final String FUNCTION_ADDRESS = "function-address";
			public static final String FUNCTION_NOTE = "function-note";
			public static final String TERMS_AND_CONDITION = "term-and-condition";
			public static final String ABOUT_US_HEADING = "about-us-heading";
			public static final String RUPEES_SYMBOL = "rupees-symbol";
			public static final String ADDRESS = "address";
			public static final String CONTACT = "contact";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String FUNCTION_NAME = "function-name";
			public static final String BOMBAY_BOYS = "bombay-boys";
			public static final String WAITER = "waiter";
			public static final String TIMING_AND_GUEST_DETAILS = "timing-and-guest-details";
			public static final String DATE_AND_TIME = "date-and-time";
			public static final String START_DATE = "start-date";
			public static final String END_DATE = "end-date";
			public static final String TO = "to";
			public static final String FOOD_LICENCE_NO = "food-licence-no";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class ExclusiveMenuReport {
			public static final String EVENT_NAME = "event-name";
			public static final String EVENT_DATE = "event-date";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String MOBILE_NO = "mobile-no";
			public static final String VENUE = "venue";
			public static final String PERSON = "person";
			public static final String RATE = "rate";
			public static final String DATE = "date-and-time";
			public static final String BOOKING_DATE = "booking-date";
			public static final String MONILE_NUMBER = "mobile-number";
			public static final String ADDRESS = "address";
			public static final String DESCRIPTION = "description";
			public static final String FOOD_NOTES = "food-notes";
			public static final String PRINTED_BY = "printed-by";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String FUNCTION_ADDRESS = "function-address";
			public static final String FUNCTION_NOTE = "function-note";
			public static final String RUPEES_SYMBOL = "rupees-symbol";
			public static final String TERMS_AND_CONDITION = "term-and-condition";
			public static final String START_DATE = "start-date";
			public static final String END_DATE = "end-date";
			public static final String TO = "to";
			public static final String EMAIL = "email";
			public static final String FOOD_LICENCE_NO = "food-licence-no";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class TwoLanguageMenuReport {
			public static final String EVENT_INFORMATION = "event-information";
			public static final String BOOKING_NUMBER = "booking-number";
			public static final String BOOKING_DATE = "booking-date";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String MONILE_NUMBER = "mobile-number";
			public static final String ADDRESS = "address";
			public static final String DESCRIPTION = "description";
			public static final String STATUS = "status";
			public static final String CONFIRMED = "confirmed";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String MENU_INFORMATION = "menu-information";
			public static final String DATE_AND_TIME = "date-and-time";
			public static final String PER_DISH = "per-dish";
			public static final String TOTAL_PRICE = "total-price";
			public static final String PEOPLES = "peoples";
			public static final String MENUS = "menus";
			public static final String RUPEES_SYMBOL = "rupees-symbol";
			public static final String FUNCTION_NOTE = "function-note";
			public static final String TO = "to";
			public static final String START_DATE = "start-date";
			public static final String END_DATE = "end-date";
			public static final String EMAIL = "email";
			public static final String FOOD_LICENCE_NO = "food-licence-no";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class ManagerMenuReport {
			public static final String TITLE = "manager-title";
			public static final String HALL_NAME = "hall-name";
			public static final String INSTRUCTIONS = "instructions";
			public static final String MEAL_TYPE = "meal-type";
			public static final String PERSON = "person";
			public static final String SIGN = "sign";
			public static final String REMARKS = "remarks";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String ON = "on";
			public static final String SENTENCE_1 = "sentence-1";
			public static final String SENTENCE_2 = "sentence-2";
			public static final String SENTENCE_3 = "sentence-3";
			public static final String FUNCTION_ADDRESS = "function-address";
			public static final String FUNCTION_NOTE = "function-note";
			public static final String RUPEES_SYMBOL = "rupees-symbol";
			public static final String FUNCTION = "function-name";
			public static final String DATE_AND_TIME = "date-and-time";
			public static final String EVENT_DATE = "event-date";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String MOBILE_NO = "mobile-no";
			public static final String VENUE = "venue";
			public static final String DATE = "date";
			public static final String TIME = "time";
			public static final String CHEF_NAME = "chef-name";
			public static final String MANAGER_NAME = "manager-name";
			public static final String CHEF_LABOUR = "chef-labour";
			public static final String OUTSIDE_LABOUR = "outside-labour";
			public static final String START_DATE = "start-date";
			public static final String START_TIME = "start-time";
			public static final String END_DATE = "end-date";
			public static final String END_TIME = "end-time";
			public static final String TO = "to";
			public static final String VENUE_NAME = "venue-name";
			public static final String EMAIL = "email";
			public static final String FOOD_LICENCE_NO = "food-licence-no";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class SloganMenuReport {
			public static final String CONTACT_DETAILS = "contact-details";
			public static final String NATURE_OF_EVENT = "nature-of-event";
			public static final String HOST = "host";
			public static final String NOTES = "notes";
			public static final String PERSON = "person";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String VENUE = "venue";
			public static final String DATE = "date";
			public static final String FUNCTION_ADDRESS = "function-address";
			public static final String FUNCTION_NOTE = "function-note";
			public static final String RUPEES_SYMBOL = "rupees-symbol";
			public static final String TO = "to";
			public static final String RATE = "rate";
			public static final String MOBILE_NUMBER = "mobile-no";
			public static final String EMAIL = "email";
			public static final String FOOD_LICENCE_NO = "food-licence-no";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class ImageAndSloganMenuReport {
			public static final String EVENT_NAME = "event-name";
			public static final String EVENT_DATE = "event-date";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String MOBILE_NUMBER = "mobile-no";
			public static final String MEAL_TYPE = "meal-type";
			public static final String FOOD_NOTE = "food-note";
			public static final String FUNCTION_NAME = "function";
			public static final String PERSON = "person";
			public static final String DATE = "date";
			public static final String SIGN = "sign";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String VENUE = "venue";
			public static final String TITLE_MENU_WITH_SLOGAN_IMAGE = "title-menu-with-slogan-image";
			public static final String FUNCTION_ADDRESS = "function-address";
			public static final String FUNCTION_NOTE = "function-note";
			public static final String RUPEES_SYMBOL = "rupees-symbol";
			public static final String START_DATE = "start-date";
			public static final String END_DATE = "end-date";
			public static final String TO = "to";
			public static final String RATE = "rate";
			public static final String EMAIL = "email";
			public static final String FOOD_LICENCE_NO = "food-licence-no";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class MenuWithImageReport1 {
			public static final String SIGN = "sign";
			public static final String PERSON = "person";
			public static final String TO = "to";
			public static final String TIMING_AND_GUEST_DETAILS = "timing-and-guest-details";
			public static final String MENU = "menu";
			public static final String VENUE = "venue";
			public static final String NOTE = "note";
			public static final String CUSTOMER_HOME_ADDRESS = "customer-home-address";
			public static final String HALL_NAME = "hall-name";
			public static final String EVENT_NAME = "event-name";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String NO_OF_GUEST = "no-of-guest";
			public static final String OOCATION = "oocation";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String NOTES = "notes";
			public static final String FLOW_OF_EVENT = "flow-of-event";
			public static final String PRINTED_BY = "printed-by";
			public static final String FOR = "for";
			public static final String MOBILE_NO = "mobile";
			public static final String ADDRESS = "address";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String FUNCTION_ADDRESS = "function-address";
			public static final String FUNCTION_NOTE = "function-note";
			public static final String RUPEES_SYMBOL = "rupees-symbol";
			public static final String RATE = "rate";
			public static final String EMAIL = "email";
			public static final String FOOD_LICENCE_NO = "food-licence-no";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class ImageMenuCategoryReport {
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String NAME = "name";
			public static final String PERSON = "person";
			public static final String EMAIL_ID = "email-id";
			public static final String ADDRESS = "address";
			public static final String HOME_ADDRESS = "home-address";
			public static final String VENUE = "venue";
			public static final String VENUE_NAME = "venue-name";
			public static final String EVENT_NAME = "event-name";
			public static final String EVENT_DATE = "event-date";
			public static final String MANAGER  = "manager";
			public static final String RATE = "rate";
			public static final String EVENT_TYPE = "event-type";
			public static final String HALL_NAME = "hall-name";
			public static final String FUNCTION = "function";
			public static final String TIME = "time";
			public static final String TO = "to";
			public static final String DATE = "date-and-time";
			public static final String FUNCTION_TIME = "function-time";
			public static final String CUSTOMER_MOBILE = "customer-mobile";
			public static final String DISH = "dish";
			public static final String DATE_LABEL = "date";
			public static final String FOOD_NOTES = "food-notes";
			public static final String PRINTED_BY = "printed-by";
			public static final String FUNCTION_ADDRESS = "function-address";
			public static final String FUNCTION_NOTE = "function-note";
			public static final String RUPEES_SYMBOL = "rupees-symbol";
			public static final String SIGN = "sign";
			public static final String CHEF_NAME = "chef-name";
			public static final String START_DATE = "start-date";
			public static final String END_DATE = "end-date";
			public static final String EMAIL = "email";
			public static final String FOOD_LICENCE_NO = "food-licence-no";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class ImageMenuReport {
			public static final String PARTY_NAME = "party-name";
			public static final String MOBILE_NO = "mobile-no";
			public static final String EMAIL_ID = "email-id";
			public static final String EVENT_NAME = "event-name";
			public static final String EVENT_DATE = "event-date";
			public static final String VENUE = "venue";
			public static final String START_DATE = "start-date";
			public static final String PERSON = "person";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String RUPEES_SYMBOL = "rupees-symbol";
			public static final String DATE_AND_TIME = "date-and-time";
			public static final String END_DATE = "end-date";
			public static final String TO = "to";
			public static final String NOTE = "note";
			public static final String RATE = "rate";
			public static final String EMAIL = "email";
			public static final String FOOD_LICENCE_NO = "food-licence-no";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class MenuReportWithInstruction {
			public static final String TITLE = "title";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String MOBILE_NUMBER = "mobile-no";
			public static final String EVENT_NAME = "event-name";
			public static final String VENUE = "venue";
			public static final String HALL_NAME = "hall-name";
			public static final String CUSTOMER_NUMBER = "customer-number";
			public static final String EVENT_DATE = "event-date";
			public static final String INSTRUCTIONS = "instructions";
			public static final String MEAL_TYPE = "meal-type";
			public static final String PERSON = "person";
			public static final String SIGN = "sign";
			public static final String REMARKS = "remarks";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String ON = "on";
			public static final String SENTENCE_1 = "sentence-1";
			public static final String SENTENCE_2 = "sentence-2";
			public static final String SENTENCE_3 = "sentence-3";
			public static final String FUNCTION_ADDRESS = "function-address";
			public static final String FUNCTION_NOTE = "function-note";
			public static final String RUPEES_SYMBOL = "rupees-symbol";
			public static final String EMAIL = "email";
			public static final String FOOD_LICENCE_NO = "food-licence-no";
		}

	}

	// Inner class for menu allocation report constants.
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public class MenuAllocationReport {

		public static final String MENU_ITEM_WISE_RAW_MATERIAL_REPORT = "menu-item-wise-raw-material-report";
		public static final String DETAIL_RAW_MATERIAL_REPORT = "detail-raw-material-report";
		public static final String MENU_WITH_QUANTITY_REPORT = "menu-with-quantity-report";
		public static final String SUPPLIER_WISE_RAW_MATERIAL_REPORT = "supplier-wise-raw-material-report";
		public static final String ORDER_FILE_REPORT = "order-file-report";
		public static final String CHITHHI_REPORT = "chithhi-report";


		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class MenuItemWiseRawMaterialReport {
			public static final String TITLE = "title";
			public static final String EVENT_NAME = "event-name";
			public static final String EVENT_DATE = "event-date";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String MOBILE_NUMBER = "mobile-no";
			public static final String FUNCTION = "function";
			public static final String PERSON = "person";
			public static final String VENUE = "venue";
			public static final String DATE = "date";
			public static final String SIGN = "sign";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String QUANTITY = "quantity";
			public static final String HALL_NAME = "hall-name";
			public static final String EXTRA_LABEL = "extra-label";
			public static final String FUNCTION_ADDRESS = "function-address";
			public static final String START_DATE = "start-date";
			public static final String END_DATE = "end-date";
			public static final String TO = "to";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class DetailRawMaterialReport {
			public static final String TITLE = "title";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String MOBILE_NUMBER = "mobile-no";
			public static final String VENUE = "venue";
			public static final String DATE = "date";
			public static final String DATE_TIME = "date-and-time";
			public static final String SIGN = "sign";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String EMAIL = "email";
			public static final String RAW_MATERIAL = "raw-material";
			public static final String TOTAL_RAW_MATERIAL_SUM = "total-raw-material-sum";
			public static final String FINAL_QUANTITY = "final-quantity";
			public static final String UNIT = "unit";
			public static final String FUNCTION = "function";
			public static final String ON_DUE = "on-due";
			public static final String AT = "at";
			public static final String DISPATCH_SENTENCE = "dispatch-sentence";
			public static final String PRASANG_SENTENCE = "prasang-sentence";
			public static final String NAME = "name";
			public static final String WEIGHT = "weight";
			public static final String VASHNONI_YAADI = "vashnoni-yaadi";
			public static final String NOS = "nos";
			public static final String CHEF_LABOUR_WISE_RAW_MATERIAL_REPORT_TITLE = "chef-labour-wise-report-title";
			public static final String CHEF_LABOUR_SUPPLIER_WISE_RAW_MATERIAL_REPORT_TITLE = "chef-labour-supplier-wise-report-title";
			public static final String AGENCY_NAME = "agency-name";
			public static final String SUPPLIER_NAME = "supplier-name";
			public static final String MENU_ITEMS = "menu-items";
			public static final String INSIDE = "inside";
			public static final String PKG = "Pkg";
			public static final String PRICE = "Price";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class MenuWithQuantityReport {
			public static final String TITLE = "title";
			public static final String EVENT_DATE = "event-date";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String FUNCTION = "function";
			public static final String PERSON = "person";
			public static final String VENUE = "venue";
			public static final String DATE = "date";
			public static final String SIGN = "sign";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String MENU_WITHOUT_QUANTITY_REPORT_TITLE = "menu-with-out-quantity-report-title";
			public static final String MOBILE_NUMBER = "mobile-no";
			public static final String HALL_NAME = "hall-name";
			public static final String FUNCTION_ADDRESS = "function-address";
			public static final String FUNCTION_NOTE = "function-note";
			public static final String RUPEES_SYMBOL = "rupees-symbol";
			public static final String START_DATE = "start-date";
			public static final String END_DATE = "end-date";
			public static final String TO = "to";
			public static final String OUTSIDE = "outside";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class SupplierWiseRawMaterialReport {
			public static final String TITLE = "title";
			public static final String EVENT_NAME = "event-name";
			public static final String EVENT_DATE = "event-date";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String MOBILE_NUMBER = "mobile-no";
			public static final String VENUE = "venue";
			public static final String RAW_MATERIAL_NAME = "raw-material";
			public static final String AGENCY_NAME = "agency-name";
			public static final String FINAL_QUANTITY = "final-quantity";
			public static final String SIGN = "sign";
			public static final String PAGE = "page";
			public static final String OF = "of";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class OrderFileReport {
			public static final String TITLE = "title";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String VENUE = "venue";
			public static final String EMAIL = "email";
			public static final String EVENT_NAME = "event-name";
			public static final String EVENT_DATE = "event-date";
			public static final String MANAGER_NAME = "manager-name";
			public static final String MANAGER_NUMBER = "manager-number";
			public static final String PERSON = "pax";
			public static final String SIGN = "sign";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String NAME = "name";
			public static final String PARTICULARS = "particulars";
			public static final String ORDER_QTY = "order-qty";
			public static final String UNIT = "unit";
			public static final String DATE = "date";
			public static final String TIME = "time";
			public static final String INCOMING_QTY = "incoming-qty";
			public static final String REMAINING_QTY = "remaining-qty";
			public static final String LABOUR = "labour";
			public static final String HELPER = "helper";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class ChithhiReport {
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String VENUE = "venue";
			public static final String ITEM_NAME = "item-name";
			public static final String ORDER_DATE = "order-date";
			public static final String DISPATCH_SENTENCE = "dispatch-sentence";
			public static final String WEIGHT = "weight";
		}
	}

	// Inner class for order report constants.
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public class OrderReport {

		public static final String QUOTATION_REPORT = "quotation-report";

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class QuotationReport {
			public static final String EMAIL_LABEL = "email-label";
			public static final String MOBILE_NUMBER_LABEL = "mobile-number-label";
			public static final String EVENT_NAME_LABEL = "event-name-label";
			public static final String EVENT_TIME_LABEL = "event-time-label";
			public static final String MANAGER_NAME_LABEL = "manager-name-label";
			public static final String CUSTOMER_NAME_LABEL = "customer-name-label";
			public static final String CUSTOMER_ADDRESS_LABEL = "customer-address-label";
			public static final String CUSTOMER_AADHAR_NO_LABEL = "customer-aadhar-no-label";
			public static final String CUSTOMER_PAN_NO_LABEL = "customer-pan-no-label";
			public static final String CUSTOMER_GST_NO_LABEL = "customer-gst-no-label";
			public static final String ESTIMATE = "estimate";
			public static final String PARTICULARS = "particulars";
			public static final String SAC = "sac";
			public static final String QUANTITY = "quantity";
			public static final String EXTRA = "extra";
			public static final String RATE = "rate";
			public static final String AMOUNT = "amount";
			public static final String SUBTOTAL_LABEL = "subtotal-label";
			public static final String SGST_LABEL = "sgst-label";
			public static final String CGST_LABEL = "cgst-label";
			public static final String IGST_LABEL = "igst-label";
			public static final String DISCOUNT_LABEL = "discount-label";
			public static final String ADVANCE_PAYMENT_LABEL = "advance-payment-label";
			public static final String ROUND_OFF_LABEL = "round-off-label";
			public static final String TOTAL_LABEL = "total-label";
			public static final String REMARK_LABEL = "remark-label";
			public static final String COMPANY_GST_NO_LABEL = "company-gst-no-label";
			public static final String MANAGER_SIGNATURE = "manager-signature";
			public static final String CUSTOMER_SIGNATURE = "customer-signature";
			public static final String AUTHORISED_SIGNATORY = "authorised-signatory";
			public static final String AUTHORISED_SIGNATURE = "authorised-signature";
			public static final String ACCOUNT_NAME_LABEL = "account-name";
			public static final String ACCOUNT_NUMBER_LABEL = "account-number";
			public static final String BRANCH_NAME_LABEL = "branch-name";
			public static final String IFSC_CODE_LABEL = "ifsc-code";
			public static final String SCAN_AND_PAY = "scan-and-pay";
			public static final String PAY_TO = "pay-to";
			public static final String COMPANY_ADMIN_NUMBER = "mobile-number";
			public static final String MS = "mr/ms";
			public static final String ADD = "address";
			public static final String NO = "no";
			public static final String CHARGES_NAME = "charges-name";
			public static final String GST_NO = "gst-no";
			public static final String TOTAL = "total";
			public static final String RUPEES_IN_WORDS = "rupees";
			public static final String SIGNATURE = "signature";
			public static final String BANK_DETAILS_HEADING = "bank-details-heading";
			public static final String FOR = "for";
			public static final String DATE = "date";
			public static final String BILL_NO = "bill-number";
			public static final String BILL_DATE = "bill-date";
			public static final String GRAND_TOTAL_LABEL = "grand-total";
			public static final String REMAINING_AMOUNT = "remaining-amount";
			public static final String INVOICE = "invoice";
			public static final String PROFORMA_INVOICE = "proforma-invoice";
			public static final String CONTACT_PERSON_NAME_LABEL = "contact-person-name";
			public static final String PO_NUMBER_LABEL = "po-number";
		}
	}

	// Inner class for general fix kariyanu and crockery allocation report constants.
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public class GeneralFixAndCrockeryAllocation {

		public static final String GENERAL_FIX_REPORT = "general-fix-report";
		public static final String CROCKERY_REPORT = "crockery-report";
		public static final String CROCKERY_WITH_MENU_REPORT = "crockery-with-menu-report";

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class GeneralFixReport {
			public static final String TITLE = "title";
			public static final String DATE ="date";
			public static final String FUNCTION ="function";
			public static final String NAME = "name";
			public static final String QUANTITY_AND_UNIT = "quantity-and-unit";
			public static final String FUNCTION_ADDRESS = "function-address";
			public static final String ON_DUE = "on-due";
			public static final String AT = "at";
			public static final String DISPATCH_SENTENCE = "dispatch-sentence";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class CrockeryReport {
			public static final String TITLE = "title";
			public static final String FUNCTION = "function";
			public static final String NAME = "name";
			public static final String CATEGORY = "category";
			public static final String QUANTITY = "quantity";
			public static final String DATE = "date";
			public static final String ON_DUE = "on-due";
			public static final String AT = "at";
			public static final String DISPATCH_SENTENCE = "dispatch-sentence";
			public static final String FUNCTION_ADDRESS = "function-address";
			public static final String KITCHEN_CROCKERY_TITLE = "kitchen-crockery-title";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class CrockeryWithMenuReport {
			public static final String TITLE = "title";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String EVENT_NAME = "event-name";
			public static final String EVENT_DATE = "event-date";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String MOBILE_NO = "mobile-no";
			public static final String VENUE = "venue";
			public static final String FUNCTION= "function";
			public static final String PERSON = "person";
			public static final String RATE = "rate";
			public static final String DATE = "date";
			public static final String DATE_AND_TIME = "date-and-time";
			public static final String TIME = "time";
			public static final String NOTES = "notes";
			public static final String HALL_NAME = "hall-name";
			public static final String CROCKERY_NAME = "crockery-name";
			public static final String QUANTITY = "quantity";
			public static final String ON_DUE = "on-due";
			public static final String AT = "at";
			public static final String RUPEES_SYMBOL = "rupees-symbol";
			public static final String DISPATCH_SENTENCE = "dispatch-sentence";
			public static final String FUNCTION_ADDRESS = "function-address";
		}
	}

	// Inner class for labour and agency report constants.
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public class LabourAndAgencyReport {

		public static final String CHEF_LABOUR_REPORT = "chef-labour-report";
		public static final String CHEF_LABOUR_CHITHHI_REPORT = "chef-labour-chithhi-report";
		public static final String LABOUR_REPORT = "labour-report";
		public static final String LABOUR_CHITHHI_REPORT = "labour-chithhi-report";
		public static final String BOOKING_REPORT = "booking-report";

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class ChefLabourReport {
			public static final String TITLE = "title";
			public static final String ORDER_DATE = "order-date";
			public static final String AGENCY_NAME = "agency-name";
			public static final String CHEF_NAME = "chef-name";
			public static final String MENU = "menu";
			public static final String MOBILE_NUMBER = "mobile-no";
			public static final String FUNCTION = "function";
			public static final String MENU_ITEM_NAME = "menu-item-name";
			public static final String PERSON = "person";
			public static final String VENUE = "venue";
			public static final String AGENCY_NUMBER = "agency-number";
			public static final String PERSONS = "person";
			public static final String SIGN = "sign";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String EMAIL = "emailId";
			public static final String TITLE_FOR_OUTSIDE_AGENCY = "title-for-outside-agency";
			public static final String REMARK = "remark";
			public static final String QUANTITY = "quantity";
			public static final String UNIT = "unit";
			public static final String HALLNAME = "hallName";
			public static final String WEBSITE = "website";
			public static final String ADDRESS = "address";
			public static final String DATE = "date";
			public static final String DATE_AND_TIME = "date-and-time";
			public static final String TIME = "time";
			public static final String GODOWN = "godown";
			public static final String LABOUR = "labour";
			public static final String HELPER = "helper";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class ChefLabourChithhiReport {
			public static final String AGENCY_NAME = "agency-name";
			public static final String MANAGER_NAME = "manager-name";
			public static final String MOBILE_NUMBER = "mobile-no";
			public static final String ORDER_DATE = "order-date";
			public static final String MENU_ITEM_NAME = "menu-item-name";
			public static final String PERSON = "person";
			public static final String VENUE = "venue";
			public static final String ON_DUE = "on-due";
			public static final String AT = "at";
			public static final String FOR = "for";
			public static final String CHEF_NAME = "chef-name";
			public static final String MENU = "menu";
			public static final String FUNCTION = "function";
			public static final String DATE = "date";
			public static final String TIME = "time";
			public static final String GODOWN = "godown";
			public static final String AGENCY = "agency";
			public static final String LABOUR = "labour";
			public static final String HELPER = "helper";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class LabourReport {
			public static final String MOBILE_NO = "mobile-number";
			public static final String EMAIL = "email";
			public static final String FUNCTION = "function";
			public static final String TITLE = "title";
			public static final String SIGN = "sign";
			public static final String VENUE = "venue";
			public static final String PERSON = "person";
			public static final String DATE = "date";
			public static final String LABOUR = "labour";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String HALLNAME = "hall-name";
			public static final String LABOUR_SHIFT = "labour-shift";
			public static final String MOBILE_NUMBER_LABEL = "mobile-number";
			public static final String VENUE_LABEL = "venue";
			public static final String CUSTOMER_NAME_LABEL = "customer-name";
			public static final String NOTE = "note";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class LabourChithhiReport {
			public static final String VENUE = "venue";
			public static final String MANAGER_NAME = "manager-name";
			public static final String CONTACT_NAME = "contact-name";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String DATE = "date";
			public static final String NOTE = "note";
			public static final String DISPATCH_SENTENCE = "sentence";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class BookingReport {
			public static final String TITLE = "title";
			public static final String SIGN = "sign";
			public static final String DATE = "date";
			public static final String MIDDLE_SENTENCE = "middle-sentence";
			public static final String MOBILE_NUMBER_LABEL = "mobile-number";
			public static final String VENUE_LABEL = "venue";
			public static final String CUSTOMER_NAME_LABEL = "customer-name";
			public static final String NOTE = "note";
			public static final String CUSTOMER_HOME_ADDRESS = "customer-home-address";
		}

	}

	// Inner class for admin report constants.
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public class AdminReport {
		public static final String WASTAGE_REPORT = "wastage-report";
		public static final String DISH_COUNTING_REPORT = "dish-counting-report";
		public static final String SUPPLIER_DETAILS_REPORT = "supplier-details-report";
		public static final String DISH_COSTING_REPORT = "dish-costing-report";
		public static final String TOTAL_DISH_COSTING_REPORT = "total-dish-costing-report";
		public static final String CUSTOMER_EXTRA_COST_REPORT = "customer-extra-cost-report";
		public static final String PARTY_COMPLAIN_REPORT = "party-complain-report";

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class WastageReport {
			public static final String TITLE = "title";
			public static final String ADDRESS = "address";
			public static final String EVENT_NAME = "event-name";
			public static final String EMAIL = "email";
			public static final String WEBSITE = "website";
			public static final String DATE = "date";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String HALL_NAME = "hall-name";
			public static final String MANAGER_NAME = "manager-name";
			public static final String MOBILE_NUMBER = "mobile-no";
			public static final String VENUE = "venue";
			public static final String MENU_ITEM_NAME = "menu-item-name";
			public static final String SR = "sr";
			public static final String REQ_QTY = "req-qty";
			public static final String SEND_QTY = "send-qty";
			public static final String BAL_QTY = "bal-qty";
			public static final String UNIT = "unit";
			public static final String SIGN = "sign";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String CONTRACTOR_NAME = "contractor-name";
			public static final String DISPATCH_SENTENCE = "dispatch-sentence";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class PartyComplainReport {
			public static final String TITLE = "title";
			public static final String NAME = "name";
			public static final String SPORT_SHOES = "sport-shoes";
			public static final String WITHOUT_SEVING = "without-seving";
			public static final String WITHOUT_UNIFORM = "without-uniform";
			public static final String NOTE = "note";
			public static final String TAI_WAITER = "tai-waiter";
			public static final String KARIGAR = "karigar";
			public static final String MANAGER = "manager";
			public static final String SIGN = "sign";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class DishCountingReport {
			public static final String TITLE = "title";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String MOBILE_NUMBER = "mobile-no";
			public static final String FUNCTION_NAME = "function-name";
			public static final String PERSON = "person";
			public static final String VENUE = "venue";
			public static final String DATE = "date";
			public static final String SIGN = "sign";
			public static final String MANAGER_SIGNATURE = "manager-signature";
			public static final String SIGNATURE = "signature";
			public static final String DISH_COUNTING = "dish-counting";
			public static final String EXTRA_DISH = "extra-dish";
			public static final String TESTING = "testing";
			public static final String NAME = "Name";
			public static final String TIME = "time";
			public static final String NOTE = "note";
			public static final String FEEDBACK_FORM = "feedback-form";
			public static final String FUNCTION_ADDRESS = "function-address";
			public static final String ORDER_DISH = "order-dish";
			public static final String TOTAL_DISH = "total-dish";
			public static final String EVENT_DATE = "event-date";
			public static final String CUSTOMER_SIGNATURE = "customer-signature";
			public static final String CONFIRM_SIGNATURE = "confirm-signature";
			public static final String START_DATE = "start-date";
			public static final String END_DATE = "end-date";
			public static final String TO = "to";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class SupplierDetailsReport {
			public static final String SIGN = "sign";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String VENUE = "venue";
			public static final String TITLE = "title";
			public static final String EVENT_NAME = "event-name";
			public static final String EVENT_DATE = "event-date";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String MOBILE_NUMBER = "mobile-no";
			public static final String EMAIL = "email";
			public static final String FUNCTION = "function";
			public static final String PERSON = "person";
			public static final String DATE = "date";
			public static final String CATEGORY_NAME = "category-name";
			public static final String SUPPLIER_NO = "supplier-no";
			public static final String NOTES = "notes";
			public static final String SUPPLIER_NAME = "supplier-name";
			public static final String FUNCTION_ADDRESS = "function-address";
			public static final String START_DATE = "start-date";
			public static final String END_DATE = "end-date";
			public static final String TO = "to";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class DishCostingReport {
			public static final String TITLE = "title";
			public static final String CHARGES = "charges";
			public static final String DATE_AND_TIME = "date-and-time";
			public static final String FUNCTION = "function";
			public static final String TOTAL = "total";
			public static final String CHEF_LABOUR_CHARGES = "chef-labour-charges";
			public static final String LABOUR_CHARGES = "labour-charges";
			public static final String OUTSIDE_AGENCY_CHARGES = "out-side-agency-charges";
			public static final String EXTRA_EXPENSE_CHARGES = "extra-expense-charges";
			public static final String TOTAL_RAW_MATERIAL_CHARGES = "total-raw-material-charges";
			public static final String TOTAL_AGENCY_CHARGES = "total-agency-charges";
			public static final String TOTAL_GENERAL_FIX_CHARGES = "total-general-fix-charges";
			public static final String TOTAL_CROCKERY_CHARGES = "total-crockery-charges";
			public static final String GRAND_TOTAL = "grand-total";
			public static final String DISH_COSTING = "dish-costing";
			public static final String START_DATE = "start-date";
			public static final String END_DATE = "end-date";
			public static final String TO = "to";
			public static final String MOBILE_NUMBER = "mobile-no";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class TotalDishCostingReport {
			public static final String TITLE = "title";
			public static final String DATE_AND_TIME = "date-and-time";
			public static final String FUNCTION = "function";
			public static final String TOTAL = "total";
			public static final String GRAND_TOTAL = "grand-total";
			public static final String DISH_COSTING = "dish-costing";
			public static final String TO = "to";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class CustomerFormatReport {
			public static final String DATE = "date";
			public static final String VENUE = "venue";
			public static final String CUSTOMER_NAME = "customer-name";
			public static final String EXTRA_DISH = "extra-dish";
			public static final String REQUIREMENT = "requirement";
			public static final String MANAGER_SIGN = "manager-sign";
			public static final String VERIFIER_SIGN = "verifier-sign";
			public static final String PAX = "pax";
			public static final String CUSTOMER_FORMAT = "customer-format";
			public static final String FUNCTION_ADDRESS = "function-address";
		}
	}

	// Inner class for date wise report constants.
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public class DateWiseReports {

		public static final String DATE_WISE_OUTSIDE_LABOUR_REPORT = "date-wise-outside-labour-report";

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class OutSideLabourReport {
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String EMAIL = "email";
			public static final String TITLE = "title";
			public static final String TITLE_FOR_CHEF_LABOUR = "title-chef-labour";
			public static final String TITLE_FOR_LABOUR = "title-labour";
			public static final String TITLE_FOR_MENU_ITEM = "title-menu-item";
			public static final String CATEGORY_NAME = "category-name";
			public static final String TO = "to";
			public static final String SUPPLIER_CATEGORY = "supplier-category";
			public static final String SUPPLIER_NAME = "supplier-name";
			public static final String MENU_ITEM_CATEGORY = "menu-item-category";
			public static final String ORDER_DATE = "order-date";
			public static final String VENUE = "venue";
			public static final String MENU_ITEM_NAME = "menu-item-name";
			public static final String TIME = "time";
			public static final String QUANTITY = "quantity";
			public static final String RATE = "rate";
			public static final String TOTAL = "total";
			public static final String SIGN = "sign";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String GRAND_TOTAL = "grand-total";
			public static final String UNIT = "unit";
			public static final String RAW_MATERIAL_NAME = "raw-material-name";
			public static final String RAW_MATERIAL_TITLE = "raw-material-title";
			public static final String TITLE_FOR_ORDER_BOOKING_REPORT = "title-order-booking-report";
			public static final String PAX = "pax";
			public static final String FUNCTION = "function";
			public static final String PARTY = "party";
			public static final String DATE = "date";
			public static final String COUNTER = "counter";
			public static final String HELPER = "helper";
			public static final String LABOUR = "labour";
			public static final String HELPERS = "helpers";
			public static final String LABOURS = "labours";
			public static final String PERSON = "person";
			public static final String TOTAL_QUANTITY = "total-quantity";
		}

	}

	// Inner class for transaction report constants.
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public class TransactionReports {

		public static final String TRANSACTION_REPORT = "transaction-report";
		public static final String STOCK_REPORT = "stock-report";
		public static final String ACCOUNT_REPORT = "account-report";
		public static final String GROUP_SUMMARY_REPORT = "group-summary-report";
		public static final String CASH_BOOK_REPORT = "cash-book-report";
		public static final String DAILY_ACTIVITY_REPORT = "daily-activity-report";
		public static final String GST_SALES_PURCHASE_REPORT = "gst-sales-purchase-report";

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class TransactionReport {
			public static final String TITLE_NAME = "title";
			public static final String TITLE_NAME_WITHOUT_RAW_MATERIAL = "title-without-raw-material";
			public static final String PURCHASE_BILL_TITLE_NAME = "purchase-bill-title";
			public static final String PURCHASE_BILL_TITLE_NAME_WITHOUT_RAW_MATERIAL = "purchase-bill-title-without-raw-material";
			public static final String INPUT_TRANSFER_HALL_TITLE_NAME_WITH_RAW_MATERIAL = "input-transfer-hall-title-with-raw-material";
			public static final String INPUT_TRANSFER_HALL_TITLE_NAME_WITHOUT_RAW_MATERIAL = "input-transfer-hall-title-without-raw-material";
			public static final String DEBIT_NOTE_TITLE_NAME_WITH_ITEMS = "debit-note-title-with-items";
			public static final String DEBIT_NOTE_TITLE_NAME_WITHOUT_ITEMS = "debit-note-title-without-items";
			public static final String PAGE = "page";
			public static final String NO = "no";
			public static final String REF_NO = "ref-no";
			public static final String ITEM_NAME = "item-name";
			public static final String DATE = "date";
			public static final String TRANSFER_DATE = "transfer-date";
			public static final String BILL_DATE = "bill-date";
			public static final String PURCHASE_DATE = "purchase-date";
			public static final String QTY = "qty";
			public static final String TOTAL_QTY = "total-qty";
			public static final String RATE = "rate";
			public static final String AMT = "amt";
			public static final String NET_AMT = "net-amt";
			public static final String TOTAL = "total";
			public static final String OF = "of";
			public static final String CONTACT_NAME = "contact-name";
			public static final String HALL_NAME = "hall-name";
			public static final String CASH_PAYMENT_TITLE = "cash-payment-title";
			public static final String CASH_RECEIPT_TITLE = "cash-receipt-title";
			public static final String ACCOUNT_NAME = "account-name";
			public static final String PARTICULARS = "particulars";
			public static final String BANK_PAYMENT_TITLE = "bank-payment-title";
			public static final String BANK_RECEIPT_TITLE = "bank-receipt-title";
			public static final String CHEQUE_NO = "cheque-no";
			public static final String CHEQUE_DATE = "cheque-date";
			public static final String RAW_MATERIAL_RETURN_HALL_TITLE_NAME_WITH_RAW_MATERIAL = "raw-material-return-hall-title-with-raw-material";
			public static final String RAW_MATERIAL_RETURN_HALL_TITLE_NAME_WITHOUT_RAW_MATERIAL = "raw-material-return-hall-title-without-raw-material";
			public static final String EXTRA_EXPENSE = "extra-expense";
			public static final String DISCOUNT = "discount";
			public static final String ROUND_OFF = "round-off";
			public static final String GRAND_TOTAL = "grand-total";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String EMAIL = "email";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class StockReport {
			public static final String STOCK_LEDGER_TITLE = "stock-ledger-title";
			public static final String STOCK_SUMMARY_TITLE = "stock-summary-title";
			public static final String FROM = "from";
			public static final String TO = "to";
			public static final String DATE = "date";
			public static final String VOUCHER_NO = "voucher-no";
			public static final String VOUCHER_TYPE = "voucher-type";
			public static final String CONTACT_NAME = "contact-name";
			public static final String ISSUE = "issue";
			public static final String RECEIVE = "receive";
			public static final String BALANCE = "balance";
			public static final String OPB = "opb";
			public static final String CLB = "clb";
			public static final String PAGE = "page";
			public static final String NO = "no";
			public static final String OF = "of";
			public static final String ITEM_NAME = "item-name";
			public static final String STOCK = "stock";
			public static final String UNIT = "unit";
			public static final String RATE = "rate";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String EMAIL = "email";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class AccountReport {
			public static final String COLLECTION_REPORT_TITLE = "collection-report-title";
			public static final String NAME = "name";
			public static final String FROM = "from";
			public static final String TO = "to";
			public static final String CREDIT="credit";
			public static final String DEBIT="debit";
			public static final String TOTAL="total";
			public static final String PAGE = "page";
			public static final String NO = "no";
			public static final String OF = "of";
			public static final String SR = "sr";
			public static final String DATE = "date";
			public static final String VOUCHER_NO = "voucher-no";
			public static final String VOUCHER_TYPE = "voucher-type";
			public static final String BALANCE = "balance";
			public static final String CLB = "clb";
			public static final String PARTY_TOTAL = "party-total";
			public static final String REMARK = "remark";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String EMAIL = "email";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class GroupSummaryReport {
			public static final String TITLE = "title";
			public static final String NAME = "name";
			public static final String CREDIT = "credit";
			public static final String DEBIT = "debit";
			public static final String TOTAL = "total";
			public static final String OPB = "opb";
			public static final String BALANCE = "balance";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String SR = "sr";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String EMAIL = "email";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class CashBookReport {
			public static final String CASH_BOOK_TITLE = "cash-book-title";
			public static final String PERIOD = "period";
			public static final String DESCRIPTION = "description";
			public static final String OPB_TITLE = "opb-title";
			public static final String CLB_TITLE = "clb-title";
			public static final String TO_DATE = "to-date";
			public static final String TO_CONTACT = "to-contact";
			public static final String BY_CONTACT = "by-contact";
			public static final String VOUCHER_NO = "voucher-no";
			public static final String VOUCHER_TYPE = "voucher-type";
			public static final String DATE = "date";
			public static final String CREDIT="credit";
			public static final String DEBIT="debit";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String BANK_BOOK_TITLE = "bank-book-title";
			public static final String CHEQUE_NO = "cheque-no";
			public static final String TRANSACTION_NO = "transaction-no";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String EMAIL = "email";
		}
	
		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class DailyActivityReport {
			public static final String DAILY_ACTIVITY_TITLE = "daily-activity-title";
			public static final String STOCK_DETAILS_TITLE = "stock-details-title";
			public static final String DATE = "date";
			public static final String VOUCHER_NO = "voucher-no";
			public static final String VOUCHER_TYPE = "voucher-type";
			public static final String ACCOUNT_NAME = "account-name";
			public static final String JAMA = "jama";
			public static final String UDHAR = "udhar";
			public static final String BALANCE = "balance";
			public static final String CLB = "clb";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String ACCOUNT_DETAILS_TITLE = "account-details-title";
			public static final String REMARK = "remark";
			public static final String OPB = "opb";
			public static final String TOTAL = "total";
			public static final String PERIOD = "period";
			public static final String TO_DATE = "to-date";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String EMAIL = "email";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class GstSalePurchaseReport {
			public static final String GST_SALES_TITLE = "gst-sales-title";
			public static final String GST_PURCHASE_TITLE = "gst-purchase-title";
			public static final String NO = "no";
			public static final String DATE = "date";
			public static final String INV_NO = "inv-no";
			public static final String BILL_NO = "bill-no";
			public static final String CONTACT_NAME = "contact-name";
			public static final String GST_NUMBER = "gst-number";
			public static final String NET_TOTAL = "net-total";
			public static final String SGST = "sgst";
			public static final String CGST = "cgst";
			public static final String IGST = "igst";
			public static final String SGST_AMOUNT = "sgst-amount";
			public static final String CGST_AMOUNT = "cgst-amount";
			public static final String IGST_AMOUNT = "igst-amount";
			public static final String VALUE = "value";
			public static final String RATE = "rate";
			public static final String TOTAL = "total";
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String EMAIL = "email";
		}

	}

	// Inner class for individual record report constants.
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public class IndividualRecordReport {

		public static final String CASH_BANK_PAYMENT_RECEIVE_REPORT = "cash-bank-payment-receive-report";
		public static final String JOURNAL_VOUCHER_REPORT = "journal-voucher-report";
		public static final String PURCHASE_ORDER_REPORT = "purchase-order-report";
		public static final String INPUT_TRANSFER_TO_HALL_REPORT = "input-transfer-to-hall-report";

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class CashBankPaymentReceiptReport {
			public static final String SENTENCE_1 = "sentence-1";
			public static final String SIGNATURE = "signature";
			public static final String FOR = "for";
			public static final String NO = "no";
			public static final String VOUCHER_NUMBER = "voucher-number";
			public static final String TOTAL = "total";
			public static final String AMOUNT = "amount";
			public static final String DESCRIPTION = "description";
			public static final String DATE = "date";
			public static final String EMAIL = "email";
			public static final String CASH_PAYMENT = "cash-payment";
			public static final String CASH_RECEIVE = "cash-receive";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String TRANSACTION_DATE = "transaction-date";
			public static final String TRANSACTION_NUMBER = "transaction-number";
			public static final String CHEQUE_NUMBER = "cheque-number";
			public static final String CHEQUE_DATE = "cheque-date";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class JournalVoucherReport {
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String VOUCHER_NUMBER = "voucher-number";
			public static final String CR_DR_LABEL = "cr-dr-label";
			public static final String CR = "cr";
			public static final String NO = "no";
			public static final String DR = "dr";
			public static final String TOTAL = "total";
			public static final String AMOUNT = "amount";
			public static final String REMARK = "remark";
			public static final String DATE = "date";
			public static final String EMAIL = "email";
			public static final String TITLE = "title";
			public static final String ACCOUNT_NAME = "account-name";
			public static final String MOBILE_NUMBER = "mobile-number";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class PurchaseOrderReport {
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String TITLE = "title";
			public static final String TOTAL = "total";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String NO = "no";
			public static final String EMAIL = "email";
			public static final String VOUCHER_NUMBER = "voucher-number";
			public static final String RATE = "rate";
			public static final String DATE = "date";
			public static final String DELIVERY_DATE = "delivery-date";
			public static final String QTY = "qty";
			public static final String SGST = "sgst";
			public static final String CGST = "cgst";
			public static final String IGST = "igst";
			public static final String DESCRIPTION = "description";
			public static final String AMOUNT = "amount";
			public static final String VALUE = "value";
			public static final String PAN_NUMBER = "pan-number";
			public static final String GST_NUMBER = "gst-number";
			public static final String SENTENCE_1 = "sentence-1";
			public static final String SIGNATURE = "signature";
			public static final String FOR = "for";
			public static final String SENTENCE_2 = "sentence-2";
			public static final String SENTENCE_3 = "sentence-3";
			public static final String SENTENCE_4 = "sentence-4";
			public static final String TITLE_PURCHASE_BILL = "title-purchase-bill";
			public static final String TITLE_DEBIT_NOTE = "title-debit-note";
			public static final String EXTRA_EXPENSE_TITLE = "extra-expense-title";
			public static final String DISCOUNT_TITLE = "discount-title";
			public static final String ROUND_OFF_TITLE = "round-off-title";
			public static final String GRAND_TOTAL_TITLE = "grand-total-title";
			public static final String REMARK = "remark";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class InputTransferToHallReport {
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String INPUT_TRANSFER_TITLE = "input-transfer-title";
			public static final String RAW_MATERIAL_RETURN_TITLE = "raw-material-return-title";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String NO = "no";
			public static final String EMAIL = "email";
			public static final String VOUCHER_NUMBER = "voucher-number";
			public static final String DATE = "date";
			public static final String HALL_NAME = "hall-name";
			public static final String WEIGHT = "weight";
			public static final String UNIT = "unit";
			public static final String RAW_MATERIAL_NAME = "raw-material-name";
		}

	}

	// Inner class for all data report constants.
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public class AllDataReports {

		public static final String MENU_ITEM_REPORT = "menu-item-report";
		public static final String RAW_MATERIAL_REPORT = "raw-material-report";
		public static final String REPORT_REPORT = "package-report";

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class MenuItemReport {
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String RAW_MATERIAL = "raw-material";
			public static final String RAW_MATERIAL_CATEGORY = "raw-material-category";
			public static final String QTY = "qty";
			public static final String MEASUREMENT = "measurement";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class RawMaterialReport {
			public static final String PAGE = "page";
			public static final String OF = "of";
			public static final String NO = "no";
			public static final String NAME = "name";
			public static final String BPRICE = "b-price";
			public static final String PPRICE = "p-price";
			public static final String UNIT = "unit";
		}

		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public class CustomPackageReport {
			public static final String TITLE = "title";
			public static final String ANY_OF = "any-of";
			public static final String MOBILE_NUMBER = "mobile-number";
			public static final String PACKAGE_NAME = "package-name";
			public static final String PACKAGE_PRICE ="package-price";
		}

	}

}