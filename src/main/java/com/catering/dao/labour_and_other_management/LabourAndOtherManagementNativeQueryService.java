package com.catering.dao.labour_and_other_management;

import java.util.List;

import com.catering.dto.tenant.request.CommonDataForDropDownDto;
import com.catering.dto.tenant.request.GetOrderCrockeryDto;
import com.catering.dto.tenant.request.OrderCrockeryItemCategoryDto;
import com.catering.dto.tenant.request.OrderCrockeryParameterDto;
import com.catering.dto.tenant.request.OrderGeneralFixRawMaterialResponseContainerDto;

public interface LabourAndOtherManagementNativeQueryService {

	List<OrderCrockeryItemCategoryDto> findCrockeryDataByOrderId(OrderCrockeryParameterDto parameterDto);

	List<GetOrderCrockeryDto> findCrockeryDataByOrderFunctionAndRawMaterialCategoryId(Long orderFunctionId, Long rawMaterialCategoryId);

	List<CommonDataForDropDownDto> findCrockeryRawMaterialCategoryByOrderFunctionId(Long orderFunctionId);

	List<CommonDataForDropDownDto> findCrockerySupplierContactByOrderFunctionId(Long orderFunctionId);

	Double calculateItemCost(Long functionId, Long menuItemId);

	List<OrderGeneralFixRawMaterialResponseContainerDto> findOrderGeneralFixRawMaterial(Long[] functionIds);

	List<GetOrderCrockeryDto> findGeneralFixRawMaterialByOrderFunctionId(Long orderFunctionId);

	List<CommonDataForDropDownDto> findGeneralFixRawMaterialSupplierContactByOrderFunctionId(Long orderFunctionId);

}