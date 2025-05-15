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
@Table(name = "raw_material_return_to_hall")
public class RawMaterialReturnToHallModel extends AuditByIdModel {

	@Column(name = "return_date")
	private LocalDate returnDate;

	@Column(name = "return_date", insertable = false, updatable = false)
	private String returnDateString;

	@JoinColumn(name = "fk_hall_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private HallMasterModel hallMaster;

	@JoinColumn(name = "fk_input_transfer_to_hall_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private InputTransferToHallModel inputTransferToHall;

	@OneToMany(mappedBy = "rawMaterialReturnToHall", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RawMaterialReturnToHallDetailsModel> rawMaterialReturnToHallDetails;

	/**
	 * Creates a new instance of {@link RawMaterialReturnToHallModel} with the return date initialized based on the provided query.
	 * The return date is parsed from the query string and formatted into a database-compatible format (YYYY-MM-DD).
	 * This method is useful for searching or filtering raw material return records by the return date.
	 *
	 * @param query The search query representing the return date in MM/DD/YYYY or other formats.
	 * @return A new instance of {@link RawMaterialReturnToHallModel} with the return date set according to the query value.
	 */
	public static RawMaterialReturnToHallModel ofSearchingModel(String query) {
		return RawMaterialReturnToHallModel.builder()
			.returnDateString(getDateInDatabaseFormat(query))
			.build();
	}

	/**
	 * Converts a date string in various formats (e.g., MM/DD/YYYY) into a database-compatible date format (YYYY-MM-DD).
	 * If the query string contains an invalid date, it returns the original query.
	 *
	 * @param query The date string to be converted into database format.
	 * @return The formatted date string in the format YYYY-MM-DD, or the original query if no valid date is found.
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