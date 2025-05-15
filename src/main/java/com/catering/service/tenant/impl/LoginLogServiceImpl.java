package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.LoginLogDto;
import com.catering.model.tenant.LoginLogModel;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.LoginLogService;
import com.catering.util.PagingUtils;

/**
 * Implementation of the {@link LoginLogService} interface for handling login log-related operations.
 * This class extends the {@link GenericServiceImpl} to inherit common CRUD operations while providing
 * additional implementation specific to login logs.
 *
 * The primary responsibilities include retrieving login log information based on user ID and applying
 * filtering criteria such as sorting and pagination.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 */
@Service
public class LoginLogServiceImpl extends GenericServiceImpl<LoginLogDto, LoginLogModel, Long> implements LoginLogService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<LoginLogDto>> read(Long id, FilterDto filterDto) {
		LoginLogModel loginLogModel = LoginLogModel.builder().build();
		loginLogModel.setUserId(id);
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		return read(LoginLogDto.class, LoginLogModel.class, filterDto, Optional.empty());
	}

}