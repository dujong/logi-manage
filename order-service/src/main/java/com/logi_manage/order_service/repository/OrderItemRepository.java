package com.logi_manage.order_service.repository;

import com.logi_manage.order_service.dto.response.OrderItemDetailResponseDto;
import com.logi_manage.order_service.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
