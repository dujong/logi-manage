package com.logi_manage.procurement_service.repository;

import com.logi_manage.procurement_service.entity.Procurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcurementRepository extends JpaRepository<Procurement, Long> {
}
