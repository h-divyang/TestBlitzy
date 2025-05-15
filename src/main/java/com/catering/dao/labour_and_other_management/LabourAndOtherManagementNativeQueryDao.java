package com.catering.dao.labour_and_other_management;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.CommonDataForDropDownDto;
import com.catering.dto.tenant.request.GetOrderCrockeryDto;
import com.catering.dto.tenant.request.OrderCrockeryRawMaterialDto;
import com.catering.dto.tenant.request.OrderGeneralFixRawMaterialResponseDto;

public interface LabourAndOtherManagementNativeQueryDao extends JpaRepository<LabourAndOtherManagementNativeQuery, Long> {

	@Query(name = "findCrockeryDataByOrderIdAndItemCategoryId", nativeQuery = true)
	List<OrderCrockeryRawMaterialDto> findCrockeryDataByOrderIdAndItemCategoryId(Long orderId, Long rawMaterialCategoryId, Long orderFunctionId, Boolean isAdjustQuantity);

	@Query(name = "calculateItemCost", nativeQuery = true)
	Double calculateItemCost(Long functionId, Long menuItemId, Boolean isAdjustQuantity);

	@Query(name = "findOrderGeneralFixRawMaterialByFunctionId", nativeQuery = true)
	List<OrderGeneralFixRawMaterialResponseDto> findOrderGeneralFixRawMaterialByFunctionId(Long orderFunctionId, Boolean isAdjustQuantity);

	@Query(name = "findCrockeryDataByOrderFunctionAndRawMaterialCategoryId", nativeQuery = true)
	List<GetOrderCrockeryDto> findCrockeryDataByOrderFunctionAndRawMaterialCategoryId(Boolean isAdjustQuantity, Long orderFunctionId, Long rawMaterialCategoryId);

	@Query(name = "findCrockeryRawMaterialCategoryByOrderFunctionId", nativeQuery = true)
	List<CommonDataForDropDownDto> findCrockeryRawMaterialCategoryByOrderFunctionId(Long orderFunctionId);

	@Query(name = "findCrockerySupplierContactByOrderFunctionId", nativeQuery = true)
	List<CommonDataForDropDownDto> findCrockerySupplierContactByOrderFunctionId(Long orderFunctionId);

	@Query(name = "findGeneralFixRawMaterialByOrderFunctionId", nativeQuery = true)
	List<GetOrderCrockeryDto> findGeneralFixRawMaterialByOrderFunctionId(Boolean isAdjustQuantity, Long orderFunctionId);

	@Query(name = "findGeneralFixRawMaterialSupplierContactByOrderFunctionId", nativeQuery = true)
	List<CommonDataForDropDownDto> findGeneralFixRawMaterialSupplierContactByOrderFunctionId(Long orderFunctionId);

}