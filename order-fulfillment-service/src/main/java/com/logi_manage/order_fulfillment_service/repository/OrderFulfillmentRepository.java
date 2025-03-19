package com.logi_manage.order_fulfillment_service.repository;

import com.logi_manage.order_fulfillment_service.constant.OrderFulfillmentStatus;
import com.logi_manage.order_fulfillment_service.dto.response.OrderFulfillmentDetailResponseDto;
import com.logi_manage.order_fulfillment_service.entity.OrderFulfillment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrderFulfillmentRepository extends JpaRepository<OrderFulfillment, Long> {

    @Query("SELECT new com.logi_manage.order_fulfillment_service.dto.response.OrderFulfillmentDetailResponseDto( " +
            "of.id, of.orderId, of.productId, p.name, of.quantity, of.warehouseId, of.status, of.remarks, of.createdAt, of.modifiedAt" +
            "FROM OrderFulfillment of " +
            "JOIN Product p ON of.productId = p.id " +
            "WHERE (:productId IS NULL OR of.productId = :productId) " +
            "AND (:warehouseId IS NULL OR of.warehouseId = :warehouseId) " +
            "AND (:orderId IS NULL OR of.orderId = :orderId) " +
            "AND (:status IS NULL OR of.status = :status) " +
            "AND (:dateFrom IS NULL OR of.createdAt >= :dateFrom) " +
            "AND (:dateTo IS NULL OR of.createdAt <= :dateTo)"
    )
    Page<OrderFulfillmentDetailResponseDto> findOrderFulfillmentWithFilterAndSorting(
            @Param("productId") Long productId,
            @Param("warehouseId") Long warehouseId,
            @Param("orderId") Long orderId,
            @Param("status") OrderFulfillmentStatus status,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            Pageable pageable
    );

    @Query("SELECT new com.logi_manage.order_fulfillment_service.dto.response.StockInDetailResponseDto( " +
            "of.id, of.orderId, of.productId, p.name, of.quantity, of.warehouseId, of.status, of.remarks, of.createdAt, of.modifiedAt" +
            "FROM OrderFulfillment of " +
            "JOIN Product p ON of.productId = p.id " +
            "WHERE (:orderFulfillmentId IS NULL OR of.id = :orderFulfillmentId) " +
            "AND (:productId IS NULL OR of.productId = :productId) " +
            "AND (:warehouseId IS NULL OR of.warehouseId = :warehouseId) "
    )
    OrderFulfillmentDetailResponseDto getOrderFulfillmentByProductIdAndWarehouseId(
            @Param("orderFulfillmentId") Long orderFulfillmentId,
            @Param("productId") Long productId,
            @Param("warehouseId") Long warehouseId
    );

    @Query("SELECT new com.logi_manage.order_fulfillment_service.dto.response.StockInDetailResponseDto( " +
            "of.id, of.orderId, of.productId, of.quantity, of.warehouseId, of.status, of.remarks, of.createdAt, of.modifiedAt" +
            "FROM OrderFulfillment of " +
            "WHERE (:orderId IS NULL OR of.orderId = :orderId) " +
            "AND (:orderItemId IS NULL OR of.orderItemId = :orderItemId) " +
            "AND (:productId IS NULL OR of.productId = :productId) "
    )
    OrderFulfillmentDetailResponseDto getOrderFulfillmentByOrderIdAndOrderItemIdAndProductId(
            @Param("orderId") Long orderId,
            @Param("orderItemId") Long orderItemId,
            @Param("productId") Long productId
    );
}
