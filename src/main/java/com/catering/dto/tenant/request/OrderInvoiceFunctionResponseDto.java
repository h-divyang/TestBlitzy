package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import com.catering.dto.audit.IdDto;

import lombok.Getter;

/**
 * Data Transfer Object (DTO) representing the response for order invoice functions.
 * Extends {@link IdDto} to include an identifier field.
 *
 * The class encapsulates information about an order invoice function, including details such as
 * function names in default, preferred, and supportive languages, the number of persons, additional charges (extra),
 * rate, calculated amount, and identifiers for the associated order invoice and order function.
 *
 * This DTO is typically used to transfer order invoice function response data between different layers of the application.
 *
 * The class provides a constructor for initializing its fields.
 *
 * @author Krushali Talaviya
 * @since 23rd January 2024
 */
@Getter
public class OrderInvoiceFunctionResponseDto extends IdDto {

	private String functionNameDefaultLang;

	private String functionNamePreferLang;

	private String functionNameSupportiveLang;

	private LocalDateTime date;

	private Double person;

	private Double extra;

	private Double rate;

	private Double functionRate;

	private Double amount;

	private Long orderInvoiceId;

	private Long orderFunctionId;

	/**
	 * Constructs an {@code OrderInvoiceFunctionResponseDto} with the specified parameters.
	 *
	 * @param id                         The identifier of the order invoice function.
	 * @param functionNameDefaultLang    The function name in the default language.
	 * @param functionNamePreferLang     The function name in the preferred language.
	 * @param functionNameSupportiveLang The function name in the supportive language.
	 * @param person                     The number of persons associated with the function.
	 * @param extra                      Additional charges associated with the function.
	 * @param rate                       The rate or price per unit for the function.
	 * @param functionRate               The functionRate or price per unit for the function.
	 * @param amount                     The calculated amount for the function.
	 * @param orderInvoiceId             The identifier of the associated order invoice.
	 * @param orderFunctionId            The identifier of the associated order function.
	 */
	public OrderInvoiceFunctionResponseDto(Long id, String functionNameDefaultLang,
			String functionNamePreferLang, String functionNameSupportiveLang, LocalDateTime date, Double person, Double extra, Double rate, Double functionRate,
			Double amount, Long orderInvoiceId, Long orderFunctionId) {
		setId(id);
		this.functionNameDefaultLang = functionNameDefaultLang;
		this.functionNamePreferLang = functionNamePreferLang;
		this.functionNameSupportiveLang = functionNameSupportiveLang;
		this.date = date;
		this.person = person;
		this.extra = extra;
		this.rate = rate;
		this.functionRate = functionRate;
		this.amount = amount;
		this.orderInvoiceId = orderInvoiceId;
		this.orderFunctionId = orderFunctionId;
	}

}