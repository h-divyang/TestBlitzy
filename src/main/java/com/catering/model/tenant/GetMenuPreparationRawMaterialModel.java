package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "raw_material")
public class GetMenuPreparationRawMaterialModel extends AuditIdModelOnly {

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "priority")
	private Integer priority;

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

}