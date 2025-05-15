package com.catering.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiPathConstant {

	private static final String ROOT = "/";
	public static final String ID = "/{id}";
	public static final String ORDER_ID = "/{orderId}";
	public static final String ACTIVE = "/active";
	public static final String ALL = "/all";
	public static final String PAGINATION_SIZE = "/pagination-size";
	public static final String IS_IMAGE = "/{isImage}";
	public static final String STATUS = "/{status}";
	public static final String ORDER_FUNCTION_ID = "/{orderFunctionId}";
	public static final String TIME_ZONES = "/time-zones";
	public static final String LOGO_HORIZONTAL = "/logo-horizontal";
	public static final String LOGO_HORIZONTAL_WHITE = "/logo-horizontal-white";
	public static final String LOGO_VERTICAL = "/logo-vertical";
	public static final String FAVICON = "/favicon";
	public static final String LIST = "/list";

	/**
	 * Authentication
	 */
	public static final String AUTHENTICATE = "/authenticate";
	public static final String FORGOT_PASSWORD = "/forgot-password";
	public static final String REFRESH_TOKEN = "/refresh-token";
	public static final String VALIDATE_TOKEN = "/validate-token";

	/**
	 * Swagger
	 */
	private static final String SWAGGER_API = "/jucas/**";
	private static final String ALL_SWAGGER_UI = "/swagger-ui/**";

	public static final String SUPERADMIN = "/superadmin";
	public static final String REGISTRATION = "/registration";
	public static final String CHANGE_PASSWORD = "/change-password";
	private static final String CONTACT_CATEGORY_TYPE = "/contact-category-type";
	private static final String RAW_MATERIAL_CATEGORY_TYPE = "/raw-material-category-type";

	/**
	 * Super Admin
	 * */
	private static final String CONTACT_CATEGORY_TYPE_MASTER = SUPERADMIN + CONTACT_CATEGORY_TYPE;
	public static final String LANGUAGE = SUPERADMIN + "/language";
	public static final String SUBSCRIPTIONS = "/subscriptions";
	public static final String UPDATE_SIDEBAR_CACHING = SUPERADMIN + "/update-sidebar-caching/{uniqueCode}";

	/**
	 * Tenant
	 * */
	private static final String TENANT = "/tenant";
	public static final String KITCHEN_AREA= TENANT + "/kitchen-area";
	public static final String MEAL_TYPE = TENANT + "/meal-type";
	public static final String EVENT_TYPE = TENANT + "/event-type";
	public static final String GODOWN = TENANT + "/godown";
	public static final String MENU_ITEM_SUB_CATEGORY = TENANT + "/menu-item-sub-category";
	public static final String COMPANY = TENANT + "/company";
	public static final String COMPANY_USER = TENANT + "/company-user";
	public static final String VERIFY_EMAIL = "/verify-email";
	public static final String RESET_PASSWORD = "/reset-password";
	public static final String SEND_VERIFICATION_EMAIL = "/send-verification-email";
	private static final String USERID_TENANT = "/{userId}/{tenant}";
	public static final String VERIFY_EMAIL_USERID_TENANT = VERIFY_EMAIL + USERID_TENANT;
	public static final String SEND_VERIFICATION_EMAIL_USERID_TENANT = SEND_VERIFICATION_EMAIL + USERID_TENANT;
	public static final String IS_ACTIVE = "/is-active";
	public static final String CONTACT_CATEGORY = TENANT + "/contact-category";
	public static final String CONTACT_CATEGORY_WITH_CONTACTS = "/with-contacts";
	public static final String CONTACT_CATEGORY_TYPE_TENANT = TENANT + CONTACT_CATEGORY_TYPE;
	public static final String CONTACT = TENANT + "/contact";
	public static final String BANK_LIST = "/bank-list";
	public static final String CONTACT_BY_CATEGORY_TYPE = "/category-type" + ID;
	public static final String COMPANY_PREFERENCES = TENANT + "/company-preferences";
	public static final String COMPANY_SETTING = TENANT + "/company-setting";
	public static final String COMPANY_SETTING_LAYOUT_THEME = "/layout-theme";
	public static final String COMPANY_SETTING_COLOUR_THEME = "/colour-theme";
	public static final String COMPANY_BANK = TENANT + "/company-bank";
	public static final String QR_CODE = "/qr-code";
	public static final String BACKGROUND_IMAGE = "/background-image";
	public static final String COMPANY_SETTING_MENU_PREPARATION_TOGGLE_PRICE_VISIBILITY = "/menu-preparation-toggle-price-visibility";
	public static final String RAW_MATERIAL_CATEGORY = TENANT + "/raw-material-category";
	public static final String RAW_MATERIAL_CATEGORY_TYPE_TENANT = TENANT +  RAW_MATERIAL_CATEGORY_TYPE;
	public static final String MEASUREMENT = TENANT + "/measurement";
	public static final String MEASUREMENT_BASE_UNIT = "/base-unit";
	public static final String CUSTOM_RANGE = "/custom-range";
	public static final String RAW_MATERIAL = TENANT + "/raw-material";
	public static final String RAW_MATERIAL_SUPPLIER= TENANT + "/raw-material-supplier";
	public static final String UNIQUE_LIST = "/unique-list";
	public static final String RAW_MATERIAL_WITH_CONTRACTOR_LIST = "/raw-material-with-supplier";
	public static final String MENU_ITEM_CATEGORY = TENANT + "/menu-item-category";
	public static final String MENU_ITEM = TENANT + "/menu-item";
	public static final String NAMES = "/names";
	public static final String MENU_ITEM_LIST = "/only-menu-items";
	public static final String MENU_ITEM_RAW_MATERIAL = TENANT + "/menu-item-raw-material";
	public static final String NOTES = TENANT + "/notes";
	public static final String CATERING_PREFERENCES = "/catering-preferences";
	public static final String FUNCTION_TYPE = TENANT + "/function-type";
	public static final String CUSTOM_PACKAGE = TENANT + "/custom-package";
	public static final String AUTO_TRANSLATE = "/translate";
	public static final String MENU_HEADER = TENANT + "/menu-header";
	public static final String BOOK_ORDER = TENANT + "/book-order";
	public static final String ORDER_FUNCTION = TENANT + "/order-function";
	public static final String BOOK_ORDER_DETAILS = "/order-details";
	public static final String BOOK_ORDER_UPCOMING = "/upcoming";
	public static final String ORDER_MENU_PREPARATION = TENANT + "/order-menu-preparation";
	public static final String USER = TENANT + "/user";
	public static final String ROLES = "/roles";
	public static final String MENU_ALLOCATION = TENANT + "/menu-allocation";
	public static final String RAW_MATERIAL_ALLOCATION = TENANT + "/raw-material-allocation";
	public static final String LABOUR_AND_OTHER_MANAGEMENT = TENANT + "/labour-and-other-management";
	public static final String LABOUR_AND_OTHER_MANAGEMENT_RAW_MATERIAL_CATEGORY = "/crockery-raw-material-category";
	public static final String LABOUR_AND_OTHER_MANAGEMENT_CROCKEY_SUPPLIER = "/crockery-supplier";
	public static final String LABOUR_AND_OTHER_MANAGEMENT_GENERAL_FIX_RAW_MATERIAL_SUPPLIER = "/general-fix-raw-material-supplier";
	public static final String CROCKERY = "/crockery";
	public static final String EXTRA = "/extra";
	public static final String ORDER_GENERAL_FIX_RAW_MATERIAL = "/order-general-fix-raw-material";
	public static final String ORDER_LABOUR_DISTRIBUTION = TENANT + "/order-labour-distribution";
	public static final String USER_RIGHTS = TENANT + "/user-rights";
	public static final String COMBINE_REPORT = TENANT + "/combine-report";
	public static final String REPORT_COMPANY_DETAIL_RIGHTS = TENANT + "/report-company-detail-rights";
	public static final String REPORT_MASTER = TENANT + "/report-master";
	public static final String REPORT_CATEGORY_RANGE = "/category-range";
	public static final String REPORT_USER_RIGHTS = TENANT + "/report-user-rights";
	public static final String REPORT_CATEGORY = "/report-category";
	public static final String ORDER_BOOKING_REPORTS = TENANT + "/order-booking-reports";
	public static final String NAME_PLATE = TENANT + "/name-plate";
	public static final String TABLE_MENU_REPORT = TENANT + "/table-menu";
	public static final String RAW_MATERIAL_REPORT_MENU_ALLOCATION_RAW_MATERIAL_CATEGORY_PER_ORDER = "/item-category";
	public static final String RAW_MATERIAL_REPORT_MENU_ALLOCATION_FUNCTION_PER_ORDER = "/function";
	public static final String BY_MENU_ITEM = "/by-menu-item"+ID;
	public static final String ORDER_QUOTATION = TENANT + "/order-quotation";
	public static final String ORDER_QUOTATION_BY_ORDER = "/by-order" + ID;
	public static final String ORDER_INVOICE = TENANT + "/order-invoice";
	public static final String ORDER_INVOICE_BY_ORDER = "/invoice-by-order" + ID;
	public static final String ORDER_PROFORMA_INVOICE = TENANT + "/order-proforma-invoice";
	public static final String ORDER_PROFORMA_INVOICE_BY_ORDER = "/proforma-invoice-by-order" + ID;
	public static final String DISH_COSTING = "/dish-costing" + ID;
	public static final String GET_MENU_ALLOCATION = "/get" + ORDER_ID;
	public static final String FOR_MENU_PREPARATION = "/preparation" + ID;
	public static final String GET_MENU_PREPARATION = "/get-preparation" + ID;
	public static final String GET_MENU_PREPARATION_RAW_MATEIRAL = "/all-raw-material";
	public static final String GET_MENU_PREPARATION_RAW_MATEIRAL_BY_MENU_ITEM_ID = "/raw-material-by-menu-item" + ID;
	public static final String GET_MENU_PREPARATION_MENU_ITEM_CATEGORY_MENU_ITEM_EXIST = "/menu-item-category-menu-item-exist" + ORDER_ID;
	public static final String MENU_ITEM_INFO = "/menu-item-information";
	public static final String MENU_ITEM_RUPEES = "/menu-item-rupees";
	public static final String MENU_ITEM_CATEGORY_INFO = "/menu-item-category-information";
	public static final String MENU_ITEM_CATEGORY_RUPEES = "/menu-item-category-rupees";
	public static final String SYNC_RAW_MATERIAL = "/sync-raw-material";
	public static final String LOGIN_LOG = TENANT + "/login-log";
	public static final String SETTINGS = TENANT + "/settings";
	public static final String TERMS_AND_CONDITIONS = TENANT + "/terms-and-conditions";
	public static final String GENERAL_FIX_RAW_MATERIAL_NOTES = TENANT + "/general-fix-raw-material-notes";
	public static final String CROCKERY_NOTES = TENANT + "/crockery-notes";
	public static final String ORDER_BOOKING_REPORT_TEMPLATE_NOTES = TENANT + "/order-booking-report-template-notes";
	public static final String TABLE_MENU_REPORT_HEADER_NOTES = TENANT + "/table-menu-reports-header-notes";
	public static final String TABLE_MENU_REPORT_FOOTER_NOTES = TENANT + "/table-menu-reports-footer-notes";
	public static final String TERMS_AND_CONTIONS_REPORT = "/terms-and-conditions-report";
	public static final String DATE_WISE_REPORTS = TENANT + "/date-wise-reports";
	public static final String DATE_WISE_OUTSIDE_ORDER_REPORT_WITH_PRICE = "/date-wise-outside-order-report-with-price";
	public static final String DATE_WISE_OUTSIDE_ORDER_REPORT_WITHOUT_PRICE = "/date-wise-outside-order-report-without-price";
	public static final String DATE_WISE_CHEF_LABOUR_REPORT_WITH_PRICE = "/date-wise-chef-labour-report-with-price";
	public static final String DATE_WISE_CHEF_LABOUR_REPORT_WITHOUT_PRICE = "/date-wise-chef-labour-report-without-price";
	public static final String DATE_WISE_LABOUR_REPORT_WITH_PRICE = "/date-wise-labour-report-with-price";
	public static final String DATE_WISE_LABOUR_REPORT_WITHOUT_PRICE = "/date-wise-labour-report-without-price";
	public static final String DATE_WISE_ORDER_BOOKING_REPORT = "/date-wise-order-booking";
	public static final String DATE_WISE_MENU_ITEM_REPORT = "/date-wise-menu-item-report";
	public static final String DATE_WISE_RAW_MATERIAL_REPORT = "/date-wise-raw-material-report";
	public static final String DATE_WISE_TOTAL_RAW_MATERIAL_REPORT = "/date-wise-total-raw-material-report";
	public static final String OUTSIDE_SUPPLIER_CATEGORY = "/outside-supplier-category";
	public static final String OUTSIDE_SUPPLIER_NAME = "/outside-supplier-name";
	public static final String DATE_WISE_RAW_MATERIAL_SUPPLIER_CATEGORY = "/raw-material-supplier-category";
	public static final String DATE_WISE_RAW_MATERIAL_SUPPLIER = "/raw-material-supplier";
	public static final String DATE_WISE_RAW_MATERIAL_CATEGORY = "/raw-material-category";
	public static final String MENU_ITEM_CATEGORY_FOR_REPORT = "/menu-item-category";
	public static final String MENU_ITEM_SUB_CATEGORY_FOR_REPORT = "/menu-item-sub-category";
	public static final String KITCHEN_AREA_FOR_REPORT = "/kitchen-area";
	public static final String CUSTOMER_CONTACTS_FOR_REPORT = "/customer-contact";
	public static final String CHEF_LABOUR_SUPPLIER_CATEGORY = "/chef-labour-supplier-category";
	public static final String CHEF_LABOUR_SUPPLIER_NAME = "/chef-labour-supplier-name";
	public static final String UPDATE_NOTE = "/update-notes";
	public static final String UPDATE_RAW_MATERIAL_QUANTITY = "/update-raw-material-quantity";
	public static final String UPDATE_FUNCTION_ADDRESS = "/update-function-address";
	public static final String UPDATE_RAW_MATERIAL_TIME = "/update-raw-material-time";
	public static final String LABOUR_SUPPLIER_CATEGORY = "/labour-supplier-category";
	public static final String LABOUR_SUPPLIER_NAME = "/labour-supplier-name";
	public static final String CONTACT_NAME = "/contact-name";
	public static final String ALL_DATA_REPORT = TENANT + "/all-data-reports";
	public static final String CATALOGUE_REPORT = "/menu-item-catalogue-report";
	public static final String MENU_REPORT = "/menu-item-menu-report";
	public static final String RAW_MATERIAL_REPORT = "/raw-material-report";
	public static final String RAW_MATERIAL_CATEGORY_DROP_DOWN = "/raw-material-category-dropdown";
	public static final String MENU_ITEM_WISE_RAW_MATERIAL_REPORT_DROPDOWN = "/raw-material-dropdown";
	public static final String MENU_ITEM_WISE_RAW_MATERIAL_REPORT = "/menu-item-wise-raw-material-report";
	public static final String MENU_ITEM_WISE_QUANTITY_RAW_MATERIAL_REPORT_DROPDOWN = "/menu-item-category-dropdown";
	public static final String MENU_ITEM_WISE_QUANTITY_RAW_MATERIAL_REPORT = "/menu-item-wise-quantity-raw-material-report";
	public static final String MENU_ITEM_EXIST = "/menu-item-exist";
	public static final String RAW_MATERIAL_CALCULATION = "/calculateRawMaterialCost";
	public static final String CUSTOM_PACKAGE_DROP_DOWN = "/custom-package-dropdown";
	public static final String CUSTOM_PACKAGE_REPORT = "/custom-package-report";
	public static final String RAW_MATERIAL_EXIST = "/raw-material-exist";
	public static final String UPDATE_PRIORITY = "/update-priority";
	public static final String HIGHEST_PRIORITY = "/highest-priority";
	public static final String AGENCY_ALLOCATION = "/agency-allocation";
	public static final String SMALLEST_MEASUREMENT_VALUE = "/smallest-measurement-value";
	public static final String SMALLEST_MEASUREMENT_ID = "/smallest-measurement-id";
	public static final String ADJUSTED_QUANTITY = "/adjusted-quantity";
	public static final String LABOUR_SHIFT = TENANT + "/labour-shift";
	public static final String HALL_MASTER = TENANT + "/hall-master";
	public static final String CHECK_EXISTENCE = "/check-existence" + ID;
	public static final String ORDER_STATUS = TENANT + "/order-status";
	public static final String ORDER_STATUS_UPDATE = "/order-status" + ID;
	public static final String ABOUT_US = TENANT + "/about-us";
	public static final String TRANSACTION_REPORTS = TENANT + "/transactions-reports";
	public static final String STOCK_REPORTS = TENANT + "/stock-reports";
	public static final String ACCOUNT_REPORTS = TENANT + "/account-reports";
	public static final String CASH_PAYMENT_RECEIPT_SUPPILER_CONTACT = "/cash-payment-receipt-suppiler-contact";
	public static final String CASH_PAYMENT_RECEIPT_REPORT = "/cash-payment-receipt-report";
	public static final String BANK_PAYMENT_RECEIPT_SUPPILER_CONTACT = "/bank-payment-receipt-suppiler-contact";
	public static final String BANK_PAYMENT_RECEIPT_REPORT = "/bank-payment-receipt-report";
	public static final String PURCHASE_ORDER_SUPPILER_CONTACT = "/purchase-order-suppiler-contact";
	public static final String INPUT_TRANSFER_TO_HALL_DROP_DOWN_DATA = "/input-transfer-hall";
	public static final String INPUT_TRANSFER_TO_HALL_RAW_MATERIAL_REPORT = "/input-transfer-hall-raw-material";
	public static final String RAW_MATERIAL_RETURN_TO_HALL_DROP_DOWN_DATA = "/raw-material-return-to-hall";
	public static final String RAW_MATERIAL_RETURN_TO_HALL_RAW_MATERIAL_REPORT = "/raw-material-return-hall-raw-material";
	public static final String PURCHASE_ORDER_RAW_MATERIAL_REPORT = "/purchase-order-raw-material";
	public static final String DATE_WISE_PURCHASE_ORDER_REPORT_WITH_ITEMS = "/date-wise-purchase-order-report-with-items";
	public static final String DATE_WISE_PURCHASE_ORDER_REPORT_WITHOUT_ITEMS = "/date-wise-purchase-order-report-without-items";
	public static final String PURCHASE_BILL_SUPPILER_CONTACT_REPORT = "/purchase-bill-suppiler-contact";
	public static final String PURCHASE_BILL_RAW_MATERIAL_REPORT = "/purchase-bill-raw-material";
	public static final String DATE_WISE_PURCHASE_BILL_REPORT_WITH_ITEMS = "/date-wise-purchase-bill-report-with-items";
	public static final String DATE_WISE_PURCHASE_BILL_REPORT_WITHOUT_ITEMS = "/date-wise-purchase-bill-report-without-items";
	public static final String DATE_WISE_INPUT_TRANSFER_HALL_REPORT_WITH_ITEMS = "/date-wise-input-transfer-hall-report-with-items";
	public static final String DATE_WISE_INPUT_TRANSFER_HALL_REPORT_WITHOUT_ITEMS = "/date-wise-input-transfer-hall-report-without-items";
	public static final String DATE_WISE_RAW_MATERIAL_RETURN_HALL_REPORT_WITH_ITEMS = "/date-wise-raw-material-return-hall-report-with-items";
	public static final String DATE_WISE_RAW_MATERIAL_RETURN_HALL_REPORT_WITHOUT_ITEMS = "/date-wise-raw-material-return-hall-report-without-items";
	public static final String DEBIT_NOTE_SUPPILER_CONTACT_REPORT = "/debit-note-suppiler-contact";
	public static final String DEBIT_NOTE_RAW_MATERIAL_REPORT = "/debit-note-raw-material";
	public static final String DATE_WISE_DEBIT_NOTE_REPORT_WITH_ITEMS = "/date-wise-debit-note-report-with-items";
	public static final String DATE_WISE_DEBIT_NOTE_REPORT_WITHOUT_ITEMS = "/date-wise-debit-note-report-without-items";
	public static final String STOCK_LEDGER_RAW_MATERIAL_REPORT = "/stock-ledger-raw-material";
	public static final String STOCK_LEDGER_START_DATE_REPORT = "/stock-ledger-start-date";
	public static final String DATE_WISE_STOCK_LEDGER_REPORT = "/date-wise-stock-ledger-report";
	public static final String STOCK_SUMMARY_RAW_MATERIAL_CATEGORY_REPORT = "/stock-summary-raw-material-category";
	public static final String STOCK_SUMMARY_RAW_MATERIAL_REPORT = "/stock-summary-raw-material";
	public static final String STOCK_SUMMARY_REPORT = "/stock-summary-report";
	public static final String COLLECTION_REPORT = "/collection-report";
	public static final String COLLECTION_CONTACT_CATEGORY_REPORT = "/collection-contact-category";
	public static final String GENERAL_LEDGER_SUPPILER_CONTACT_REPORT = "/general-ledger-suppiler-contact";
	public static final String GENERAL_LEDGER_REPORT = "/general-ledger-report";
	public static final String GROUP_SUMMARY_REPORT = "/group-summary-report";
	public static final String COLLECTION_CONTACT_REPORT = "/collection-contact";
	public static final String CASH_BOOK_REPORT = "/cash-book-report";
	public static final String BANK_BOOK_BANK_CONTACT_REPORT = "/bank-book-bank-contact";
	public static final String BANK_BOOK_REPORT = "/bank-book-report";
	public static final String GST_SALES_REPORT = "/gst-sales-report";
	public static final String GST_PURCHASE_REPORT = "/gst-purchase-report";
	public static final String VOUCHER_PAYMENT_HISTORY = "/voucher-payment-history";
	public static final String DAILY_ACTIVITY_REPORT = "/daily-activity-report";
	public static final String GET_CUSTOM_PACKAGE_MENU_ITEM_CATEGORY_MENU_ITEM_EXIST = "/menu-item-category-menu-item-exist-with-custom-package";
	public static final String GET_CUSTOM_PACKAGE_RECORD = "/custom-package-record";

	/**
	 * Reports
	 * */
	public static final String MENU_PREPARATION_CUSTOM_MENU_REPORT = "/custom-menu-report" + ID;
	public static final String MENU_PREPARATION_SIMPLE_MENU_REPORT = "/simple-menu-report" + ID;
	public static final String MENU_PREPARATION_TWO_LANGUGAE_MENU_REPORT = "/two-language-menu-report" + ID;
	public static final String MENU_PREPARATION_EXCLUSIVE_REPORT = "/exclusive-menu-report" + ID;
	public static final String MENU_PREPARATION_MANAGER_MENU_REPORT = "/manager-menu-report" + ID;
	public static final String MENU_PREPARATION_PREMIUM_IMAGE_MENU_REPORT = "/premium-image-menu-report" + ID;
	public static final String MENU_PREPARATION_IMAGE_MENU_REPORT = "/image-menu-report" + ID;
	public static final String MENU_PREPARATION_IMAGE_MENU_CATEGORY_REPORT = "/image-menu-category-report" + ID;
	public static final String MENU_PREPARATION_SLOGAN_MENU_REPORT = "/slogan-menu-report" + ID;
	public static final String MENU_PREPARATION_IMAGE_AND_SLOGAN_MENU_REPORT = "/image-and-slogan-menu-report" + ID;
	public static final String MENU_PREPARATION_FUNCTION_PER_ORDER = "/function/menu-preparation";
	public static final String MENU_ALLOCATION_MENU_ITEM_WISE_RAW_MATERIAL_REPORT = "/menu-item-wise-raw-material-report" + ID;
	public static final String MENU_ALLOCATION_DETAIL_RAW_MATERIAL_REPORT = "/detail-raw-material-report" + ID;
	public static final String MENU_ALLOCATION_TOTAL_RAW_MATERIAL_REPORT = "/total-raw-material-report" + ID;
	public static final String MENU_ALLOCATION_TOTAL_RAW_MATERIAL_WITH_PRICE_REPORT = "/total-raw-material-with-price-report" + ID;
	public static final String MENU_ALLOCATION_TOTAL_RAW_MATERIAL_WITH_CATEGORY_REPORT = "/total-raw-material-with-category-report" + ID;
	public static final String MENU_ALLOCATION_CHEF_LABOUR_WISE_RAW_MATERIAL_REPORT = "/chef-labour-wise-raw-mateial-report" + ID;
	public static final String MENU_ALLOCATION_CHEF_LABOUR_SUPPLIER_WISE_RAW_MATERIAL_REPORT = "/chef-labour-supplier-wise-raw-mateial-report" + ID;
	public static final String MENU_ALLOCATION_MENU_WITH_QUANTITY_REPORT = "/menu-with-quantity-report" + ID;
	public static final String MENU_ALLOCATION_MENU_WITH_OUT_QUANTITY_REPORT = "/menu-with-out-quantity-report" + ID;
	public static final String MENU_ALLOCATION_SUPPLIER_WISE_RAW_MATERIAL_REPORT = "/supplier-wise-raw-material-report" + ID;
	public static final String PARTY_COMPLAIN_REPORT = "/party-complain-report" + ID;
	public static final String LABOUR_AND_CROCKERY_CHEF_LABOUR_REPORT = "/chef-labour-report" + ID;
	public static final String LABOUR_AND_CROCKERY_CHEF_LABOUR_CHITHHI_REPORT = "/chef-labour-chithhi-report" + ID;
	public static final String LABOUR_AND_CROCKERY_OUTSIDE_AGENCY_REPORT = "/outside-agency-report" + ID;
	public static final String LABOUR_AND_CROCKERY_OUTSIDE_AGENCY_CHITHHI_REPORT = "/outside-agency-chithhi-report" + ID;
	public static final String NAME_PLATE_REPORT = "/counter-name-plate-report" + ID;
	public static final String TWO_LANGUAGE_NAME_PLATE_REPORT = "/two-language-counter-name-plate-report" + ID;
	public static final String TWO_LANGUAGE_NAME_PLATE_DOC_REPORT = "/two-language-counter-name-plate-doc-report" + ID;
	public static final String NAME_PLATE_DOC_REPORT = "/counter-name-plate-doc-report" + ID;
	public static final String NAME_PLATE_STRING = "/get-name-plate-details" + ID;
	public static final String TABLE_MENU_STRING = "/get-table-menu-details" + ID;
	public static final String UPDATE_NAME_PLATE = "/update-name-plate-details";
	public static final String UPDATE_TABLE_MENU = "/update-table-menu-details";
	public static final String RAW_MATERIAL_ORDER_FILE_REPORT = "/order-file-report" + ID;
	public static final String RAW_MATERIAL_DIRECT_ORDER = "/raw-material-category/direct-order";
	public static final String TIME_WISE_RAW_MATERIAL_A5_REPORT = "/time-wise-raw-material-a5-report" + ID;
	public static final String TIME_WISE_RAW_MATERIAL_A6_REPORT = "/time-wise-raw-material-a6-report" + ID;
	public static final String TIME_WISE_RAW_MATERIAL_A4_REPORT = "/time-wise-raw-material-a4-report" + ID;
	public static final String ORDER_QUOTATION_REPORT = "/quotation-report" + ID;
	public static final String ORDER_INVOICE_REPORT = "/invoice-report" + ID;
	public static final String ORDER_PROFORMA_INVOICE_REPORT = "/proforma-invoice-report" + ID;
	public static final String LABOUR_AND_CROCKERY_LABOUR_REPORT = "/labour-report" + ID;
	public static final String LABOUR_AND_CROCKERY_LABOUR_CITHHI_REPORT = "/labour-chithhi-report" + ID;
	public static final String LABOUR_AND_CROCKERY_BOOKING_REPORT = "/booking-report" + ID;
	public static final String GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_REPORT = "/crockery-report" + ID;
	public static final String GENERAL_FIX_AND_CROCKERY_ALLOCATION_KITCHEN_CROCKERY_REPORT = "/kitchen-crockery-report" + ID;
	public static final String GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_WITH_MENU_REPORT = "/crockery-with-menu-report" + ID;
	public static final String GENERAL_FIX_AND_CROCKERY_ALLOCATION_GENERAL_FIX_REPORT = "/general-fix-report" + ID;
	public static final String ADMIN_WASTAGE_REPORT = "/wastage-report" + ID;
	public static final String ADMIN_DISH_COUNTING = "/dish-counting-report" + ID;
	public static final String ADMIN_TOTAL_DISH_COSTING = "/total-dish-costing-report" + ID;
	public static final String ADMIN_SUPPLIER_DETAILS_REPORT = "/supplier-details-report" + ID;
	public static final String ADMIN_DISH_COSTING_REPORT = "/dish-costing-report" + ID;
	public static final String ADMIN_CUSTOMER_EXTRA_COST_REPORT = "/customer-extra-cost-report" + ID;
	public static final String ADMIN_ADDRESS_CHITHHI_REPORT = "/admin-address-chithhi-report" + ID;
	public static final String CHEF_LABOUR_SUPPLIERS = "/chef-labour-suppliers" + ID;
	public static final String OUTSIDE_LABOUR_SUPPLIERS = "/outside-labour-suppliers" + ID;
	public static final String ACTIVE_FUNCTIONS_CHEF_LABOUR = "/active-functions-chef-labour" + ID;
	public static final String ACTIVE_FUNCTIONS_OUTSIDE = "/active-functions-outside" + ID;
	public static final String ACTIVE_FUNCTIONS_LABOUR = "/active-functions-labour" + ID;
	public static final String CHEF_LABOUR_ITEM_NAME = "/chef-labour-item-name" + ID;
	public static final String OUTSIDE_LABOUR_ITEM_NAME = "/outside-labour-item-name" + ID;
	public static final String INSTRUCTION_MENU_REPORT = "/menu-report-with-instruction" + ID;
	public static final String TABLE_MENU_REPORT_1 = "/table-menu-report-1" + ID;
	public static final String TABLE_MENU_REPORT_2 = "/table-menu-report-2" + ID;
	public static final String TABLE_MENU_DOC_REPORT_1 = "/table-menu-doc-report-1" + ID;
	public static final String TABLE_MENU_DOC_REPORT_2 = "/table-menu-doc-report-2" + ID;
	public static final String SELECTED_CHEF_LABOUR_AGENCY = "/selected-chef-labour-agency";
	public static final String MENU_ITEMS = "/menu-items";
	public static final String ORDER_BOOKING_COMBINE_REPORT = "/combine-report" + ID;

	public static final String INDIVIDUAL_RECORD_REPORT = TENANT + "/individual-record-report";
	public static final String INDIVIDUAL_RECORD_CASH_PAYMENT_RECEIPT_REPORT = "/cash-payment-receipt-report" + ID;
	public static final String INDIVIDUAL_RECORD_BANK_PAYMENT_RECEIPT_REPORT = "/bank-payment-receipt-report" + ID;
	public static final String INDIVIDUAL_RECORD_PURCHASE_ORDER_REPORT = "/purchase-order-report" + ID;
	public static final String INDIVIDUAL_RECORD_PURCHASE_BILL_REPORT = "/purchase-bill-report" + ID;
	public static final String INDIVIDUAL_RECORD_DEBIT_NOTE_REPORT = "/debit-note-report" + ID;
	public static final String INDIVIDUAL_RECORD_JOURNAL_VOUCHER_REPORT = "/journal-voucher-report" + ID;
	public static final String INDIVIDUAL_RECORD_INPUT_TRANSFER_TO_HALL_REPORT = "/input-transfer-to-hall-report" + ID;
	public static final String INDIVIDUAL_RECORD_RAW_MATERIAL_RETURN_TO_HALL_REPORT = "/raw-material-return-to-hall-report" + ID;


	public static final String NOTIFICATION = TENANT + "/notification";
	public static final String MARK_AS_READ = "/mark-as-read";

	public static final String CURRENT_SUBSCRIPTION = "/current-subscription";
	public static final String PAYMENT_HISTORY = "/payment-history";
	public static final String SUBSCRIPTION_ACTIVITY = "/subscription-activity";

	public static final String TAX_MASTER = TENANT + "/tax-master";

	/**
	 * Module 2 APIs
	 */
	public static final String PURCHASE_ORDER = TENANT + "/purchase-order";
	public static final String PURCHASE_BILL = TENANT + "/purchase-bill";
	public static final String DEBIT_NOTE = TENANT + "/debit-note";
	public static final String SUPPLIER_CONTACT = "/contact-data";
	public static final String RAW_MATERIAL_DATA = "/raw-material-data";
	public static final String PURCHASE_ORDER_DROPDOWN = "/get-purchase-order" + ID;
	public static final String PURCHASE_ORDER_RAW_MATERIAL = "/get-purchase-order-raw-material" + ID;
	public static final String PURCHASE_BILL_DROPDOWN = "/get-purchase-bill" + ID;
	public static final String PURCHASE_BILL_RAW_MATERIAL = "/get-purchase-bill-raw-material" + ID;

	public static final String JOURNAL_VOUCHER = TENANT + "/journal-voucher";

	public static final String INPUT_TRANSFER_TO_HALL = TENANT + "/input-transfer-to-hall";
	public static final String INPUT_TRANSFER_TO_HALL_RAW_MATERIAL_LIST = "/raw-material-list" + ID;
	public static final String INPUT_TRANSFER_TO_HALL_ORDERS_lIST = "/orders-list"+ ID;

	public static final String CASH_PAYMENT_RECEIPT = TENANT + "/cash-payment-receipt";
	public static final String CASH_PAYMENT = "/cash-payment";
	public static final String CASH_RECEIPT = "/cash-receipt";
	public static final String BANK_PAYMENT = "/bank-payment";
	public static final String BANK_RECEIPT = "/bank-receipt";
	public static final String VOUCHER_NO_LIST = "/voucher-no-list";
	public static final String PAYMENT_CONTACT_LIST = "/contact-list";
	public static final String BANK_PAYMENT_RECEIPT = TENANT + "/bank-payment-receipt";

	public static final String RAW_MATERIAL_RETURN_TO_HALL = TENANT + "/raw-material-return-to-hall";
	public static final String RAW_MATERIAL_RETURN_TO_HALL_RAW_MATERIAL_LIST = "/raw-material-list" + ID;
	public static final String RAW_MATERIAL_RETURN_TO_HALL_INPUT_TRANSFER_HALL_LIST = "/raw-material-return-hall-input-transfer-drop-down"+ ID;

	/**
	 * Permit APIs
	 */
	public static String[] getAllowRequests() {
		return new String[] { ROOT, AUTHENTICATE, ALL_SWAGGER_UI, SWAGGER_API, COMPANY + REGISTRATION, COMPANY_USER + REGISTRATION, COMPANY_USER + VERIFY_EMAIL_USERID_TENANT,
				COMPANY_USER + SEND_VERIFICATION_EMAIL_USERID_TENANT, CATERING_PREFERENCES, CATERING_PREFERENCES + LOGO_VERTICAL, CATERING_PREFERENCES + LOGO_HORIZONTAL, CATERING_PREFERENCES + FAVICON,
				AUTHENTICATE + FORGOT_PASSWORD, VALIDATE_TOKEN, RESET_PASSWORD, UPDATE_SIDEBAR_CACHING, AUTO_TRANSLATE};
	}

	public static String[] getAllowMasterRequests() {
		return new String[] { CONTACT_CATEGORY_TYPE_MASTER, LANGUAGE };
	}

}