package com.catering.dto.tenant.request;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.AuditIdDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RawMaterialReturnToHallDto extends AuditIdDto {

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private LocalDate returnDate;

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	List<InputTransferToHallRawMaterialDto> rawMaterialReturnToHallDetails;

	private HallMasterDto hallMaster;

	private InputTransferToHallDto inputTransferToHall;

}