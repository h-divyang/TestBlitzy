package com.catering.service.common.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import com.catering.bean.Paging;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.exception.RestException;
import com.catering.model.audit.AuditIdModelOnly;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.GenericService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.util.PagingUtils;
import com.catering.util.RequestResponseUtils;

/**
 * A generic service implementation providing basic CRUD operations for entities
 * that extend the {@link IdModel} class. The service uses a JPA repository
 * ({@link JpaRepository}) to interact with the underlying database.
 *
 * @param <D> The DTO (Data Transfer Object) type used for data exchange with clients.
 * @param <M> The entity type that extends {@link IdModel}, representing the data model.
 * @param <I> The type of the identifier (ID) used for the entity.
 *
 * @author Krushali Talaviya
 * @since July 2023
 */
public class GenericServiceWithAuditIdModelImpl<D, M extends AuditIdModelOnly, I> implements GenericService<D, M, I> {

	/**
	 * Service for managing and handling message-related operations.
	 */
	@Autowired
	private MessageService messageService;

	/**
	 * Service for managing exceptions and error handling.
	 */
	@Autowired
	private ExceptionService exceptionService;

	/**
	 * Service for mapping objects between different models.
	 */
	@Autowired
	private ModelMapperService modelMapperService;

	/**
	 * Repository for performing CRUD operations on entities.
	 */
	@Autowired
	private JpaRepository<M, I> repository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public D createAndUpdate(D dto, Class<D> dtoType, Class<M> modelType, I id) throws RestException {
		M model = modelMapperService.convertEntityAndDto(dto, modelType);
		return modelMapperService.convertEntityAndDto(repository.save(model), dtoType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<D>> read(Class<D> dtoType, Class<M> modelType, FilterDto filterDto, Optional<Example<M>> example) {
		Optional<PageRequest> pageRequest = PagingUtils.pageRequestOf(filterDto.getCurrentPage(), filterDto.getSizePerPage());
		Optional<Sort> sort = PagingUtils.sortOf(filterDto.getSortDirection(), filterDto.getSortBy(), modelType);
		Optional<Page<M>> pages = Optional.empty();
		List<M> list = null;
		if (sort.isPresent() && pageRequest.isPresent()) {
			pageRequest = Optional.of(pageRequest.get().withSort(sort.get()));
		}
		if (pageRequest.isPresent() && example.isPresent()) {
			pages = Optional.of(repository.findAll(example.get(), pageRequest.get()));
		} else if (pageRequest.isPresent()) {
			pages =  Optional.of(repository.findAll(pageRequest.get()));
		} else if (example.isPresent()) {
			list = repository.findAll(example.get());
		} else if (sort.isPresent()) {
			list = repository.findAll(sort.get());
		}
		if (pages.isEmpty() && Objects.isNull(list)) {
			list = repository.findAll();
		}
		if (Objects.nonNull(list)) {
			return RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(list, dtoType));
		}
		Optional<Paging> paging = PagingUtils.getPaging(pages.get());
		return RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(pages.get().getContent(), dtoType)).setCustomPaging(!pages.get().getContent().isEmpty() ? paging.get() : Paging.builder().build());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<D> read(I id, Class<D> dtoType) throws RestException {
		if (Objects.isNull(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		Optional<M> model = repository.findById(id);
		if (model.isEmpty()) {
			exceptionService.throwRestException(HttpStatus.NOT_FOUND, messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		return Optional.of(modelMapperService.convertEntityAndDto(model.get(), dtoType));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(I id) throws RestException {
		if (!repository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		repository.deleteById(id);
	}

}