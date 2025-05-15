package com.catering.dao.labour_and_other_management;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catering.constant.IdConstant;
import com.catering.dto.tenant.request.CommonDataForDropDownDto;
import com.catering.dto.tenant.request.GetOrderCrockeryDto;
import com.catering.dto.tenant.request.OrderCrockeryItemCategoryDto;
import com.catering.dto.tenant.request.OrderCrockeryParameterDto;
import com.catering.dto.tenant.request.OrderCrockeryRawMaterialDto;
import com.catering.dto.tenant.request.OrderGeneralFixRawMaterialResponseContainerDto;
import com.catering.dto.tenant.request.RawMaterialCategoryDto;
import com.catering.repository.tenant.BookOrderRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.RawMaterialCategoryService;

@Service
public class LabourAndOtherManagementNativeQueryServiceImpl implements LabourAndOtherManagementNativeQueryService {

	@Autowired
	private RawMaterialCategoryService rawMaterialCategoryService;

	@Autowired
	private ModelMapperService modelMapperService;

	@Autowired
	private LabourAndOtherManagementNativeQueryDao labourAndOtherManagementNativeQueryDao;

	@Autowired
	private BookOrderRepository bookOrderRepository;

	@Override
	public List<OrderCrockeryItemCategoryDto> findCrockeryDataByOrderId(OrderCrockeryParameterDto parameterDto) {
		List<OrderCrockeryItemCategoryDto> list = new ArrayList<>();
		List<RawMaterialCategoryDto> itemCategories = rawMaterialCategoryService.readByCategoryTypeId(List.of(IdConstant.RAW_MATERIAL_CATEGORY_TYPE_LIST_OF_UTENSILS_ID, IdConstant.RAW_MATERIAL_CATEGORY_TYPE_KITCHEN_CROCKERY_CATEGORY_ID));
		List<OrderCrockeryItemCategoryDto> itemCategoriesCrokeries = modelMapperService.convertListEntityAndListDto(itemCategories, OrderCrockeryItemCategoryDto.class);
		if (Objects.nonNull(parameterDto.getOrderFunction())) {
			for (Long orderFunctionId : parameterDto.getOrderFunction()) {
				itemCategoriesCrokeries.forEach(rawMaterialCategoryCrockery -> {
					OrderCrockeryItemCategoryDto clonedItemCategory = rawMaterialCategoryCrockery.clone();
					List<OrderCrockeryRawMaterialDto> orderCrockeryRawMaterialDtos = labourAndOtherManagementNativeQueryDao.findCrockeryDataByOrderIdAndItemCategoryId(parameterDto.getBookOrderId(), rawMaterialCategoryCrockery.getId(), orderFunctionId, bookOrderRepository.getAdjustQuantityOfOrderByFunctionId(orderFunctionId));
					clonedItemCategory.setRawMaterials(orderCrockeryRawMaterialDtos);
					if (!orderCrockeryRawMaterialDtos.isEmpty()) {
						clonedItemCategory.setFunctionId(orderCrockeryRawMaterialDtos.get(0).getFunctionId());
					}
					list.add(clonedItemCategory);
				});
			}
		}
		return list;
	}

	@Override
	public Double calculateItemCost(Long functionId, Long menuItemId) {
		return labourAndOtherManagementNativeQueryDao.calculateItemCost(functionId, menuItemId, bookOrderRepository.getAdjustQuantityOfOrderByFunctionId(functionId));
	}

	@Override
	public List<OrderGeneralFixRawMaterialResponseContainerDto> findOrderGeneralFixRawMaterial(Long[] functionIds) {
		List<OrderGeneralFixRawMaterialResponseContainerDto> orderGeneralFixRawMaterialResponseDtos = new ArrayList<>();
		for (Long functionId : functionIds) {
			orderGeneralFixRawMaterialResponseDtos.add(OrderGeneralFixRawMaterialResponseContainerDto.builder().functionId(functionId).orderGeneralFixRawMaterial(labourAndOtherManagementNativeQueryDao.findOrderGeneralFixRawMaterialByFunctionId(functionId, bookOrderRepository.getAdjustQuantityOfOrderByFunctionId(functionId))).build());
		}
		return orderGeneralFixRawMaterialResponseDtos;
	}

	@Override
	public List<GetOrderCrockeryDto> findCrockeryDataByOrderFunctionAndRawMaterialCategoryId(Long orderFunctionId, Long rawMaterialCategoryId) {
		return labourAndOtherManagementNativeQueryDao.findCrockeryDataByOrderFunctionAndRawMaterialCategoryId(bookOrderRepository.getAdjustQuantityOfOrderByFunctionId(orderFunctionId), orderFunctionId, rawMaterialCategoryId);
	}

	@Override
	public List<CommonDataForDropDownDto> findCrockeryRawMaterialCategoryByOrderFunctionId(Long orderFunctionId) {
		return labourAndOtherManagementNativeQueryDao.findCrockeryRawMaterialCategoryByOrderFunctionId(orderFunctionId);
	}

	@Override
	public List<CommonDataForDropDownDto> findCrockerySupplierContactByOrderFunctionId(Long orderFunctionId) {
		return labourAndOtherManagementNativeQueryDao.findCrockerySupplierContactByOrderFunctionId(orderFunctionId);
	}

	@Override
	public List<GetOrderCrockeryDto> findGeneralFixRawMaterialByOrderFunctionId(Long orderFunctionId) {
		return labourAndOtherManagementNativeQueryDao.findGeneralFixRawMaterialByOrderFunctionId(bookOrderRepository.getAdjustQuantityOfOrderByFunctionId(orderFunctionId), orderFunctionId);
	}

	@Override
	public List<CommonDataForDropDownDto> findGeneralFixRawMaterialSupplierContactByOrderFunctionId(Long orderFunctionId) {
		return labourAndOtherManagementNativeQueryDao.findGeneralFixRawMaterialSupplierContactByOrderFunctionId(orderFunctionId);
	}

}