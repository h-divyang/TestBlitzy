package com.catering.service.tenant.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.catering.constant.Constants;
import com.catering.dao.labour_and_other_management.LabourAndOtherManagementNativeQueryDao;
import com.catering.dao.labour_and_other_management.LabourAndOtherManagementNativeQueryService;
import com.catering.dto.tenant.request.CompanySettingDto;
import com.catering.dto.tenant.request.OrderCrockeryDto;
import com.catering.dto.tenant.request.OrderCrockeryItemCategoryDto;
import com.catering.dto.tenant.request.OrderCrockeryParameterDto;
import com.catering.dto.tenant.request.OrderGeneralFixRawMaterialDto;
import com.catering.dto.tenant.request.OrderGeneralFixRawMaterialResponseContainerDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.CompanySettingModel;
import com.catering.model.tenant.SaveMenuPreparationModel;
import com.catering.repository.tenant.BookOrderRepository;
import com.catering.repository.tenant.CompanySettingRepository;
import com.catering.repository.tenant.SaveMenuPreparationOrderFunctionRepository;
import com.catering.repository.tenant.SaveMenuPreparationRepository;
import com.catering.service.common.FileService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.S3Service;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.CompanySettingService;
import com.catering.service.tenant.OrderCrockeryService;
import com.catering.service.tenant.OrderGeneralFixRawMaterialService;
import com.catering.util.DataUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for managing company settings, extending generic service functionalities.
 * This class provides methods to retrieve, create, update, and handle related business logic pertaining to company settings.
 *
 * It collabourates with repositories, external services, and utilities to process and persist company setting data along
 * with associated operations, such as managing general order-level fixed raw materials and crockery adjustments.
 * 
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @FieldDefaults: Configures field access level as private.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanySettingServiceImpl extends GenericServiceImpl<CompanySettingDto, CompanySettingModel, Long> implements CompanySettingService {

	/**
	 * Repository for managing Company Setting entity interactions with the database.
	 */
	@Autowired
	CompanySettingRepository companySettingRepository;

	/**
	 * Service providing functionalities for mapping objects using ModelMapper.
	 */
	@Autowired
	ModelMapperService modelMapperService;

	/**
	 * Repository for handling operations related to saving menu preparations.
	 */
	@Autowired
	SaveMenuPreparationRepository saveMenuPreparationRepository;

	/**
	 * DAO for executing native queries related to labour and other management.
	 */
	@Autowired
	LabourAndOtherManagementNativeQueryDao labourAndOtherManagementNativeQueryDao;

	/**
	 * Service for executing native query operations related to labour and other management.
	 */
	@Autowired
	LabourAndOtherManagementNativeQueryService labourAndOtherManagementNativeQueryService;

	/**
	 * Service for managing operations related to order crockery.
	 */
	@Autowired
	OrderCrockeryService orderCrockeryService;

	/**
	 * Service for managing operations related to general fixed raw materials for orders.
	 */
	@Autowired
	OrderGeneralFixRawMaterialService orderGeneralFixRawMaterialService;

	/**
	 * Repository for managing save menu preparation order functions.
	 */
	@Autowired
	SaveMenuPreparationOrderFunctionRepository saveMenuPreparationOrderFunctionRepository;

	/**
	 * A service responsible for handling file operations such as uploading files, retrieving file information,
	 * constructing file paths, and generating URLs for files.
	 */
	@Autowired
	private FileService fileService;

	/**
	 * Handles operations related to Amazon S3 storage services.
	 */
	@Autowired
	private S3Service s3Service;

	@Autowired
	BookOrderRepository bookOrderRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompanySettingDto getCompannySetting(Boolean isBackgroundImageRequired) {
		CompanySettingModel companySettingModel = companySettingRepository.findById(1l).orElse(null);
		if (Objects.nonNull(companySettingModel)) {
				CompanySettingDto companySettingDto = modelMapperService.convertEntityAndDto(companySettingModel, CompanySettingDto.class);
				if(isBackgroundImageRequired) {
					companySettingDto.setBackgroundImage(fileService.getUrl(Constants.GENERAL_REPORT_BACKGROUND));
				}
				return companySettingDto;
			}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompanySettingDto createAndUpdate(CompanySettingDto companySettingDto, MultipartFile backGroundImage) throws RestException {
		CompanySettingModel companySettingModel = modelMapperService.convertEntityAndDto(companySettingDto, CompanySettingModel.class);
		if (Objects.nonNull(backGroundImage)) {
			fileService.upload(backGroundImage, fileService.createKey(Constants.GENERAL_REPORT_BACKGROUND));
		}
		DataUtils.setAuditFields(companySettingRepository, companySettingDto.getId(), companySettingModel);
		CompanySettingDto existCompanySettingDto = getCompannySetting(false);
		Boolean existingDisplayCrockeryAndGeneralFix = existCompanySettingDto.getDisplayCrockeryAndGeneralFix();
		if (Boolean.FALSE.equals(existCompanySettingDto.getIsMobileNumberUnique())) {
			companySettingModel.setIsMobileNumberUnique(Boolean.FALSE);
		}
		if (Boolean.FALSE.equals(existCompanySettingDto.getIsMenuItemUnique())) {
			companySettingModel.setIsMenuItemUnique(Boolean.FALSE);
		}
		CompanySettingDto savedCompanySettingDto = modelMapperService.convertEntityAndDto(companySettingRepository.save(companySettingModel), CompanySettingDto.class);
		Boolean newDisplayCrockeryAndGeneralFix = savedCompanySettingDto.getDisplayCrockeryAndGeneralFix();
		if (!Objects.equals(existingDisplayCrockeryAndGeneralFix, newDisplayCrockeryAndGeneralFix)) {
			new Thread(() ->
				setOrderGeneralFixRawMaterialAndCrockeryData(newDisplayCrockeryAndGeneralFix, savedCompanySettingDto)
			).start();
		}
		savedCompanySettingDto.setBackgroundImage(fileService.getUrl(Constants.GENERAL_REPORT_BACKGROUND));
		return savedCompanySettingDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getPaginationSize() {
		return companySettingRepository.getPaginationSize();
	}

	@Override
	public void deleteBackgroundImage() {
		s3Service.delete(fileService.createKey(Constants.GENERAL_REPORT_BACKGROUND));
	}

	/**
	 * Updates and manages general fixed raw material and crockery data for orders based on specified configuration settings.
	 *
	 * @param newDisplayCrockeryAndGeneralFix Determines whether crockery and general fixed raw material data should be displayed
	 *										  or modified. If true, existing data is deleted; otherwise, additional processing occurs.
	 * @param savedCompanySettingDto The company settings to be used in adjustments, including options like
	 *								 quantity adjustment and other configurations.
	 */
	private void setOrderGeneralFixRawMaterialAndCrockeryData(Boolean newDisplayCrockeryAndGeneralFix, CompanySettingDto savedCompanySettingDto) {
		List<SaveMenuPreparationModel> saveMenuPreparationModels = saveMenuPreparationRepository.findAll();
		Set<Long> maxPersonOrderFunctionIds = new HashSet<>(saveMenuPreparationOrderFunctionRepository.retrieveMaxPersonOrderFunctionIds());
		if (newDisplayCrockeryAndGeneralFix) {
			List<Long> orderFunctionId = new ArrayList<>(maxPersonOrderFunctionIds);
			orderCrockeryService.deleteCrockeryData(orderFunctionId);
			orderGeneralFixRawMaterialService.deleteUnusedGeneralFixRawMaterial(orderFunctionId);
		} else {
			Map<Long, List<Long>> orderFunctionMap = new HashMap<>();
			List<Long> filteredOrderFunctionIds = new ArrayList<>();
			saveMenuPreparationModels.forEach(model -> {
				Long orderFunctionId = model.getOrderFunction().getId();
				Long orderId = model.getOrderFunction().getOrderId();
				if (!maxPersonOrderFunctionIds.contains(orderFunctionId)) {
					filteredOrderFunctionIds.add(orderFunctionId);
				}
				if (!maxPersonOrderFunctionIds.contains(orderFunctionId)) {
					orderFunctionMap.computeIfAbsent(orderId, k -> new ArrayList<>()).add(orderFunctionId);
				}
			});
			List<OrderGeneralFixRawMaterialResponseContainerDto> orderGeneralFixRawMaterialResponseDtosList = new ArrayList<>();
			List<OrderGeneralFixRawMaterialDto> orderGeneralFixRawMaterialDtos = new ArrayList<>();
			if (!filteredOrderFunctionIds.isEmpty()) {
				saveMenuPreparationModels.forEach(saveMenuPreparationModel -> {
					if (filteredOrderFunctionIds.contains(saveMenuPreparationModel.getOrderFunction().getId())) {
						orderGeneralFixRawMaterialResponseDtosList.add(OrderGeneralFixRawMaterialResponseContainerDto.builder().functionId(saveMenuPreparationModel.getOrderFunction().getId()).orderGeneralFixRawMaterial(labourAndOtherManagementNativeQueryDao.findOrderGeneralFixRawMaterialByFunctionId(saveMenuPreparationModel.getOrderFunction().getId(), bookOrderRepository.getAdjustQuantityOfOrderByFunctionId(saveMenuPreparationModel.getOrderFunction().getId()))).build());
					}
				});
			}
			if (!orderGeneralFixRawMaterialResponseDtosList.isEmpty()) {
				orderGeneralFixRawMaterialResponseDtosList.stream().map(OrderGeneralFixRawMaterialResponseContainerDto::getOrderGeneralFixRawMaterial).flatMap(List::stream).forEach(orderGeneralFixKariyanu -> {
					OrderGeneralFixRawMaterialDto orderGeneralFixKariyanuDto =  modelMapperService.convertEntityAndDto(orderGeneralFixKariyanu, OrderGeneralFixRawMaterialDto.class);
					orderGeneralFixKariyanuDto.setOrderTime(orderGeneralFixKariyanu.getFunctionDate());
					orderGeneralFixRawMaterialDtos.add(orderGeneralFixKariyanuDto);
				});
				orderGeneralFixRawMaterialService.saveOrderGeneralFixRawMaterial(orderGeneralFixRawMaterialDtos);
			}
			for (Map.Entry<Long, List<Long>> entry : orderFunctionMap.entrySet()) {
				Long orderId = entry.getKey();
				List<Long> functionIds = entry.getValue();
				Long[] orderFunctionIdArray = functionIds.toArray(new Long[0]);
				if (orderFunctionIdArray.length > 0) {
					OrderCrockeryParameterDto orderCrockeryParameterDto = new OrderCrockeryParameterDto(); 
					orderCrockeryParameterDto.setBookOrderId(orderId);
					orderCrockeryParameterDto.setOrderFunction(orderFunctionIdArray);
					List<OrderCrockeryItemCategoryDto> orderCrockeryItemCategoryDtos = labourAndOtherManagementNativeQueryService.findCrockeryDataByOrderId(orderCrockeryParameterDto);
					List<OrderCrockeryDto> orderCrockeries = new ArrayList<>();
					orderCrockeryItemCategoryDtos.forEach(orderCrockeryItemCategoryDto -> {
						orderCrockeryItemCategoryDto.getRawMaterials().forEach(rawMaterial -> {
							// Create a new OrderCrockeryDto and set its fields
							OrderCrockeryDto orderCrockeryDto = new OrderCrockeryDto();
							orderCrockeryDto.setRawMaterialId(rawMaterial.getId());
							orderCrockeryDto.setOrderFunction(rawMaterial.getFunctionId());
							orderCrockeryDto.setQty(rawMaterial.getQty());
							orderCrockeryDto.setMeasurementId(rawMaterial.getMeasurementId());
							orderCrockeryDto.setOrderTime(rawMaterial.getFunctionDate());
							orderCrockeryDto.setAgency(rawMaterial.getAgency());
							orderCrockeryDto.setPrice(Objects.nonNull(rawMaterial.getQty()) && Objects.nonNull(rawMaterial.getSupplierRate()) ? rawMaterial.getQty() * rawMaterial.getSupplierRate() : 0);
							orderCrockeries.add(orderCrockeryDto);
						});
					});
					orderCrockeryService.saveCrockeryData(orderCrockeries);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateIsMenuPreparationToggleEnabledAndIsMenuItemPrice(Boolean isMenuPreparationToggleEnabled, Boolean isMenuItemPriceVisible) {
		companySettingRepository.updateIsMenuPreparationToggleEnabledAndIsMenuItemPrice(isMenuPreparationToggleEnabled, isMenuItemPriceVisible);
	}

}