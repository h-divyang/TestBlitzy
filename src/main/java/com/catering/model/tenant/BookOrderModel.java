package com.catering.model.tenant;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "customer_order_details")
public class BookOrderModel extends AuditByIdModel {

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

	@Column(name = "party_plot_name_default_lang")
	private String partyPlotNameDefaultLang;

	@Column(name = "party_plot_name_prefer_lang")
	private String partyPlotNamePreferLang;

	@Column(name = "party_plot_name_supportive_lang")
	private String partyPlotNameSupportiveLang;

	@Column(name = "location_url")
	private String locationUrl;

	@JoinColumn(name = "fk_event_type_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EventTypeModel eventType;

	@Column(name = "reference_by")
	private String referenceBy;

	@Column(name = "meeting_date")
	private LocalDate MeetingDate;

	@Column(name = "event_main_date")
	private LocalDate eventMainDate;

	@JoinColumn(name = "fk_order_status_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private OrderStatusModel status;

	@Column(name = "adjust_quantity", updatable = false)
	private Boolean isAdjustQuantity;

	@Column(name = "event_main_date", insertable = false, updatable = false)
	private String eventMainDateString;

	@JoinColumn(name = "fk_meal_type_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private MealTypeModel mealType;

	@Column(name = "meal_note_default_lang")
	private String mealNoteDefaultLang;

	@Column(name = "meal_note_prefer_lang")
	private String mealNotePreferLang;

	@Column(name = "meal_note_supportive_lang")
	private String mealNoteSupportiveLang;

	@Column(name = "groom_name")
	private String groomName;

	@Column(name = "groom_birth_date")
	private LocalDate groomBirthDate;

	@Column(name = "groom_community")
	private String groomCommunity;

	@Column(name = "bride_name")
	private String brideName;

	@Column(name = "bride_birth_date")
	private LocalDate brideBirthDate;

	@Column(name = "bride_community")
	private String brideCommunity;

	@OneToMany(mappedBy = "bookOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	List<OrderFunctionModel> functions;

}