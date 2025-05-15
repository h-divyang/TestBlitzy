package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.HallMasterDto;
import com.catering.model.tenant.HallMasterModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing Hall Master entities and related operations.
 * Extends the {@code GenericService} interface, providing specific methods
 * for managing Hall Master data and its associated images.
 * 
 * @author Rohan Parmar
 * @since February 2024
 */
public interface HallMasterService extends GenericService<HallMasterDto, HallMasterModel, Long> {

	/**
	* Inserts or updates Hall Master data using the provided {@code hallMasterDto}.
	* Additionally, creates or updates the image associated with the Hall Master.
	* 
	* @param hallMasterDto The HallMasterDto containing the data to be inserted or updated.
	* @param image The MultipartFile representing the image to be associated with the Hall Master.
	* @return An Optional containing the HallMasterDto after insertion or update, if successful.
	*/
	Optional<HallMasterDto> save(HallMasterDto hallMasterDto, MultipartFile image);

	/**
	 * Reads paginated data from the provided {@code filterDto} using GenericService.
	 * Additionally, sets the image path in the return data.
	 * 
	 * @param filterDto The FilterDto containing the filter criteria for retrieving the data.
	 * @return A ResponseContainerDto containing a list of HallMasterDto objects with the image path set, if successful.
	 */
	ResponseContainerDto<List<HallMasterDto>> read(FilterDto filterDto);

	/**
	 * Deletes data with the provided {@code id}.<br/>
	 *
	 * If {@code isImage} is true, the associated image will be removed from the AWS S3 bucket.
	 * If {@code isImage} is false, this method checks if data exists with the given {@code id}, 
	 * and if the data is not used in another table, then it deletes the record along with the image.
	 * 
	 * @param id The ID of the data to be deleted.
	 * @param isImage A boolean flag indicating whether to delete the associated image from the AWS S3 bucket.
	 */
	void deleteById(Long id, Boolean isImage);

	/**
	 * Updates the status of the record with the given {@code id} to the specified {@code status}.
	 * If the data with the provided {@code id} does not exist, a RestException is thrown.
	 *
	 * @param id The ID of the record to update.
	 * @param status The new status to set for the record.
	 * @return An Optional containing the updated HallMasterDto if the operation is successful.
	 */
	Optional<HallMasterDto> updateStatus(Long id, Boolean status);

	/**
	 * Retrieves a list of all HallMasterDto objects.
	 *
	 * @return A list of HallMasterDto representing all Hall Master entities.
	 */
	List<HallMasterDto> getAllHallMaster();

}