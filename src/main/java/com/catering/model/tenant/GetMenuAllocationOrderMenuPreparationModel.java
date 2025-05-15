package com.catering.model.tenant;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "order_menu_preparation")
public class GetMenuAllocationOrderMenuPreparationModel extends AuditIdModelOnly {

	@JoinColumn(name = "fk_order_function_id")
	@OneToOne(fetch = FetchType.LAZY)
	private GetMenuAllocationOrderFunctionModel orderFunction;

	@OneToMany(mappedBy = "menuPreparation")
	private List<GetMenuAllocationOrderMenuPreparationMenuItemModel> menuPreparationMenuItem;

}