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

/**
 * Entity class representing a purchase order.
 *
 * @author Krushali Talaviya
 * @since 2024-05-31
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "purchase_order")
public class PurchaseOrderModel extends AuditByIdModel {

	@Column(name = "purchase_date")
	private LocalDate purchaseDate;

	@JoinColumn(name = "fk_contact_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ContactModel contactSupplier;

	@OneToMany(mappedBy = "purchaseOrder", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private List<PurchaseOrderRawMaterialModel> purchaseOrderRawMaterialList;

}