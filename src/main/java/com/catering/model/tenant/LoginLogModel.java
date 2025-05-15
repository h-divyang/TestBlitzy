package com.catering.model.tenant;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "login_log")
public class LoginLogModel extends AuditByIdModel {

	@Column(name = "user_id")
	private Long userId;

	@Column(name="login_timestamp")
	private LocalDateTime loginTimeStamp;

	@Column(name="status")
	private Boolean status;

	@Column(name = "ip_address")
	private String ipAddress;

	@Column(name = "username")
	private String username;

}