package com.catering.model.tenant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "contact_categories")
public class ContactCategoriesModel extends AuditIdModelOnly {

	@JoinColumn(name = "fk_contact_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ContactModel contact;

	@JoinColumn(name = "fk_contact_category_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ContactCategoryModel contactCategory;

}