package com.catering.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing the different types of subscription transactions.
 * Each type is associated with a unique ID, which helps to distinguish between various transaction types.
 * <p>These transaction types are used to track different events related to subscriptions, such as 
 * the initial subscription, renewals, and changes in subscription details.</p>
 * 
 * <p>Available transaction types:</p>
 * <ul>
 * <li><b>DEMO:</b> Represents a demo subscription.</li>
 * <li><b>SUBSCRIPTION:</b> Represents a regular subscription.</li>
 * <li><b>EXTRA_USER:</b> Represents an additional user added to the subscription.</li>
 * <li><b>RENEW:</b> Represents the renewal of a subscription.</li>
 * <li><b>MODULE_SWITCH:</b> Represents a switch between subscription modules.</li>
 * <li><b>DEMO_TO_SUBSCRIPTION:</b> Represents the upgrade from a demo to a full subscription.</li>
 * <li><b>DEMO_EXTEND:</b> Represents an extension of a demo subscription.</li>
 * </ul>
 * 
 * <p>This enum also includes a static method {@link #of(Long id)} to retrieve an enum constant based on its ID.</p>
 */
@Getter
@AllArgsConstructor
public enum SubscriptionTransactionTypeEnum {

	DEMO(0l), SUBSCRIPTION(1l), EXTRA_USER(2l), RENEW(3l), MODULE_SWITCH(5l), DEMO_TO_SUBSCRIPTION(6l), DEMO_EXTEND(7l);

	private Long id;

	/**
	 * Static method to get the corresponding enum constant based on the ID.
	 * 
	 * @param id the ID of the subscription transaction type
	 * @return the corresponding {@link SubscriptionTransactionTypeEnum} enum constant
	 * @throws IllegalArgumentException if the provided ID does not match any of the enum constants
	 */
	public static SubscriptionTransactionTypeEnum of(Long id) {
		return Arrays.stream(values()).filter(e -> e.getId().equals(id)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Invalid subscription type id: " + id));
	}

}