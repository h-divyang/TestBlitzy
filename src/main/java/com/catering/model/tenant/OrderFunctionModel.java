package com.catering.model.tenant;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;

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
@Table(name = "order_function")
public class OrderFunctionModel extends AuditIdModelOnly {

	@JoinColumn(name = "fk_function_type_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private FunctionTypeModel functionType;

	@JoinColumn(name = "fk_customer_order_details_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private BookOrderModel bookOrder;

	@Column(name = "person")
	private Integer person;

	@Column(name = "date")
	private LocalDateTime date;

	@Column(name = "end_date")
	private LocalDateTime endDate;

	@Column(name = "rate")
	private Double rate;

	@Column(name = "sequence")
	private Integer sequence;

	@Column(name = "copied_function_sequence")
	private Integer copiedFunctionSequence;

	@Column(name = "note_default_lang")
	private String noteDefaultLang;

	@Column(name = "note_prefer_lang")
	private String notePreferLang;

	@Column(name = "note_supportive_lang")
	private String noteSupportiveLang;

	@Column(name = "function_address_default_lang")
	private String functionAddressDefaultLang;

	@Column(name = "function_address_prefer_lang")
	private String functionAddressPreferLang;

	@Column(name = "function_address_supportive_lang")
	private String functionAddressSupportiveLang;

	@Column(name = "raw_material_time")
	private LocalDateTime rawMaterialTime;

	@OneToMany(mappedBy = "orderFunction")
	List<OrderMenuPreparationModel> orderMenuPreparations;

}