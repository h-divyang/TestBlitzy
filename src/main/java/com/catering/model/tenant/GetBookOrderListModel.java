package com.catering.model.tenant;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_order_details")
public class GetBookOrderListModel extends AuditByIdModel {

	@JoinColumn(name = "fk_contact_customer_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private GetBookOrderContactModel contactCustomer;

	@JoinColumn(name = "fk_contact_manager_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private GetBookOrderContactModel contactManager;

	@JoinColumn(name = "fk_contact_chef_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private GetBookOrderContactModel contactChef;

	@Column(name = "venue_default_lang")
	private String venueDefaultLang;

	@Column(name = "venue_prefer_lang")
	private String venuePreferLang;

	@Column(name = "venue_supportive_lang")
	private String venueSupportiveLang;

	@JoinColumn(name = "fk_event_type_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EventTypeModel eventType;

	@Column(name = "event_main_date")
	private LocalDate eventMainDate;

	@JoinColumn(name = "fk_order_status_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private OrderStatusModel status;

	@Column(name = "event_main_date", insertable = false, updatable = false)
	private String eventMainDateString;

	/**
	 * Creates a new instance of {@link GetBookOrderListModel} with the event date set in database format.
	 * The input query is assumed to be a date string in the format "dd/MM/yyyy", and it is converted
	 * to "yyyy-MM-dd" format for database compatibility.
	 *
	 * @param query The input date string used to populate the event date in the model.
	 * @return A new instance of {@link GetBookOrderListModel} with the formatted event date.
	 */
	public static GetBookOrderListModel ofSearchingModel(String query) {
		return GetBookOrderListModel.builder()
			.eventMainDateString(getDateInDatabaseFormat(query))
			.build();
	}

	/**
	 * Converts a date string from "dd/MM/yyyy" format to "yyyy-MM-dd" format for database storage.
	 * Handles single-digit day and month values by padding them with leading zeroes.
	 *
	 * @param query The input date string in "dd/MM/yyyy" format.
	 * @return The formatted date string in "yyyy-MM-dd" format, or the original query if it's not a valid date.
	 */
	private static String getDateInDatabaseFormat(String query) {
		if (StringUtils.isNotBlank(query)) {
			List<String> dateList = Arrays.asList(StringUtils.split(query, "/"));
			dateList.forEach(date -> {
				if (date.length() == 1 && NumberUtils.isDigits(date) && Integer.parseInt(date) > 0) {
					dateList.set(dateList.indexOf(date), String.format("%02d", Integer.parseInt(date)));
				}
			});
			Collections.reverse(dateList);
			return String.join("-", dateList);
		}
		return query;
	}

}