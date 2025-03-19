package com.logi_manage.shipment_service.repository;

import com.logi_manage.shipment_service.constant.ShippingStatus;
import com.logi_manage.shipment_service.dto.response.ShipmentDetailResponseDto;
import com.logi_manage.shipment_service.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    @Query("SELECT new com.logi_manage.shipment_service.dto.response.ShipmentDetailResponseDto(" +
            "s.id, s.orderId, s.customerId, c.name, s.createdAt, s.modifiedAt, of.shippedDate, s.deliveredDate, o.receiverName, o.receiverPhone, o.receiverAddress, s.trackingNumber, s.courierName) " +
            "FROM Shipment s " +
            "JOIN Customer c ON s.customerId = c.id " +
            "JOIN OrderFulfillment of ON s.orderFulfillmentId = of.id " +
            "JOIN Order o ON s.orderId = o.id " +
            "WHERE (:orderId IS NULL OR s.orderId = :orderId) " +
            "AND (:orderItemId IS NULL OR EXISTS (SELECT 1 FROM OrderItem oi WHERE oi.orderId = s.orderId AND oi.id = :orderItemId)) " +
            "AND (:customerId IS NULL OR s.customerId = :customerId) " +
            "AND (:orderFulfillmentId IS NULL OR s.orderFulfillmentId = :orderFulfillmentId) " +
            "AND (:status IS NULL OR s.status = :status) " +
            "AND (:trackingNumber IS NULL OR s.trackingNumber LIKE %:trackingNumber%) " +
            "AND (:courierName IS NULL OR s.courierName LIKE %:courierName%) " +
            "AND (:deliveredAfter IS NULL OR s.deliveredDate >= :deliveredAfter) " +
            "AND (:deliveredBefore IS NULL OR s.deliveredDate <= :deliveredBefore) " +
            "AND (:shippedAfter IS NULL OR of.shippedDate >= :shippedAfter) " +
            "AND (:shippedBefore IS NULL OR of.shippedDate <= :shippedBefore) "
    )
    Page<ShipmentDetailResponseDto> findShipmentWithFilterAndSorting(
            @Param("orderId") Long orderId,
            @Param("orderItemId") Long orderItemId,
            @Param("customerId") Long customerId,
            @Param("orderFulfillmentId") Long orderFulfillmentId,
            @Param("status") ShippingStatus status,
            @Param("trackingNumber") String trackingNumber,
            @Param("courierName") String courierName,
            @Param("deliveredAfter") LocalDateTime deliveredAfter,
            @Param("deliveredBefore") LocalDateTime deliveredBefore,
            @Param("shippedAfter") LocalDateTime shippedAfter,
            @Param("shippedBefore") LocalDateTime shippedBefore,
            Pageable pageable
    );

    @Query("SELECT new com.logi_manage.shipment_service.dto.response.ShipmentDetailResponseDto(" +
            "s.id, s.orderId, s.customerId, c.name, s.createdAt, s.modifiedAt, of.shippedDate, s.deliveredDate, o.receiverName, o.receiverPhone, o.receiverAddress, s.trackingNumber, s.courierName) " +
            "FROM Shipment s " +
            "JOIN Customer c ON s.customerId = c.id " +
            "JOIN OrderFulfillment of ON s.orderFulfillmentId = of.id " +
            "JOIN Order o ON s.orderId = o.id " +
            "WHERE (:shipmentId IS NULL OR s.id = :shipmentId) "
    )
    ShipmentDetailResponseDto getShipment(@Param("shipmentId") Long shipmentId);

    @Query("SELECT i.id FROM Inventory i " +
            "JOIN OrderFulfillment ofu ON i.productId = ofu.productId " +
            "JOIN Shipment s ON s.orderFulfillmentId = ofu.id " +
            "WHERE s.id = :shipmentId")
    Long findInventoryIdByShipmentId(@Param("shipmentId") Long shipmentId);

    @Query("SELECT of.quantity FROM OrderFulfillment of " +
            "WHERE of.id = :orderFulfillmentId")
    int findQuantityByOrderFulfillmentId(@Param("orderFulfillmentId") Long orderFulfillmentId);

    ShippingStatus findShippingStatusByOrderItemId(Long orderItemId);
}
