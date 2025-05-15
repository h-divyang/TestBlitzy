package com.catering.service.tenant;

import java.util.List;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.LoginLogDto;
import com.catering.model.tenant.LoginLogModel;
import com.catering.service.common.GenericService;

/**
 * The LoginLogService interface defines operations related to managing and retrieving login logs.
 * It extends the GenericService interface, inheriting common CRUD operations and providing additional
 * specific methods for handling login logs.
 *
 * This service is responsible for interacting with LoginLogModel entities and their corresponding
 * LoginLogDto representations. It supports the retrieval of login log details for a specific user
 * based on various filter and sorting criteria.
 */
public interface LoginLogService extends GenericService<LoginLogDto, LoginLogModel, Long> {

	/**
	 * Reads login logs based on the given user ID and filter criteria, and returns the result as a standard response container.
	 *
	 * @param id The unique identifier of the user for whom the login logs are to be retrieved.
	 * @param filterDto The filter criteria for retrieving login logs, such as pagination and sorting options.
	 * @return A ResponseContainerDto containing a list of LoginLogDto objects and associated metadata.
	 */
	ResponseContainerDto<List<LoginLogDto>> read(Long id, FilterDto filterDto);

}