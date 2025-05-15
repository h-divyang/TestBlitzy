package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dao.raw_material_return_to_hall.RawMaterialReturnToHallNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderRawMaterial;
import com.catering.dto.tenant.request.RawMaterialReturnToHallCalculationDto;
import com.catering.dto.tenant.request.RawMaterialReturnToHallDto;
import com.catering.dto.tenant.request.RawMaterialReturnToHallInputTransferToHallDropDownDto;
import com.catering.dto.tenant.request.RawMaterialReturnToHallResponseDto;
import com.catering.model.tenant.HallMasterModel;
import com.catering.model.tenant.RawMaterialReturnToHallModel;
import com.catering.repository.tenant.RawMaterialReturnToHallRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.RawMaterialReturnToHallService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.RequestResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for handling operations related to Raw Material Return to Hall.
 * This class extends the GenericServiceImpl and implements the RawMaterialReturnToHallService interface.
 * It provides CRUD operations and other functionalities for managing raw material
 * returns to hall entities.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class RawMaterialReturnToHallServiceImpl extends GenericServiceImpl<RawMaterialReturnToHallResponseDto, RawMaterialReturnToHallModel, Long> implements RawMaterialReturnToHallService {

	/**
	 * Service for mapping models and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Service for sending messages and notifications.
	 */
	MessageService messageService;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Repository for managing raw material return to hall records.
	 */
	RawMaterialReturnToHallRepository rawMaterialReturnToHallRepository;

	/**
	 * Service for executing native queries related to raw material returns to the hall.
	 */
	RawMaterialReturnToHallNativeQueryService rawMaterialReturnToHallNativeQueryService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<RawMaterialReturnToHallDto> createUpdateRawMaterialReturnToHall(RawMaterialReturnToHallDto rawMaterialReturnToHallDto) {
		rawMaterialReturnToHallDto.setIsActive(true);
		RawMaterialReturnToHallModel rawMaterialReturnToHallModel = modelMapperService.convertEntityAndDto(rawMaterialReturnToHallDto, RawMaterialReturnToHallModel.class);
		rawMaterialReturnToHallModel.getRawMaterialReturnToHallDetails().forEach(rawMaterialReturnToHall -> rawMaterialReturnToHall.setRawMaterialReturnToHall(rawMaterialReturnToHallModel));
		DataUtils.setAuditFields(rawMaterialReturnToHallRepository, rawMaterialReturnToHallDto.getId(), rawMaterialReturnToHallModel);
		return Optional.of(modelMapperService.convertEntityAndDto(rawMaterialReturnToHallRepository.save(rawMaterialReturnToHallModel), RawMaterialReturnToHallDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteRawMaterialReturnToHall(Long id) {
		if (!rawMaterialReturnToHallRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		rawMaterialReturnToHallRepository.deleteById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<RawMaterialReturnToHallDto> getRawMaterialReturnToHall(Long id) {
		Optional<RawMaterialReturnToHallModel> rawMaterialReturnToHallOp = rawMaterialReturnToHallRepository.findById(id);
		if (rawMaterialReturnToHallOp.isPresent()) {
			RawMaterialReturnToHallDto rawMaterialReturnToHallDto = modelMapperService.convertEntityAndDto(rawMaterialReturnToHallOp.get(), RawMaterialReturnToHallDto.class);
			return Optional.of(rawMaterialReturnToHallDto);
		} else {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<RawMaterialReturnToHallResponseDto>> getAllRawMaterialReturnToHall(FilterDto filterDto) {
		Optional<Example<RawMaterialReturnToHallModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			RawMaterialReturnToHallModel rawMaterialReturnToHallModel = RawMaterialReturnToHallModel.ofSearchingModel(query);
			rawMaterialReturnToHallModel.setHallMaster(HallMasterModel.builder().nameDefaultLang(query).namePreferLang(query).nameSupportiveLang(query).build());
			example = Optional.of(Example.of(rawMaterialReturnToHallModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		ResponseContainerDto<List<RawMaterialReturnToHallResponseDto>> rawMaterialReturnToHallResponseDto = read(RawMaterialReturnToHallResponseDto.class, RawMaterialReturnToHallModel.class, filterDto, example);
		ResponseContainerDto<List<RawMaterialReturnToHallResponseDto>> rawMaterialReturnToHallList = RequestResponseUtils.generateResponseDto(getWeightCalculation(rawMaterialReturnToHallResponseDto.getBody()));
		rawMaterialReturnToHallList.setPaging(rawMaterialReturnToHallResponseDto.getPaging());
		return rawMaterialReturnToHallList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RawMaterialReturnToHallInputTransferToHallDropDownDto> getRawMaterialReturnToHallInputTransferToHallDropDownData(Long inputTransferToHallId) {
		return rawMaterialReturnToHallNativeQueryService.getRawMaterialReturnToHallInputTransferToHallDropDownData(inputTransferToHallId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<InputTransferToHallUpcomingOrderRawMaterial> findRawMaterialByInputTransferToHallId(Long inputTransferToHallId) {
		return rawMaterialReturnToHallNativeQueryService.findRawMaterialByInputTransferToHallId(inputTransferToHallId);
	}

	/**
	 * Constructs and returns an instance of ExampleMatcher configured with specific matchers and path exclusions.
	 * The matchers allow case-insensitive partial matching for specified fields and properties, while certain audit fields are ignored in the matching process.
	 *
	 * @return An ExampleMatcher instance with customized field matchers and path exclusions.
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher
			.matchingAny()
			.withMatcher(FieldConstants.RAW_MATERIAL_RETURN_TO_HALL_RETURN_DATE_STRING, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.RAW_MATERIAL_RETURN_TO_HALL_HALL_MASTER + "." + FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.RAW_MATERIAL_RETURN_TO_HALL_HALL_MASTER + "." + FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.RAW_MATERIAL_RETURN_TO_HALL_HALL_MASTER + "." + FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG,ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName(), BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.RAW_MATERIAL_RETURN_TO_HALL_INPUT_TRANSFER_TO_HALL), BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.RAW_MATERIAL_RETURN_TO_HALL_HALL_MASTER)));
	}

	/**
	 * Calculates and updates weight-related details for a list of raw material return to hall response DTOs.
	 * The method retrieves calculation data for each DTO from the native query service and sets the corresponding weight details in the provided list of response DTOs.
	 *
	 * @param rawMaterialReturnToHallResponseDtos A list of {@link RawMaterialReturnToHallResponseDto} that requires weight-related updates
	 * @return The updated list of {@link RawMaterialReturnToHallResponseDto} with calculated weight details
	 */
	private List<RawMaterialReturnToHallResponseDto> getWeightCalculation(List<RawMaterialReturnToHallResponseDto> rawMaterialReturnToHallResponseDtos) {
		rawMaterialReturnToHallResponseDtos.forEach(rawMaterialReturnToHall -> {
			RawMaterialReturnToHallCalculationDto rawMaterialReturnToHallCalculationDto = rawMaterialReturnToHallNativeQueryService.getRawMaterialReturnToHallCalculation(rawMaterialReturnToHall.getId());
			rawMaterialReturnToHall.setInputTransferToHallId(rawMaterialReturnToHallCalculationDto.getInputTransferToHallId());
			rawMaterialReturnToHall.setWeightNameDefaultLang(rawMaterialReturnToHallCalculationDto.getNameDefaultLang());
			rawMaterialReturnToHall.setWeightNamePreferLang(rawMaterialReturnToHallCalculationDto.getNamePreferLang());
			rawMaterialReturnToHall.setWeightNameSupportiveLang(rawMaterialReturnToHallCalculationDto.getNameSupportiveLang());
		});
		return rawMaterialReturnToHallResponseDtos;
	}

}