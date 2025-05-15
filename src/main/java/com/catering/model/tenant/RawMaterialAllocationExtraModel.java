package com.catering.model.tenant;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "raw_material_allocation_extra")
public class RawMaterialAllocationExtraModel extends AuditIdModelOnly {

	@Column(name = "order_time")
	private LocalDateTime orderTime;

	@Column(name = "fk_contact_agency_id")
	private Long contactAgencyId;

	@Column(name = "fk_customer_order_details_id")
	private Long orderId;

	@Column(name = "fk_raw_material_id")
	private Long rawMaterialId;

	@Column(name = "fk_godown_id")
	private Long godownId;

	@Column(name = "quantity")
	private Double quantity;

	@Column(name = "fk_measurement_id")
	private Long measurementId;

	@Column(name = "total")
	private Double total;

}