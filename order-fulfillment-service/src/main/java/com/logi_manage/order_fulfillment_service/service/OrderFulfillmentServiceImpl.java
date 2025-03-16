package com.logi_manage.order_fulfillment_service.service;

import com.logi_manage.order_fulfillment_service.constant.OrderFulfillmentStatus;
import com.logi_manage.order_fulfillment_service.dto.request.CreateOrderFulfillmentRequestDto;
import com.logi_manage.order_fulfillment_service.entity.OrderFulfillment;
import com.logi_manage.order_fulfillment_service.repository.OrderFulfillmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class OrderFulfillmentServiceImpl implements OrderFulfillmentService{
    private final OrderFulfillmentRepository orderFulfillmentRepository;

    @Override
    public void createOrderFulfillment(CreateOrderFulfillmentRequestDto orderFulfillmentRequestDto) {


        OrderFulfillment.builder()
                .fulfilled(false)
                .productId(orderFulfillmentRequestDto.productId())
                .warehouseId(orderFulfillmentRequestDto.warehouseId())
                .orderId(orderFulfillmentRequestDto.orderId())
                //TODO: order->orderItem에서 quantity를 가져와서 사용 해야할까??
                .quantity(0)
                .orderFulfillmentStatus(OrderFulfillmentStatus.PROCESSING)
                .build();
    }
}
