package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "raw_material_supplier")
public class RawMaterialSupplierModel extends AuditByIdModel {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_contact_id")
	private ContactModel contact;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_raw_material_id")
	private RawMaterialModel rawMaterial;

	@Column(name = "is_default", columnDefinition = "Boolean Default false")
	private Boolean isDefault;

	/**
	 * Creates a new instance of {@link RawMaterialSupplierModel} for searching purposes.
	 * This method returns a builder instance of the model without initializing any specific fields,
	 * making it useful when you want to perform a search or filtering operation on the supplier model.
	 *
	 * @return A new instance of {@link RawMaterialSupplierModel} with default values.
	 */
	public static RawMaterialSupplierModel ofSearchingModel() {
		return RawMaterialSupplierModel.builder()
				.build();
	}

}