package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dao.input_transfer_to_hall.InputTransferToHallNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.InputTransferToHallCalculationDto;
import com.catering.dto.tenant.request.InputTransferToHallDto;
import com.catering.dto.tenant.request.InputTransferToHallRawMaterialDropDownDto;
import com.catering.dto.tenant.request.InputTransferToHallResponseDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderRawMaterial;
import com.catering.model.tenant.HallMasterModel;
import com.catering.model.tenant.InputTransferToHallModel;
import com.catering.repository.tenant.InputTransferToHallRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.InputTransferToHallService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.RequestResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for handling operations related to InputTransferToHall.
 * This service provides methods for creating, updating, retrieving, and deleting InputTransferToHall entities,
 * along with additional operations for related data processing.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class InputTransferToHallServiceImpl extends GenericServiceImpl<InputTransferToHallResponseDto, InputTransferToHallModel, Long> implements InputTransferToHallService {

	/**
	 * Service for converting between DTOs and entity models.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Service for retrieving and managing localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Repository for managing Input Transfer to Hall entities.
	 */
	InputTransferToHallRepository inputTransferToHallRepository;

	/**
	 * Service for executing native queries related to Input Transfer to Hall operations.
	 */
	InputTransferToHallNativeQueryService inputTransferToHallNativeQueryService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<InputTransferToHallDto> createUpdateInputTransferToHall(InputTransferToHallDto inputTransferToHallDto) {
		inputTransferToHallDto.setIsActive(true);
		InputTransferToHallModel inputTransferToHallModel = modelMapperService.convertEntityAndDto(inputTransferToHallDto, InputTransferToHallModel.class);
		inputTransferToHallModel.getInputTransferToHallRawMaterials().forEach(inputTransferToHallRawMaterial -> inputTransferToHallRawMaterial.setInputTransferToHallRawMaterial(inputTransferToHallModel));
		DataUtils.setAuditFields(inputTransferToHallRepository, inputTransferToHallDto.getId(), inputTransferToHallModel);
		return Optional.of(modelMapperService.convertEntityAndDto(inputTransferToHallRepository.save(inputTransferToHallModel), InputTransferToHallDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<InputTransferToHallResponseDto>> getAllInputTransferToHall(FilterDto filterDto) {
		Optional<Example<InputTransferToHallModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			InputTransferToHallModel inputTransferToHallModel = InputTransferToHallModel.ofSearchingModel(query);
			inputTransferToHallModel.setHallMaster(HallMasterModel.builder().nameDefaultLang(query).namePreferLang(query).nameSupportiveLang(query).build());
			example = Optional.of(Example.of(inputTransferToHallModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		ResponseContainerDto<List<InputTransferToHallResponseDto>> inputTransferToHallResponseDto = read(InputTransferToHallResponseDto.class, InputTransferToHallModel.class, filterDto, example);
		ResponseContainerDto<List<InputTransferToHallResponseDto>> inputTransferToHallList = RequestResponseUtils.generateResponseDto(getWeightCalculation(inputTransferToHallResponseDto.getBody()));
		inputTransferToHallList.setPaging(inputTransferToHallResponseDto.getPaging());
		return inputTransferToHallList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteInputTransferToHall(Long id) {
		if (!inputTransferToHallRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		try {
			inputTransferToHallRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<InputTransferToHallDto> getInputTransferToHall(Long id) {
		Optional<InputTransferToHallModel> inputTransferToHallOp = inputTransferToHallRepository.findById(id);
		if (inputTransferToHallOp.isPresent()) {
			InputTransferToHallDto inputTransferToHallDto = modelMapperService.convertEntityAndDto(inputTransferToHallOp.get(), InputTransferToHallDto.class);
			return Optional.of(inputTransferToHallDto);
		} else {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<InputTransferToHallUpcomingOrderDto> getUpcomingOrdersForInputTransferToHall(Long orderId) {
		return inputTransferToHallNativeQueryService.getUpcomingOrdersForInputTransferToHall(orderId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<InputTransferToHallUpcomingOrderRawMaterial> findInputTransferToHallRawMaterialByOrderId(Long orderId) {
		return inputTransferToHallNativeQueryService.findInputTransferToHallRawMaterialByOrderId(orderId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<InputTransferToHallRawMaterialDropDownDto> getRawMaterial() {
		return inputTransferToHallNativeQueryService.getRawMaterial();
	}

	/**
	 * Retrieves an ExampleMatcher configured with specific matchers and ignore paths.
	 *
	 * @return An ExampleMatcher configured with matchers for common field names in default, preferred, and supportive languages, and with ignore paths for audit fields.
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher
			.matchingAny()
			.withMatcher(FieldConstants.INPUT_TRANSFER_TO_HALL_TRANSFER_DATE_STRING, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.INPUT_TRANSFER_TO_HALL_HALL_MASTER + "." + FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.INPUT_TRANSFER_TO_HALL_HALL_MASTER + "." + FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.INPUT_TRANSFER_TO_HALL_HALL_MASTER + "." + FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG,ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName(), BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.INPUT_TRANSFER_TO_HALL_ORDER), BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.INPUT_TRANSFER_TO_HALL_HALL_MASTER)));
	}

	/**
	 * Calculates and updates the weight information for a list of input transfers to hall.
	 *
	 * This method processes a list of {@code InputTransferToHallResponseDto} objects, retrieves the
	 * corresponding calculation details for each entry using the specified service, and updates the DTO
	 * with additional fields like weight names in various languages and customer order ID.
	 *
	 * @param inputTransferToHallResponseDtos A list of {@code InputTransferToHallResponseDto} objects that need their
	 *										  weight-related fields to be calculated and populated.
	 * @return The updated list of {@code InputTransferToHallResponseDto} objects containing the calculated weight-related
	 *		   fields and customer order ID.
	 */
	private List<InputTransferToHallResponseDto> getWeightCalculation(List<InputTransferToHallResponseDto> inputTransferToHallResponseDtos) {
		inputTransferToHallResponseDtos.forEach(inputTransferToHall -> {
			InputTransferToHallCalculationDto inputTransferToHallCalculationDto = inputTransferToHallNativeQueryService.getInputTransferToHallCalculation(inputTransferToHall.getId());
			inputTransferToHall.setCustomerOrderId(inputTransferToHallCalculationDto.getCustomerOrderId());
			inputTransferToHall.setWeightNameDefaultLang(inputTransferToHallCalculationDto.getNameDefaultLang());
			inputTransferToHall.setWeightNamePreferLang(inputTransferToHallCalculationDto.getNamePreferLang());
			inputTransferToHall.setWeightNameSupportiveLang(inputTransferToHallCalculationDto.getNameSupportiveLang());
		});
		return inputTransferToHallResponseDtos;
	}

}