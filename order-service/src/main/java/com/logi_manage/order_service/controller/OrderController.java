package com.logi_manage.order_service.controller;

import com.logi_manage.order_service.dto.request.CreateOrderRequestDto;
import com.logi_manage.order_service.dto.request.OrderFilterRequestDto;
import com.logi_manage.order_service.dto.request.UpdateOrderStatusRequestDto;
import com.logi_manage.order_service.dto.response.CancelOrderResponseDto;
import com.logi_manage.order_service.dto.response.OrderDetailResponseDto;
import com.logi_manage.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    /**
     * list up
     * - [O]  주문 생성
     * - [O]  주문 상태 변경 (대기 → 처리중 → 완료)
     * - [O]  주문 취소 및 조회
     */

    /**
     * 신규 주문 생성
     *
     * @param createOrderRequestDto 주문 생성 dto
     * @return 생성된 주문 id
     */
    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody CreateOrderRequestDto createOrderRequestDto) {
        Long orderId = orderService.createOrder(createOrderRequestDto);
        return ResponseEntity.ok(orderId);
    }

    /**
     * 주문 상태 변경
     *
     * @param orderId                     수정할 주문 id
     * @param updateOrderStatusRequestDto 주문 변경 dto
     */
    @PostMapping("/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderStatusRequestDto updateOrderStatusRequestDto) {
        orderService.updateOrderStatus(orderId, updateOrderStatusRequestDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * 주문 취소
     *
     * @param orderId 취소할 주문 id
     * @return 주문 취소 info list
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<CancelOrderResponseDto> cancelOrder(@PathVariable Long orderId) {
        CancelOrderResponseDto cancelOrderResponseDto = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(cancelOrderResponseDto);
    }

    /**
     * 주문 리스트 조회
     *
     * @param filterRequestDto 필터링 dto
     * @param pageable         페이징
     * @return 주문 list
     */
    @GetMapping
    public ResponseEntity<Page<OrderDetailResponseDto>> getOrderList(OrderFilterRequestDto filterRequestDto, Pageable pageable) {
        Page<OrderDetailResponseDto> orderList = orderService.getOrderList(filterRequestDto, pageable);
        return ResponseEntity.ok(orderList);
    }

    /**
     * 주문 상세 조회
     *
     * @param orderId 조회할 주문 id
     * @return 주문 상세 info
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponseDto> getOrder(@PathVariable Long orderId) {
        OrderDetailResponseDto orderDetailResponseDto = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderDetailResponseDto);
    }
}
