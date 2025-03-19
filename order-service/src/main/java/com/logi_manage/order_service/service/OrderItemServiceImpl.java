package com.logi_manage.order_service.service;

import com.logi_manage.order_service.dto.response.OrderItemDetailResponseDto;
import com.logi_manage.order_service.entity.Order;
import com.logi_manage.order_service.entity.OrderItem;
import com.logi_manage.order_service.repository.OrderItemRepository;
import com.logi_manage.order_service.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService{
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * 주문 아이템 리스트 조회
     * @param orderId 주문 id
     * @return 주문 아이템 list
     */
    @Override
    public List<OrderItemDetailResponseDto> getOrderItemList(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return order.getOrderItemList()
                .stream()
                .map(orderItem -> new OrderItemDetailResponseDto(orderItem.getId(), orderItem.getProductId(), orderItem.getQuantity(), orderItem.getPrice()))
                .toList();
    }

    /**
     * 주문 아이템 조회
     * @param orderItemId 조회할 주문 아이템 id
     * @return 주문 아이템 상세 info
     */
    @Override
    public OrderItemDetailResponseDto getOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() -> new EntityNotFoundException("OrderItem not found"));
        return new OrderItemDetailResponseDto(orderItem.getId(), orderItem.getProductId(), orderItem.getQuantity(), orderItem.getPrice());
    }
}
