package com.logi_manage.order_service.service;

import com.logi_manage.order_service.constant.OrderStatus;
import com.logi_manage.order_service.constant.ShippingStatus;
import com.logi_manage.order_service.dto.request.CreateOrderRequestDto;
import com.logi_manage.order_service.dto.request.OrderFilterRequestDto;
import com.logi_manage.order_service.dto.request.UpdateOrderStatusRequestDto;
import com.logi_manage.order_service.dto.response.CancelOrderResponseDto;
import com.logi_manage.order_service.dto.response.OrderDetailResponseDto;
import com.logi_manage.order_service.dto.response.OrderItemDetailResponseDto;
import com.logi_manage.order_service.dto.response.OrderItemsStatusResponseDto;
import com.logi_manage.order_service.entity.Customer;
import com.logi_manage.order_service.entity.Order;
import com.logi_manage.order_service.entity.OrderItem;
import com.logi_manage.order_service.repository.CustomerRepository;
import com.logi_manage.order_service.repository.OrderItemRepository;
import com.logi_manage.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;

    private final RestTemplate restTemplate;

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
                .receiverName(createOrderRequestDto.receiverName())
                .receiverPhone(createOrderRequestDto.receiverPhone())
                .receiverAddress(createOrderRequestDto.receiverAddress())
                .status(createOrderRequestDto.orderStatus())
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

        if (updateOrderStatusRequestDto.receiverName() != null) {
            order.setReceiverName(updateOrderStatusRequestDto.receiverName());
        }

        if (updateOrderStatusRequestDto.receiverPhone() != null) {
            order.setReceiverPhone(updateOrderStatusRequestDto.receiverPhone());
        }

        if (updateOrderStatusRequestDto.receiverAddress() != null) {
            order.setReceiverAddress(updateOrderStatusRequestDto.receiverAddress());
        }

        if (updateOrderStatusRequestDto.status() != null) {
            order.setStatus(updateOrderStatusRequestDto.status());
        }
    }

    /**
     * 주문 취소
     * @param orderId 취소할 주문 id
     * @return 주문 취소 info list
     */
    @Override
    public CancelOrderResponseDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        List<OrderItemsStatusResponseDto> orderItemResponses = new ArrayList<>();

        for (OrderItem orderItem : order.getOrderItemList()) {
            //배송이 진행중인지 체크
            ShippingStatus shippingStatus = restTemplate.getForObject("localhost:8087/shipments/"+orderItem.getId()+"/status", ShippingStatus.class);

            //배송이 진행중인 경우
            if (shippingStatus != ShippingStatus.PENDING) {
                orderItemResponses.add(new OrderItemsStatusResponseDto(
                        orderItem.getId(),
                        shippingStatus,
                        "Item cannot be canceled, shipment already started"
                ));
            } else {
                orderItemResponses.add(new OrderItemsStatusResponseDto(
                        orderItem.getId(),
                        shippingStatus,
                        "Item can be canceled"
                ));
            }
        }

        //주문된 모든 item list 배송 시작 상태가 아닐 때 주문 취소 상태 변경
        boolean canCancel = orderItemResponses.stream().allMatch(item -> item.status() == ShippingStatus.PENDING);

        if (canCancel) {
            order.setStatus(OrderStatus.CANCELED);
            return null;
        } else {
            return new CancelOrderResponseDto(order.getId(), orderItemResponses);
        }
    }

    /**
     * 주문 리스트 조회
     * @param filterRequestDto 필터링 dto
     * @param pageable 페이징
     * @return 주문 list
     */
    @Override
    public Page<OrderDetailResponseDto> getOrderList(OrderFilterRequestDto filterRequestDto, Pageable pageable) {
        //TODO: 해당 리스트 조회를 할 때 한가지 고민에 휩싸였다
        // entity를 조회하는 방식의 고민이였는데
        // 1. Page<Order>를 받아서 order.getOrderItemList()로 dto 완성
        // 2. Page<OrderDetailResponseDto>로 받은 후 OrderItemList는 추가 SQL문으로 조회
        //              현재방식(JPQL + 별도조회)      JPA로 접근
        // 쿼리 최적화	✅ 불필요한 데이터 로드 최소화	❌ N+1 문제가 발생할 가능성 있음
        // 페이징 성능	✅ DB에서 직접 페이징	        ❌ order.getOrderItemList() 조회 시 추가적인 SELECT 발생 가능
        // 코드 가독성	❌ DTO 매핑이 복잡함	        ✅ 코드가 간결함
        // 물류/재고 웹 사이트 특성상 대용량의 데이터를 만질 가능성이 커서 성능을 초점에 두고 개발하였다

        //1. 주문 목록을 페이징 처리하여 가져오기
        Sort sort;
        if ("desc".equalsIgnoreCase(filterRequestDto.sortDirection())) {
            sort = Sort.by(Sort.Order.by(filterRequestDto.sortBy()));
        } else {
            sort = Sort.by(Sort.Order.by(filterRequestDto.sortBy()));
        }
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<OrderDetailResponseDto> orderWithFilterAndSorting = orderRepository.findOrderWithFilterAndSorting(filterRequestDto.orderId(), filterRequestDto.customerId(), filterRequestDto.productId(), filterRequestDto.dateFrom(), filterRequestDto.dateTo(), sortedPageable);

        //2. 주문 ID 목록 추출
        List<Long> orderIds = orderWithFilterAndSorting.getContent().stream()
                .map(OrderDetailResponseDto::id)
                .toList();

        //3. 주문 Id별 OrderItem 조회
        Map<Long, List<OrderItemDetailResponseDto>> orderItemMap = orderRepository.findByOrderIds(orderIds)
                .stream()
                .collect(Collectors.groupingBy(OrderItemDetailResponseDto::id));

        //4. 주문 Dto에 OrderItemList 추가
        Page<OrderDetailResponseDto> result = orderWithFilterAndSorting.map(order -> new OrderDetailResponseDto(
                order.id(),
                order.customerId(),
                order.customerName(),
                order.status(),
                orderItemMap.getOrDefault(order.id(), List.of()),
                order.createdAt(),
                order.receiverName(),
                order.receiverPhone(),
                order.receiverAddress()
        ));

        return result;
    }

    /**
     * 주문 상세 조회
     * @param orderId 조회할 주문 id
     * @return 주문 상세 info
     */
    @Override
    public OrderDetailResponseDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return new OrderDetailResponseDto(order.getId(), order.getCustomer().getId(), order.getCustomer().getName(), order.getStatus(), OrderItemDetailResponseDto.mapEntityToDto(order.getOrderItemList()), order.getCreatedAt(), order.getReceiverName(), order.getReceiverPhone(), order.getReceiverAddress());
    }
}
