package com.catering.service.tenant.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.catering.bean.Paging;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.MenuItemRawMaterialDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.MenuItemRawMaterialModel;
import com.catering.model.tenant.MenuItemModel;
import com.catering.model.tenant.RawMaterialModel;
import com.catering.model.tenant.MeasurementModel;
import com.catering.repository.tenant.MenuItemRawMaterialRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.MenuItemRawMaterialService;
import com.catering.service.tenant.MenuItemService;
import com.catering.service.tenant.RawMaterialService;
import com.catering.service.tenant.MeasurementService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.RequestResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for managing and handling operations related to MenuItemRawMaterial entities.
 * This class provides functionalities to create, update, delete, and retrieve MenuItemRawMaterial data
 * while ensuring associated dependencies such as RawMaterial, MenuItem, and Measurement are validated.
 * It uses a combination of repositories and various utility services to enable mapping, validation,
 * exception handling, and data transformation.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuItemRawMaterialServiceImpl extends GenericServiceImpl<MenuItemRawMaterialDto, MenuItemRawMaterialModel, Long> implements MenuItemRawMaterialService {

	/**
	 * Service for converting between DTOs and entity models.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for managing the association between menu items and raw materials.
	 */
	MenuItemRawMaterialRepository menuItemRawMaterialRepository;

	/**
	 * Service for managing raw materials.
	 */
	RawMaterialService rawMaterialService;

	/**
	 * Service for managing menu items.
	 */
	MenuItemService menuItemService;

	/**
	 * Service for managing measurements.
	 */
	MeasurementService measurementService;

	/**
	 * Service for retrieving and managing localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MenuItemRawMaterialDto> createAndUpdate(MenuItemRawMaterialDto menuItemRawMaterialDto) throws RestException {
		rawMaterialService.existByIdOrThrow(menuItemRawMaterialDto.getRawMaterial().getId());
		menuItemService.existByIdOrThrow(menuItemRawMaterialDto.getMenuItem().getId());
		measurementService.existByIdOrThrow(menuItemRawMaterialDto.getMeasurement().getId());
		MenuItemRawMaterialModel menuItemRawMaterialModel = modelMapperService.convertEntityAndDto(menuItemRawMaterialDto, MenuItemRawMaterialModel.class);
		DataUtils.setAuditFields(menuItemRawMaterialRepository, menuItemRawMaterialDto.getId(), menuItemRawMaterialModel);
		return Optional.of(modelMapperService.convertEntityAndDto(menuItemRawMaterialRepository.save(menuItemRawMaterialModel), MenuItemRawMaterialDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MenuItemRawMaterialDto> createCopyRecipeList(List<MenuItemRawMaterialDto> menuItemRawMaterialDtos) throws RestException {
		menuItemRawMaterialDtos.forEach(menuItemRawMaterial -> {
			rawMaterialService.existByIdOrThrow(menuItemRawMaterial.getRawMaterial().getId());
			menuItemService.existByIdOrThrow(menuItemRawMaterial.getMenuItem().getId());
			measurementService.existByIdOrThrow(menuItemRawMaterial.getMeasurement().getId());
		});
		List<MenuItemRawMaterialModel> menuItemRawMaterialModels = modelMapperService.convertListEntityAndListDto(menuItemRawMaterialDtos, MenuItemRawMaterialModel.class);
		menuItemRawMaterialModels.forEach(menuItemRawMaterialModel -> {
			DataUtils.setAuditFields(menuItemRawMaterialRepository, menuItemRawMaterialModel.getId(), menuItemRawMaterialModel);
		});
		return modelMapperService.convertListEntityAndListDto(menuItemRawMaterialRepository.saveAll(menuItemRawMaterialModels), MenuItemRawMaterialDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<MenuItemRawMaterialDto>> read(Long menuItemId, String page, String size, String sortBy, String sortDirection, String query) {
		Optional<Example<MenuItemRawMaterialModel>> example = Optional.empty();
		if (StringUtils.isNotBlank(query)) {
			MenuItemRawMaterialModel menuItemRawMaterialModel = MenuItemRawMaterialModel.ofSearchingModel(query);
			menuItemRawMaterialModel.setRawMaterial(RawMaterialModel.ofSearchingModel(query));
			menuItemRawMaterialModel.setMeasurement(MeasurementModel.ofSearchingModel(query));
			example = Optional.of(Example.of(menuItemRawMaterialModel, getExampleMatcher()));
		}
		Optional<PageRequest> pageRequest = PagingUtils.pageRequestOf(page, size);
		Optional<Sort> sort = PagingUtils.sortOf(sortDirection, sortBy, MenuItemRawMaterialDto.class);
		Optional<Page<MenuItemRawMaterialModel>> pages = Optional.empty();
		List<MenuItemRawMaterialModel> list = null;
		if (sort.isPresent() && pageRequest.isPresent()) {
			pageRequest = Optional.of(pageRequest.get().withSort(sort.get()));
		}
		if (pageRequest.isPresent() && example.isPresent()) {
			pages = Optional.of(menuItemRawMaterialRepository.findAll(getSpecFromDatesAndExample(menuItemId, example.get()), pageRequest.get()));
		} else if (pageRequest.isPresent()) {
			pages =  Optional.of(menuItemRawMaterialRepository.findByMenuItem_Id(menuItemId, pageRequest.get()));
		}
		if (pages.isEmpty() && Objects.isNull(list)) {
			list = menuItemRawMaterialRepository.findByMenuItem_Id(menuItemId);
		}
		if (Objects.nonNull(list)) {
			return RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(list, MenuItemRawMaterialDto.class));
		}
		Optional<Paging> paging = PagingUtils.getPaging(pages.get());
		return RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(pages.get().getContent(), MenuItemRawMaterialDto.class)).setCustomPaging(!pages.get().getContent().isEmpty() ? paging.get() : Paging.builder().build());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) {
		if (!menuItemRawMaterialRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		try {
			menuItemRawMaterialRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MenuItemRawMaterialModel> findByMenuItemId(Long id) {
		return menuItemRawMaterialRepository.findByMenuItem_Id(id);
	}

	/**
	 * Constructs and returns an ExampleMatcher instance with custom matching rules and ignored paths.
	 * The matcher is configured to match specific fields with "contains" logic (case-insensitive) and
	 * ignore certain paths like audit-related fields and other specified attributes.
	 *
	 * @return An ExampleMatcher configured for use with menu item raw materials to define matching logic and ignored fields.
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher
				.matchingAny()
				.withMatcher(FieldConstants.MENU_ITEM_RAW_MATERIAL_FIELD_WEIGHT, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.RAW_MATERIAL + "." + FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.RAW_MATERIAL + "." + FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.RAW_MATERIAL + "." + FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.MEASUREMENT + "." + FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.MEASUREMENT + "." + FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.MEASUREMENT + "." + FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName(), BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.RAW_MATERIAL),
						BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.MENU_ITEM),
						BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.MEASUREMENT),
						ArrayUtils.buildArray(FieldConstants.MEASUREMENT +"."+ FieldConstants.MEASUREMENT_SYMBOL_DEFAULT_LANG, 
								FieldConstants.MEASUREMENT +"."+ FieldConstants.MEASUREMENT_SYMBOL_PREFER_LANG,
								FieldConstants.MEASUREMENT +"."+ FieldConstants.MEASUREMENT_SYMBOL_SUPPORTIVE_LANG,
								FieldConstants.RAW_MATERIAL +"."+ FieldConstants.IS_ACTIVE,
								FieldConstants.RAW_MATERIAL +"."+ FieldConstants.EDIT_COUNT
				)));
	}

	/**
	 * Constructs a specification for querying MenuItemRawMaterialModel entities based on the given raw material ID and example criteria.
	 * The specification includes predicates for filtering by a specific raw material ID and matching entity attributes using the provided example.
	 *
	 * @param rawMaterialId The ID of the raw material to filter by. If null, no filtering by raw material ID will be applied.
	 * @param example An Example object containing criteria for matching properties of MenuItemRawMaterialModel entities.
	 * @return A Specification object to be used for querying MenuItemRawMaterialModel entities.
	 */
	private Specification<MenuItemRawMaterialModel> getSpecFromDatesAndExample(Long rawMaterialId, Example<MenuItemRawMaterialModel> example) {
		return (Specification<MenuItemRawMaterialModel>) (root, query, builder) -> {
			final List<Predicate> predicates = new ArrayList<>();
			Join<MenuItemModel, MenuItemRawMaterialModel> menuItem = root.join("menuItem");
			if (rawMaterialId != null) {
				predicates.add(builder.equal(menuItem.get("id"), rawMaterialId));
			}
			predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, example));
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

}