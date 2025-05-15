package com.catering.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class holds the constants for various field names.
 * These constants are used to reference fields in the database or objects.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldConstants {

	public static final String COMMON_FIELD_ID = "id";
	public static final String COMMON_FIELD_ORDER_ID = "orderId";
	public static final String COMMON_FIELD_LANG_TYPE = "langType";
	public static final String COMMON_FIELD_LANG_CODE = "langCode";
	public static final String COMMON_FIELD_EMAIL = "email";
	public static final String COMMON_FIELD_STATUS = "status";
	public static final String COMMON_FIELD_NAME_DEFAULT_LANG = "nameDefaultLang";
	public static final String COMMON_FIELD_NAME_PREFER_LANG = "namePreferLang";
	public static final String COMMON_FIELD_NAME_SUPPORTIVE_LANG = "nameSupportiveLang";
	public static final String COMMON_FIELD_ORDER_FUNCTION_ID = "orderFunctionId";
	public static final String COMMON_FIELD_CURRENT_DATE = "currentDate";
	public static final String COMMON_FIELD_CONTACT_DATE = "contactId";
	public static final String COMMON_FIELD_CUSTOM_PACKAGE_ID = "customPackageId";
	public static final String COMMON_FIELD_FUNCTION_TYPE_TIME = "timeString";

	public static final String INVOICE_FILED_BILL_NUMBER = "billNumber";
	public static final String ADDRESS_FIELD_DEFAULT_LANG = "addressDefaultLang";
	public static final String ADDRESS_FIELD_PREFER_LANG = "addressPreferLang";
	public static final String ADDRESS_FIELD_SUPPORTIVE_LANG = "addressSupportiveLang";

	public static final String COMPANY_PREFERENCES_FIELD_PREFER_LANG = "preferLang";
	public static final String COMPANY_PREFERENCES_FIELD_SUPPORTIVE_LANG = "supportiveLang";

	public static final String CONTACT = "contact";
	public static final String CATEGORY = "category";

	public static final String CONTACT_CATEGORY_FIELD_CONTACT_CATEGORY_TYPE = "contactCategoryType";

	public static final String CONTACT_FIELD_CONTACT_CATEGORY = "contactCategory";
	public static final String CONTACT_FIELD_CATEGORY_MAPPING = "categoryMapping";
	public static final String CONTACT_FIELD_MOBILE_NUMBER = "mobileNumber";
	public static final String CONTACT_FIELD_AADHAR_NUMBER = "aadharNumber";
	public static final String CONTACT_FIELD_EMAIL = "email";

	public static final String RAW_MATERIAL_CATEGORY_FIELD_RAW_MATERIAL_CATEGORY_TYPE = "rawMaterialCategoryType";
	public static final String RAW_MATERIAL_CATEGORY = "rawMaterialCategory";
	public static final String EVENT_TYPE = "eventType";
	public static final String PRIORITY = "priority";
	public static final String RAW_MATERIAL = "rawMaterial";
	public static final String IS_ACTIVE = "isActive";
	public static final String EDIT_COUNT = "editCount";

	public static final String MEASUREMENT = "measurement";
	public static final String MEASUREMENT_SYMBOL_DEFAULT_LANG = "symbolDefaultLang";
	public static final String MEASUREMENT_SYMBOL_PREFER_LANG = "symbolPreferLang";
	public static final String MEASUREMENT_SYMBOL_SUPPORTIVE_LANG = "symbolSupportiveLang";
	public static final String MEASUREMENT_IS_BASE_UNIT = "isBaseUnit";
	public static final String MEASUREMENT_BASE_EQUIVALENT = "baseUnitEquivalent";
	public static final String MEASUREMENT_BASE_UNIT_ID = "baseUnitId";

	public static final String MENU_ITEM_CATEGORY = "menuItemCategory";
	public static final String FINAL_KITCHEN_AREA = "kitchenArea";
	public static final String IS_PLATE = "isPlate";
	public static final String MENU_ITEM = "menuItem";
	public static final String MENU_ITEM_PRICE = "price";
	public static final String MENU_ITEM_RAW_MATERIAL_FIELD_WEIGHT = "weight";
	public static final String IS_IMAGE = "isImage";

	public static final String BOOK_ORDER_FIELD_CONTACT_CUSTOMER = "contactCustomer";
	public static final String BOOK_ORDER_FIELD_CONTACT_MANAGER = "contactManager";
	public static final String BOOK_ORDER_FIELD_MEAL_TYPE = "mealType";
	public static final String BOOK_ORDER_FIELD_EVENT_MAIN_DATE_STRING = "eventMainDateString";
	public static final String BOOK_ORDER_FIELD_EVENT_NAME_DEFAULT_LANG = "eventNameDefaultLang";
	public static final String BOOK_ORDER_FIELD_EVENT_NAME_PREFER_LANG = "eventNamePreferLang";
	public static final String BOOK_ORDER_FIELD_EVENT_NAME_SUPPORTIVE_LANG = "eventNameSupportiveLang";
	public static final String BOOK_ORDER_FIELD_FUNCTIONS = "functions";
	public static final String BOOK_ORDER_FIELD_EVENT_TYPE = "eventType";

	public static final String PACKAGE_PRICE = "price";
	public static final String PACKAGE_TOTAL_ITEMS = "totalItems";
	public static final String PACKAGE_NO_OF_ITEMS = "noOfItems";

	public static final String CUSTOMER_CONTACT_ID = "customerContactId";

	public static final String TEMPLATE_ID = "templateId";

	public static final String IS_DARK_THEME = "isDarkTheme";
	public static final String COLOUR_THEME = "colourTheme";

	//=============================================================================
	// User Admin Fields
	//=============================================================================

	public static final String USER_FIRST_NAME_DEFAULT_LANG = "firstNameDefaultLang";
	public static final String USER_FIRST_NAME_PREFER_LANG = "firstNamePefereLang";
	public static final String USER_FIRST_NAME_SUPPORTIVE_LANG = "firstNameSupportiveLang";
	public static final String USER_LAST_NAME_DEFAULT_LANG = "lastNameDefaultLang";
	public static final String USER_LAST_NAME_PREFER_LANG = "lastNamePefereLang";
	public static final String USER_LAST_NAME_SUPPORTIVE_LANG = "lastNameSupportiveLang";
	public static final String USER_USERNAME = "username";
	public static final String USER_DESIGNATION_ID = "designationId";
	public static final String USER_REPORTS_TO = "reportsTo";

	//=============================================================================
	// Report Fields
	//=============================================================================
	public static final String REPORT_FUNCTION_ID  = "functionId";
	public static final String REPORT_NAME = "reportName";
	public static final String REPORT_GODOWN_ID  = "godownId";
	public static final String COUNT = "count";
	public static final String SUPPLIER_CATEGORY_ID = "supplierCategoryId";
	public static final String SUPPLIER_NAME_ID = "supplierNameId";
	public static final String STATUS_ID = "statusId";
	public static final String CONTACT_ID = "contactId";
	public static final String ORDER_BY_ID = "orderById";
	public static final String CATEGORY_ID = "categoryId";
	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";
	public static final String RAW_MATERIAL_CATEGORY_ID = "rawMaterialCategoryId";
	public static final String RAW_MATERIAL_ID = "rawMaterialId";
	public static final String MENU_ITEM_CATEGORY_ID = "menuItemCategoryId";
	public static final String CUSTOM_PACKAGE_ID = "customPackageId";
	public static final String CONTACT_CATEGORY_ID = "contactCategoryId";
	public static final String BANK_CONTACT_ID = "bankContactId";
	public static final String MENU_ITEM_SUB_CATEGORY_ID = "menuItemSubCategoryId";
	public static final String KITCHEN_AREA_ID = "kitchenAreaId";
	public static final String DATA_TYPE_ID = "dataTypeId";
	public static final String ORDER_TYPE_ID = "orderTypeId";
	public static final String MENU_ITEM_ID = "menuItemId";
	public static final String ORDER_DATE = "orderDate";
	public static final String IS_DATE_TIME = "isDateTime";

	public static final String TAX_MASTER_FIELD_TAX = "taxString";
	
	public static final String JOURNAL_VOUCHER_DETAILS_FIELD = "journalVoucherDetails";
	
	public static final String INPUT_TRANSFER_TO_HALL_RAW_MATERIALS = "inputTransferToHallRawMaterialDetails";
	public static final String INPUT_TRANSFER_TO_HALL_HALL_MASTER = "hallMaster";
	public static final String INPUT_TRANSFER_TO_HALL_ORDER = "bookOrder";
	public static final String INPUT_TRANSFER_TO_HALL_TRANSFER_DATE = "transferDate";
	public static final String INPUT_TRANSFER_TO_HALL_TRANSFER_DATE_STRING = "transferDateString";
	
	public static final String CASH_PAYMENT_RECEIPT_TRANSACTION_DATE = "transactionDate";
	public static final String CASH_PAYMENT_RECEIPT_TRANSACTION_TYPE = "transactionType";
	public static final String CASH_PAYMENT_RECEIPT_DETAILS_LIST = "cashPaymentReceiptDetailsList";
	public static final String CASH_PAYMENT_RECEIPT_DETAILS_VOUCHER_TYPE = "voucherType";

	public static final String PURCHASE_ORDER_DATE = "purchaseDate";
	public static final String PURCHASE_ORDER_CONTACT_SUPPLIER = "contactSupplier";
	public static final String PURCHASE_BILL_DATE = "billDate";
	public static final String PURCHASE_BILL_NUMBER = "billNumber";
	public static final String INPUT_TRANSFER_TO_HALL_ID = "hallId";
	public static final String CASH_PAYMENT_TRANSACTION_TYPE_REPORT = "transactionTypeId";
	public static final String TRANSACTION_TYPE = "transactionType";

	public static final String RAW_MATERIAL_RETURN_TO_HALL_RETURN_DATE_STRING = "returnDateString";
	public static final String RAW_MATERIAL_RETURN_TO_HALL_HALL_MASTER = "hallMaster";
	public static final String RAW_MATERIAL_RETURN_TO_HALL_INPUT_TRANSFER_TO_HALL = "inputTransferToHall";

	public static final String TOTAL_AMOUNT = "totalAmount";
	public static final String TRANSACTION_DATE = "transactionDate";
	public static final String VOUCHER_DATE = "voucherDate";
	public static final String TOTAL_CR_DR = "totalCrDr";
	public static final String TAXABLE_AMOUNT = "taxableAmount";
	public static final String AMOUNT = "amount";

}