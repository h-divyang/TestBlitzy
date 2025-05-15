package com.catering.model.tenant;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "input_transfer_to_hall")
public class InputTransferToHallModel extends AuditByIdModel {

	@Column(name = "transfer_date")
	private LocalDate transferDate;

	@Column(name = "transfer_date", insertable = false, updatable = false)
	private String transferDateString;

	@JoinColumn(name = "fk_hall_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private HallMasterModel hallMaster;

	@JoinColumn(name = "fk_customer_order_details_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private BookOrderModel bookOrder;

	@OneToMany(mappedBy = "inputTransferToHallRawMaterial", cascade = CascadeType.ALL, orphanRemoval = true)
	List<InputTransferToHallRawMaterialModel> inputTransferToHallRawMaterials;

	/**
	 * Creates a new instance of {@link InputTransferToHallModel} with the transfer date
	 * initialized to the provided query string. The method attempts to convert the query string
	 * into a database-compatible date format (yyyy-MM-dd) by reversing and formatting each component
	 * of the date (day, month, year).
	 *
	 * @param query The search query representing the transfer date, typically in the format dd-MM-yyyy.
	 * @return A new instance of {@link InputTransferToHallModel} with the transfer date formatted for the database.
	 */
	public static InputTransferToHallModel ofSearchingModel(String query) {
		return InputTransferToHallModel.builder()
			.transferDateString(getDateInDatabaseFormat(query))
			.build();
	}

	/**
	 * Converts the given date query string into a database-friendly format (yyyy-MM-dd).
	 * It splits the input string by the delimiter "-", reverses the components, and
	 * ensures each component (day, month, year) is zero-padded to a width of 2 digits if needed.
	 *
	 * @param query The date string to be converted.
	 * @return The formatted date string in yyyy-MM-dd format.
	 */
	private static String getDateInDatabaseFormat(String query) {
		if (StringUtils.isNotBlank(query)) {
			List<String> dateList = Arrays.asList(StringUtils.split(query, "-"));
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