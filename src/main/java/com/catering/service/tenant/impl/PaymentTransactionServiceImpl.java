package com.catering.service.tenant.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.catering.dto.tenant.request.PaymentTransactionDto;
import com.catering.repository.tenant.PaymentTransactionRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.PaymentTransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the PaymentTransactionService interface.
 * This service handles operations related to payment transactions,
 * including fetching all transaction records in descending order of their IDs.
 *
 * This class utilizes the PaymentTransactionRepository to interact with
 * the data layer and the ModelMapperService to convert entities to DTOs.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

	/**
	 * Repository for managing payment transactions.
	 */
	PaymentTransactionRepository paymentTransactionRepository;

	/**
	 * Service for mapping models and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PaymentTransactionDto> findAll() {
		return modelMapperService.convertListEntityAndListDto(paymentTransactionRepository.findByOrderByIdDesc(), PaymentTransactionDto.class);
	}

}