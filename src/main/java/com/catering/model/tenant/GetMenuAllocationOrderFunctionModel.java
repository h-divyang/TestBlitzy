package com.catering.model.tenant;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class GetMenuAllocationOrderFunctionModel extends AuditIdModelOnly {

	@Column(name = "fk_customer_order_details_id")
	private long bookOrder;

	@Column(name = "person")
	private Long person;

	@Column(name = "date")
	private LocalDateTime date;

	@JoinColumn(name = "fk_function_type_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private GetMenuAllocationFunctionTypeModel functionType;

}