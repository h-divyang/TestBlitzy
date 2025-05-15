package com.catering.model.tenant;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "contact")
public class ContactModel extends AuditByIdModel {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	@Column(name = "short_name_default_lang")
	private String shortNameDefaultLang;

	@Column(name = "short_name_prefer_lang")
	private String shortNamePreferLang;

	@Column(name = "short_name_supportive_lang")
	private String shortNameSupportiveLang;

	@Column(name = "home_address_default_lang")
	private String homeAddressDefaultLang;

	@Column(name = "home_address_prefer_lang")
	private String homeAddressPreferLang;

	@Column(name = "home_address_supportive_lang")
	private String homeAddressSupportiveLang;

	@Column(name = "venue_default_lang")
	private String venueDefaultLang;

	@Column(name = "venue_prefer_lang")
	private String venuePreferLang;

	@Column(name = "venue_supportive_lang")
	private String venueSupportiveLang;

	@Column(name = "email")
	private String email;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "alternative_number")
	private String alternativeNumber;

	@Column(name = "office_number")
	private String officeNumber;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "gst_number")
	private String gstNumber;

	@Column(name = "pan_number")
	private String panNumber;

	@Column(name = "aadhar_number")
	private String aadharNumber;

	@Column(name = "birth_date")
	private LocalDate birthDate;

	@Column(name = "anniversary_date")
	private LocalDate anniversaryDate;

	@Column(name = "labour_price")
	private Double labourPrice;

	@Column(name = "opening_balance")
	private Double openingBalance;

	@Column(name = "transaction_type")
	private Integer transactionType;

	@Column(name = "lock_date")
	private LocalDate lockDate;

	@Column(name = "is_cash")
	private Integer isCash;

	@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ContactCategoriesModel> categoryMapping;

	/**
	 * Creates a new instance of {@link ContactModel} with the provided query value.
	 * Assigns the query value to the `nameDefaultLang`, `namePreferLang`, `nameSupportiveLang`, and `mobileNumber` fields.
	 *
	 * @param query The search query used to populate the fields of {@link ContactModel}.
	 * @return A new instance of {@link ContactModel} populated with the query.
	 */
	public static ContactModel ofSearchingModel(String query) {
		return ContactModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.mobileNumber(query)
			.build();
	}

}