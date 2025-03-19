package com.logi_manage.order_fulfillment_service.repository;

import com.logi_manage.order_fulfillment_service.constant.ReturnRefundReason;
import com.logi_manage.order_fulfillment_service.constant.ReturnRefundStatus;
import com.logi_manage.order_fulfillment_service.dto.response.ReturnRefundDetailResponseDto;
import com.logi_manage.order_fulfillment_service.entity.ReturnRefund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReturnRefundRepository extends JpaRepository<ReturnRefund, Long> {

    @Query("SELECT new com.logi_manage.order_fulfillment_service.dto.response.ReturnRefundDetailResponseDto(" +
            "rr.id, rr.status, rr.reason, rr.customerId, rr.orderId, rr.orderItemId, rr.productId, rr.warehouseId, " +
            "p.name AS productName, p.description AS productDescription, " +
            "w.name AS warehouseName, w.location AS warehouseLocation, " +
            "rr.createdAt, rr.modifiedAt) " +
            "FROM ReturnRefund rr " +
            "JOIN Product p ON rr.productId = p.id " +
            "JOIN Warehouse w ON rr.warehouseId = w.id " +
            "WHERE (:status IS NULL OR rr.status = :status) " +
            "AND (:reason IS NULL OR rr.reason = :reason) " +
            "AND (:customerId IS NULL OR rr.customerId = :customerId) " +
            "AND (:orderId IS NULL OR rr.orderId = :orderId) " +
            "AND (:orderItemId IS NULL OR rr.orderItemId = :orderItemId) " +
            "AND (:productId IS NULL OR rr.productId = :productId) " +
            "AND (:warehouseId IS NULL OR rr.warehouseId = :warehouseId) " +
            "AND (:dateFrom IS NULL OR rr.createdAt >= :dateFrom) " +
            "AND (:dateTo IS NULL OR rr.createdAt <= :dateTo)"
    )
    Page<ReturnRefundDetailResponseDto> findReturnRefundWithFilterAndSorting(
            @Param("status") ReturnRefundStatus status,
            @Param("reason") ReturnRefundReason reason,
            @Param("customerId") Long customerId,
            @Param("orderId") Long orderId,
            @Param("orderItemId") Long orderItemId,
            @Param("productId") Long productId,
            @Param("warehouseId") Long warehouseId,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            Pageable pageable
    );
}
