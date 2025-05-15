package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class LoginLogDto extends AuditIdDto {

	private Long userId;

	private LocalDateTime loginTimeStamp;

	private boolean status;

	private String ipAddress;

	private String username;

}