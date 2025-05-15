package com.catering.model.tenant;

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
@Table(name = "function_type")
public class GetMenuAllocationFunctionTypeModel extends AuditIdModelOnly {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

}