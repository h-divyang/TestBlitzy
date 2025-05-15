package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.SaveMenuPreparationModel;

public interface SaveMenuPreparationRepository extends JpaRepository<SaveMenuPreparationModel, Long> {
}