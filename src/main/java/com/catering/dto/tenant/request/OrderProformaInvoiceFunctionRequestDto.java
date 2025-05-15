package com.catering.dto.tenant.request;

import java.time.LocalDateTime;
import com.catering.dto.audit.AuditIdDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderProformaInvoiceFunctionRequestDto extends AuditIdDto {

	private Long orderProformaInvoiceId;

	private Long orderFunctionId;

	private Integer person;

	private Double extra;

	private Double rate;

	private String chargesName;

	private LocalDateTime date;

}