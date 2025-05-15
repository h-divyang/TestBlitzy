package com.catering.model.tenant;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
@Table(name = "order_menu_preparation_menu_item")
public class SaveMenuAllocationOrderMenuPreparationMenuItemModel extends AuditByIdModel {

	@Column(name = "order_type", columnDefinition = "TINYINT default '0' COMMENT '0 - Default, 1 - Chef Labour, 2 - Outside'")
	private Integer orderType;

	@Column(name = "order_date")
	private LocalDateTime orderDate;

	@Column(name = "person")
	private Integer person;
	
	@Column(name = "fk_godown_id")
	private Long godown;

	@OneToMany(mappedBy = "menuPreparationMenuItem", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SaveCustomMenuAllocationTypeModel> allocationType;

}