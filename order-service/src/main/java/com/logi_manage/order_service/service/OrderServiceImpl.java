package com.logi_manage.order_service.service;

import com.logi_manage.order_service.dto.request.CreateOrderRequestDto;
import com.logi_manage.order_service.dto.request.OrderFilterRequestDto;
import com.logi_manage.order_service.dto.request.UpdateOrderStatusRequestDto;
import com.logi_manage.order_service.dto.response.OrderDetailResponseDto;
import com.logi_manage.order_service.entity.Customer;
import com.logi_manage.order_service.entity.Order;
import com.logi_manage.order_service.entity.OrderItem;
import com.logi_manage.order_service.repository.CustomerRepository;
import com.logi_manage.order_service.repository.OrderItemRepository;
import com.logi_manage.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;

    /**
     * 신규 주문 생성
     * @param createOrderRequestDto 주문 생성 dto
     * @return 생성된 주문 id
     */
    @Override
    public Long createOrder(CreateOrderRequestDto createOrderRequestDto) {
        //고객 조회
        Customer customer = customerRepository.findById(createOrderRequestDto.customerId()).orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        //주문 생성 및 저장
        Order order = Order.builder()
                .customer(customer)
                .orderStatus(createOrderRequestDto.orderStatus())
                .build();
        Order savedOrder = orderRepository.save(order);

        //주문 아이템 리스트 생성
        List<OrderItem> orderItemList = createOrderRequestDto.createOrderItemRequestDtos()
                .stream()
                .map(requestDto -> OrderItem.builder()
                        .order(savedOrder)
                        .productId(requestDto.productId())
                        .quantity(requestDto.quantity())
                        .price(requestDto.price())
                        .build())
                .toList();

        //주문 아이템 저장
        if (!orderItemList.isEmpty()) {
            List<OrderItem> savedOrderItemList = orderItemRepository.saveAll(orderItemList);
            savedOrder.setOrderItemList(savedOrderItemList);
        }
        return savedOrder.getId();
    }

    /**
     * 주문 상태 변경
     * @param orderId 주문 id
     * @param updateOrderStatusRequestDto 주문 변경 dto
     */
    @Override
    public void updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto updateOrderStatusRequestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setOrderStatus(updateOrderStatusRequestDto.orderStatus());
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public Page<OrderDetailResponseDto> getOrderList(OrderFilterRequestDto filterRequestDto, Pageable pageable) {
        
    }

    /**
     * 주문 상세 조회
     * @param orderId 조회할 주문 id
     * @return 주문 상세 info
     */
    @Override
    public OrderDetailResponseDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return new OrderDetailResponseDto(order.getId(), order.getCustomer().getId(), order.getCustomer().getName(), order.getOrderStatus(), order.getOrderItemList(), order.getCreatedAt());
    }
}
