package com.catering.model.tenant;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_crockery")
public class OrderCrockeryModel extends AuditByIdModel {

	@Column(name = "fk_raw_material_id")
	private Long rawMaterialId;

	@Column(name = "fk_order_function_id")
	private Long orderFunction;

	@Column(name = "qty")
	private Double qty;

	@Column(name = "fk_measurement_id")
	private Long measurementId;

	@Column(name = "price")
	private Double price;

	@Column(name = "order_time")
	private LocalDateTime orderTime;

	@Column(name = "fk_agency_id")
	private Long agency;

	@Column(name = "fk_godown_id")
	private Long godown;

}