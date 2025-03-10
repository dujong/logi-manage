package com.logi_manage.inventory_service.repository;

import com.logi_manage.inventory_service.dto.response.TransferDetailResponseDto;
import com.logi_manage.inventory_service.entity.InventoryTransfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface InventoryTransferRepository extends JpaRepository<InventoryTransfer, Long> {
    @Query("SELECT new com.logi_manage.inventory_service.dto.response.TransferDetailResponseDto( " +
            "t.id, p.id, p.name, " +
            "ow.id, ow.name, " +
            "dw.id, dw.name, " +
            "t.quantity, t.createAt, t.modifiedAt) " +
            "JOIN t.product p " +
            "JOIN t.originWarehouse ow " +
            "JOIN t.destinationWarehouse dw " +
            "FROM InventoryTransfer t " +
            "WHERE (:productId IS NULL OR t.productId = :productId) " +
            "AND (:fromWarehouseId IS NULL OR t.originWarehouse.id = :fromWarehouseId) " +
            "AND (:ToWarehouseId IS NULL OR t.destinationWarehouse.id = :ToWarehouseId) " +
            "AND (:dateFrom IS NULL OR t.createAt >= :dateFrom) " +
            "AND (:dateTo IS NULL OR t.createAt <= :dateTo)"
    )
    Page<TransferDetailResponseDto> findTransferWithFilters(
            @Param("productId") Long productId,
            @Param("fromWarehouseId") Long fromWarehouseId,
            @Param("ToWarehouseId") Long ToWarehouseId,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            Pageable pageable
    );
}
