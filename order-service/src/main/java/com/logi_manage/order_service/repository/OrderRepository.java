package com.logi_manage.order_service.repository;

import com.logi_manage.order_service.dto.response.OrderDetailResponseDto;
import com.logi_manage.order_service.dto.response.OrderItemDetailResponseDto;
import com.logi_manage.order_service.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT new com.logi_manage.order_service.dto.response.OrderDetailResponseDto(" +
            "o.id, c.id, c.name, o.orderStatus, o.createdAt) " +
            "FROM Order o " +
            "JOIN o.customer c" +
            "JOIN o.orderItemList oi " +
            "WHERE (:orderId IS NULL OR o.id = :orderId) " +
            "AND (:customerId IS NULL OR c.id = :customerId) " +
            "AND (:productId IS NULL OR oi.productId = :productId) " +
            "AND (:dateFrom IS NULL OR o.createdAt >= :dateFrom) " +
            "AND (:dateTo IS NULL OR o.createdAt <= :dateTo)"
    )
    Page<OrderDetailResponseDto> findOrderWithFilterAndSorting(
            @Param("orderId") Long orderId,
            @Param("customerId") Long customerId,
            @Param("productId") Long productId,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            Pageable pageable
    );

    @Query("SELECT new com.logi_manage.order_service.dto.response.OrderItemDetailResponseDto( " +
            "oi.id, oi.productId, oi.quantity, oi.price) " +
            "FROM OrderItem oi WHERE oi.order.id IN :orderIds")
    List<OrderItemDetailResponseDto> findByOrderIds(List<Long> orderIds);
}
