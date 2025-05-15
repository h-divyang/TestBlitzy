package com.catering.model.audit;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.catering.constant.FieldConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class AuditIdModelOnly {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = FieldConstants.COMMON_FIELD_ID)
	private Long id;

}