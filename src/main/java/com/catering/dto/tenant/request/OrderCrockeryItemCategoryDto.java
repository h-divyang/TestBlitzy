package com.catering.dto.tenant.request;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCrockeryItemCategoryDto extends RawMaterialCategoryDto implements Cloneable {

	Logger logger = LoggerFactory.getLogger(OrderCrockeryItemCategoryDto.class);

	private Long functionId;

	private List<OrderCrockeryRawMaterialDto> rawMaterials;

	@Override
	public OrderCrockeryItemCategoryDto clone() {
		try {
			return (OrderCrockeryItemCategoryDto) super.clone();
		} catch (CloneNotSupportedException e) {
			logger.error(e.getMessage(), e);
		}
		return this;
	}

}