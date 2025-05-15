package com.catering.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing various types of vouchers.
 * Each type corresponds to a specific business process or transaction and is associated with a unique ID and name.
 * These voucher types are used to categorize and identify different types of financial transactions or records.
 * 
 * <p>Available voucher types:</p>
 * <ul>
 * <li><b>PURCHASE_BILL:</b> Represents a purchase bill voucher.</li>
 * <li><b>CHEF_LABOUR:</b> Represents a voucher for chef labour costs.</li>
 * <li><b>OUTSIDE_LABOUR:</b> Represents a voucher for external labour costs.</li>
 * <li><b>LABOUR_ALLOCATION:</b> Represents a voucher for labour allocation.</li>
 * <li><b>DEBIT_NOTE:</b> Represents a debit note voucher.</li>
 * <li><b>INVOICE:</b> Represents an invoice voucher.</li>
 * <li><b>ADVANCE_PAYMENT:</b> Represents an advance payment voucher.</li>
 * </ul>
 * 
 * <p>This enum also includes a static utility method {@link #getIds(VoucherTypeEnum... voucherTypes)} 
 * that allows retrieving a list of IDs for the provided enum constants.</p>
 */
@Getter
@AllArgsConstructor
public enum VoucherTypeEnum {

	PURCHASE_BILL(1, "Purchase bill"),
	CHEF_LABOUR(2, "Chef Labour"),
	OUTSIDE_LABOUR(3, "Outside Labour"),
	LABOUR_ALLOCATION(4, "Labour Allocation"),
	DEBIT_NOTE(5, "Debit note"),
	INVOICE(6, "Invoice"),
	ADVANCE_PAYMENT(7, "Advance Payment");

	private int id;
	private String name;

	/**
	 * Static utility method to get a list of IDs from provided enum constants.
	 * 
	 * @param voucherTypes the enum constants whose IDs are to be retrieved
	 * @return a list of IDs corresponding to the provided enum constants
	 */
	public static List<Integer> getIds(VoucherTypeEnum... voucherTypes) {
		return Arrays.stream(voucherTypes) // Stream of provided enum values
				.map(VoucherTypeEnum::getId) // Map each enum to its ID
				.collect(Collectors.toList()); // Collect IDs into a List
	}

}