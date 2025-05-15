package com.catering.service.tenant.impl;

import com.catering.constant.MessagesConstant;
import com.catering.dto.tenant.request.CommonNotesDto;
import com.catering.dto.tenant.request.FunctionAddressDto;
import com.catering.dto.tenant.request.OrderFunctionRawMaterialTimeDto;
import com.catering.model.tenant.OrderFunctionModel;
import com.catering.repository.tenant.OrderFunctionRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.OrderFunctionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Implementation of the OrderFunctionService interface, providing methods for managing
 * and updating order function details such as rates, notes, and function addresses.
 *
 * This service utilizes the ExceptionService for error handling, the MessageService
 * for retrieving messages, and the OrderFunctionRepository for database operations.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderFunctionServiceImpl implements OrderFunctionService {

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for retrieving and managing localized messages.
	 */
	MessageService messageService;

	/**
	 * Repository for managing order function data.
	 */
	OrderFunctionRepository orderFunctionRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateRate(Double rate, Long id) {
		orderFunctionRepository.updateRate(rate, id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNotes(CommonNotesDto commonNotesDto) {
		if (!orderFunctionRepository.existsById(commonNotesDto.getId())) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		Optional<OrderFunctionModel> orderFunctionModel = orderFunctionRepository.findById(commonNotesDto.getId());
		if (orderFunctionModel.isPresent()) {
			OrderFunctionModel orderFunction = orderFunctionModel.get();
			orderFunction.setNoteDefaultLang(commonNotesDto.getNoteDefaultLang());
			orderFunction.setNotePreferLang(commonNotesDto.getNotePreferLang());
			orderFunction.setNoteSupportiveLang(commonNotesDto.getNoteSupportiveLang());
			orderFunctionRepository.save(orderFunction);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateFunctionAddress(FunctionAddressDto functionAddressDto) {
		if (!orderFunctionRepository.existsById(functionAddressDto.getId())) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		Optional<OrderFunctionModel> orderFunctionModel = orderFunctionRepository.findById(functionAddressDto.getId());
		if (orderFunctionModel.isPresent()) {
			OrderFunctionModel orderFunction = orderFunctionModel.get();
			orderFunction.setFunctionAddressDefaultLang(functionAddressDto.getFunctionAddressDefaultLang());
			orderFunction.setFunctionAddressPreferLang(functionAddressDto.getFunctionAddressPreferLang());
			orderFunction.setFunctionAddressSupportiveLang(functionAddressDto.getFunctionAddressSupportiveLang());
			orderFunctionRepository.save(orderFunction);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateRawMaterialTime(OrderFunctionRawMaterialTimeDto orderFunctionRawMaterialTimeDto) {
		if (!orderFunctionRepository.existsById(orderFunctionRawMaterialTimeDto.getId())) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		Optional<OrderFunctionModel> orderFunctionModel = orderFunctionRepository.findById(orderFunctionRawMaterialTimeDto.getId());
		if (orderFunctionModel.isPresent()) {
			OrderFunctionModel orderFunction = orderFunctionModel.get();
			orderFunction.setRawMaterialTime(orderFunctionRawMaterialTimeDto.getRawMaterialTime());
			orderFunctionRepository.save(orderFunction);
		}
	}

}