package com.catering.model.superadmin;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import com.catering.model.audit.AuditIdModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "company")
public class CompanyModel extends AuditIdModel {

	@Column(name = "tenant")
	@Type(type = "uuid-binary")
	private UUID tenant;

	@Column(name = "unique_code")
	private String uniqueCode;

	@PrePersist
	protected void onCreate() {
		tenant = UUID.randomUUID();
	}

}