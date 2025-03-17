package com.logi_manage.order_fulfillment_service.service;

import com.logi_manage.order_fulfillment_service.constant.OrderFulfillmentStatus;
import com.logi_manage.order_fulfillment_service.dto.request.*;
import com.logi_manage.order_fulfillment_service.dto.response.OrderFulfillmentDetailResponseDto;
import com.logi_manage.order_fulfillment_service.entity.OrderFulfillment;
import com.logi_manage.order_fulfillment_service.entity.StockIn;
import com.logi_manage.order_fulfillment_service.repository.OrderFulfillmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static com.logi_manage.order_fulfillment_service.constant.OrderFulfillmentStatus.PROCESSING;
import static com.logi_manage.order_fulfillment_service.constant.StockInStatus.APPROVED;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class OrderFulfillmentServiceImpl implements OrderFulfillmentService{
    private final OrderFulfillmentRepository orderFulfillmentRepository;

    private final RestTemplate restTemplate;

    /**
     * 주문 출고 처리
     * @param orderFulfillmentRequestDto 주문 출고 dto
     * @return 출고 id
     */
    @Override
    public Long createOrderFulfillment(CreateOrderFulfillmentRequestDto orderFulfillmentRequestDto) {
        //TODO: kafka로 비동기식 처리를 활용하여 성능 업그레이드 해보기

        OrderFulfillment orderFulfillment = OrderFulfillment.builder()
                .productId(orderFulfillmentRequestDto.productId())
                .warehouseId(orderFulfillmentRequestDto.warehouseId())
                .orderId(orderFulfillmentRequestDto.orderId())
                .quantity(orderFulfillmentRequestDto.quantity())
                .status(PROCESSING)
                .build();
        OrderFulfillment savedOrderfulfillment = orderFulfillmentRepository.save(orderFulfillment);

        return savedOrderfulfillment.getId();
    }

    /**
     * 출고 검수
     * @param verifyRequestDto 출고 검수 dto
     */
    @Override
    public ResponseEntity<String> verifyOrderFulfillment(OrderFulfillmentVerifyRequestDto verifyRequestDto) {
        OrderFulfillment orderFulfillment = orderFulfillmentRepository.findById(verifyRequestDto.id()).orElseThrow(() -> new EntityNotFoundException("OrderFulfillment not found"));

        if (orderFulfillment.getStatus() != PROCESSING) {
            return ResponseEntity.badRequest().body("Only PROCESSING order-fulfillment can be verified");
        }

        orderFulfillment.setStatus(verifyRequestDto.status());
        orderFulfillment.setRemarks(verifyRequestDto.remarks());
        return ResponseEntity.ok("OrderFulfillment verified successfully");
    }

    /**
     * 출고 승인
     * @param orderFulfillmentId 승인된 출고 id
     * @param updateOrderFulfillmentRequestDto 출고 update dto
     */
    @Override
    public ResponseEntity<String> updateOrderFulfillmentQuantity(Long orderFulfillmentId, UpdateOrderFulfillmentRequestDto updateOrderFulfillmentRequestDto) {
        OrderFulfillment orderFulfillment = orderFulfillmentRepository.findById(orderFulfillmentId).orElseThrow(() -> new EntityNotFoundException("OrderFulfillment not found"));

        if (orderFulfillment.getStatus() != PROCESSING) {
            return ResponseEntity.badRequest().body("Order-fulfillment status must be PROCESSING to update the quantity");
        }

        UpdateInventoryRequestDto updateInventoryRequestDto = new UpdateInventoryRequestDto(updateOrderFulfillmentRequestDto.quantity());
        restTemplate.patchForObject("localhost:8084/inventories/" + updateOrderFulfillmentRequestDto.id(), updateInventoryRequestDto, Void.class);

        return ResponseEntity.noContent().build();
    }

    /**
     * 출고 리스트 조회
     * @param filterRequestDto 필터링 dto
     * @param pageable 페이징
     * @return 출고 리스트
     */
    @Override
    public Page<OrderFulfillmentDetailResponseDto> getOrderFulfillmentList(OrderFulfillmentFilterRequestDto filterRequestDto, Pageable pageable) {
        return orderFulfillmentRepository.findOrderFulfillmentWithFilterAndSorting(filterRequestDto.productId(), filterRequestDto.warehouseId(), filterRequestDto.orderId(), filterRequestDto.status(), filterRequestDto.dateFrom(), filterRequestDto.dateTo(), pageable);
    }

    /**
     * 출고 상세 조회
     * @param orderFulfillmentId 조회할 출고 id
     * @param filterRequestDto 필터링
     * @return 출고 상세 info
     */
    @Override
    public OrderFulfillmentDetailResponseDto getOrderFulfillment(Long orderFulfillmentId, OrderFulfillmentFilterRequestDto filterRequestDto) {
        return orderFulfillmentRepository.getOrderFulfillmentDto(orderFulfillmentId, filterRequestDto.productId(), filterRequestDto.warehouseId());
    }
}
