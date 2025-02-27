package com.logi_manage.inventory_service.repository;

import com.logi_manage.inventory_service.entity.InventoryTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryTransferRepository extends JpaRepository<InventoryTransfer, Long> {
}
