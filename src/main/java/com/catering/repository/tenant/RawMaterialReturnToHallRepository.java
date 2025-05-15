package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.RawMaterialReturnToHallModel;

public interface RawMaterialReturnToHallRepository extends JpaRepository<RawMaterialReturnToHallModel, Long> {
}