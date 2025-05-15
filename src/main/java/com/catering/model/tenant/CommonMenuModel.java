package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.catering.model.audit.AuditIdModelOnly;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class CommonMenuModel extends AuditIdModelOnly {

	/**
	 * The title of common menu
	 */
	@Column(name = "name")
	private String title;

	/**
	 * The sequence number of the common menu.
	 */
	@Column(name = "sequence")
	private int sequence;

}