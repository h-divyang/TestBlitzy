package com.catering.specification;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import com.catering.bean.Paging;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.model.tenant.MenuItemModel;
import com.catering.model.tenant.RawMaterialModel;
import com.catering.repository.tenant.MenuItemRepository;
import com.catering.repository.tenant.RawMaterialRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.util.PagingUtils;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;

/**
 * Generic service class for handling Specification-based queries across multiple JPA entities.
 * 
 * Supports dynamic filtering, pagination, and sorting for any model class that has a corresponding
 * {@link JpaSpecificationExecutor} registered in the repositoryMap.
 * 
 * @author Krushali Talaviya
 * @since 2025-04-08
 */
@Service
public class GenericSpecificationService {

	// Holds model class to repository mappings for dynamic resolution
	private final Map<Class<?>, JpaSpecificationExecutor<?>> repositoryMap;

	@Autowired
	private ModelMapperService modelMapperService;

	/**
	 * Constructor that initializes the repository map with known model-repository pairs.
	 *
	 * @param menuItemRepository the repository for MenuItemModel
	 * @param rawMaterialRepository the repository for RawMaterialModel
	 */
	public GenericSpecificationService(MenuItemRepository menuItemRepository, RawMaterialRepository rawMaterialRepository) {
		this.repositoryMap = Map.of(MenuItemModel.class, menuItemRepository, RawMaterialModel.class, rawMaterialRepository);
	}

	/**
	 * Executes a specification-based query for the given model and converts the results to DTOs.
	 *
	 * @param dtoClass the class of the DTO to return
	 * @param modelClass the entity model class to query
	 * @param filterDto contains pagination and sorting info
	 * @param specification JPA Specification for dynamic filtering
	 * @param mapperFunction a function to convert entity to DTO
	 * @param <D> the DTO type
	 * @param <M> the model/entity type
	 * @return a response containing a list of DTOs and paging metadata
	 */
	public <D, M> ResponseContainerDto<List<D>> readWithSpecification(Class<D> dtoClass, Class<M> modelClass, FilterDto filterDto, Specification<M> specification, Function<M, D> mapperFunction) {
		// Get the appropriate repository
		JpaSpecificationExecutor<M> repository = getRepository(modelClass);

		// Create page request with sorting
		Pageable pageable = buildPageRequest(filterDto, modelClass);

		// Execute query
		Optional<Page<M>> pages = Optional.empty();
		pages = Optional.of(repository.findAll(specification, pageable));

		Optional<Paging> paging = PagingUtils.getPaging(pages.get());
		return RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(pages.get().getContent(), dtoClass)).setCustomPaging(!pages.get().getContent().isEmpty() ? paging.get() : Paging.builder().build());
	}

	/**
	 * Builds a Pageable object using page number, size, and sorting logic.
	 *
	 * @param filterDto the filter containing pagination and sort details
	 * @param modelClass the model class being queried (can be used for custom logic)
	 * @param <M> the model type
	 * @return a Pageable object
	 */
	private <M> Pageable buildPageRequest(FilterDto filterDto, Class<M> modelClass) {
		Integer page = 0;
		Integer size = 0;
		if (StringUtils.isNoneBlank(filterDto.getCurrentPage()) && StringUtils.isNoneBlank(filterDto.getSizePerPage()) && ValidationUtils.isNumber(filterDto.getCurrentPage()) && ValidationUtils.isNumber(filterDto.getSizePerPage())) {
			int pageInt = Integer.parseInt(filterDto.getCurrentPage()) - 1;
			int sizeInt = Integer.parseInt(filterDto.getSizePerPage());
			if (pageInt >= 0 && sizeInt > 0) {
				page = pageInt;
				size = sizeInt;
			}
		}
		return PageRequest.of(page, size, buildSort(filterDto, modelClass));
	}

	/**
	 * Builds a Sort object based on the sortBy and sortDirection values in filterDto.
	 * Adds custom logic for sorting by priority.
	 *
	 * @param filterDto contains sort field and direction
	 * @param modelClass the model class being queried
	 * @param <M> the model type
	 * @return a Sort object representing the sort order
	 */
	private <M> Sort buildSort(FilterDto filterDto, Class<M> modelClass) {
		if (filterDto.getSortBy() == null) {
			return Sort.unsorted();
		}

		// Add entity-specific sort logic if needed
		if ("priority".equals(filterDto.getSortBy())) {
			return Sort.by(Sort.Direction.fromString(filterDto.getSortDirection()), "priority", "id");
		}

		return Sort.by(Sort.Direction.fromString(filterDto.getSortDirection()), filterDto.getSortBy());
	}

	/**
	 * Dynamically resolves the appropriate repository from the model class.
	 *
	 * @param modelClass the entity class
	 * @param <M> the model type
	 * @return the corresponding JpaSpecificationExecutor repository
	 * @throws IllegalArgumentException if no repository is found
	 */
	@SuppressWarnings("unchecked")
	private <M> JpaSpecificationExecutor<M> getRepository(Class<M> modelClass) {
		JpaSpecificationExecutor<?> repo = repositoryMap.get(modelClass);
		if (repo == null) {
			throw new IllegalArgumentException("No repository found for: " + modelClass.getName());
		}
		return (JpaSpecificationExecutor<M>) repo;
	}

}