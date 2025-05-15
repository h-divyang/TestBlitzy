package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.RawMaterialCategoryTypeModel;

public interface RawMaterialCategoryTypeRepository  extends JpaRepository<RawMaterialCategoryTypeModel, Long> {
}