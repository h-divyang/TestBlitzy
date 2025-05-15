package com.catering.enums;

/**
 * Enum representing different types of orders in the system.
 * This enum is used to categorize orders based on their type, such as 
 * "DEFAULT", "CHEF_LABOUR", and "OUTSIDE_FOOD".
 * 
 * <p>Each enum constant has an associated integer value, which can be 
 * retrieved using the {@link #getValue()} method.</p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * OrderTypeEnum orderType = OrderTypeEnum.DEFAULT;
 * int orderTypeValue = orderType.getValue();
 * </pre>
 */
public enum OrderTypeEnum {

	/**
	 * Represents the default order type.
	 */
	DEFAULT(0),

	/**
	 * Represents an order where chef labour is involved.
	 */
	CHEF_LABOUR(1),
	
	/**
	 * Represents an order that involves outside food.
	 */
	OUTSIDE_FOOD(2);

	private final int value;  // Integer value representing the order type

	/**
	 * Constructor to set the value for each enum constant.
	 * 
	 * @param value the integer value representing the order type
	 */
	OrderTypeEnum(int value) {
		this.value = value;
	}

	/**
	 * Gets the integer value associated with the order type.
	 * 
	 * @return the integer value representing the order type
	 */
	public int getValue() {
		return value;
	}

}