package com.logi_manage.order_fulfillment_service.repository;

import com.logi_manage.order_fulfillment_service.constant.StockInStatus;
import com.logi_manage.order_fulfillment_service.dto.response.StockInDetailResponseDto;
import com.logi_manage.order_fulfillment_service.entity.StockIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface StockInRepository extends JpaRepository<StockIn, Long> {

    @Query("SELECT new com.logi_manage.order_fulfillment_service.dto.response.StockInDetailResponseDto( " +
            "si.id, si.orderId, si.productId, p.name, si.quantity, si.warehouseId, si.status, si.remarks, si.createdAt, si.modifiedAt" +
            "FROM StockIn si " +
            "JOIN Product p ON si.productId = p.id " +
            "JOIN Warehouse w ON si.warehouseId = w.id " +
            "WHERE (:productId IS NULL OR si.productId = :productId) " +
            "AND (:warehouseId IS NULL OR si.warehouseId = :warehouseId) " +
            "AND (:orderId IS NULL OR si.orderId = :orderId) " +
            "AND (:status IS NULL OR si.status = :status) " +
            "AND (:dateFrom IS NULL OR si.createdAt >= :dateFrom) " +
            "AND (:dateTo IS NULL OR si.createdAt <= :dateTo)"
    )
    Page<StockInDetailResponseDto> findStockInWithFilterAndSorting(
            @Param("productId") Long productId,
            @Param("warehouseId") Long warehouseId,
            @Param("orderId") Long orderId,
            @Param("status") StockInStatus status,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            Pageable pageable
    );

    @Query("SELECT new com.logi_manage.order_fulfillment_service.dto.response.StockInDetailResponseDto( " +
            "si.id, si.orderId, si.productId, p.name, si.quantity, si.warehouseId, si.status, si.remarks, si.createdAt, si.modifiedAt" +
            "FROM StockIn si " +
            "JOIN Product p ON si.productId = p.id " +
            "JOIN Warehouse w ON si.warehouseId = w.id " +
            "WHERE (:stockId IS NULL OR si.id = :stockId) " +
            "AND (:productId IS NULL OR si.productId = :productId) " +
            "AND (:warehouseId IS NULL OR si.warehouseId = :warehouseId) "
    )
    StockInDetailResponseDto getStockInDto(
            @Param("stockId") Long stockId,
            @Param("productId") Long productId,
            @Param("warehouseId") Long warehouseId
    );
}
