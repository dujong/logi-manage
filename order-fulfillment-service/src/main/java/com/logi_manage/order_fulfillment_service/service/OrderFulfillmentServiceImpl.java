package com.logi_manage.order_fulfillment_service.service;

import com.logi_manage.order_fulfillment_service.constant.OrderStatus;
import com.logi_manage.order_fulfillment_service.dto.request.StockInRequestDto;
import com.logi_manage.order_fulfillment_service.dto.response.OrderDetailResponseDto;
import com.logi_manage.order_fulfillment_service.dto.response.ProductDetailResponseDto;
import com.logi_manage.order_fulfillment_service.dto.response.WarehouseDetailResponseDto;
import com.logi_manage.order_fulfillment_service.entity.OrderFulfillment;
import com.logi_manage.order_fulfillment_service.repository.OrderFulfillmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class OrderFulfillmentServiceImpl implements OrderFulfillmentService{
    private final OrderFulfillmentRepository orderFulfillmentRepository;
    private final RestTemplate restTemplate;

    private final String orderServiceUrl = "localhost:8087/orders/";
    private final String productServiceUrl = "localhost:8081/products/";
    private final String warehouseServiceUrl = "localhost:8084/warehouses/";
    private final String inventoryServiceUrl = "localhost:8084/inventories/";

    @Override
    public ResponseEntity<String> processStockIn(StockInRequestDto stockInRequestDto) {

        //1. 발주 정보 확인(주문 상태가 '완료' 상태인지 체크)
        String url = orderServiceUrl + stockInRequestDto.orderId();
        OrderDetailResponseDto orderDto = restTemplate.getForObject(url, OrderDetailResponseDto.class);

        if (orderDto.orderStatus() != OrderStatus.COMPLETED) {
            return ResponseEntity.badRequest().body("Invalid order status");
        }

        //2. 상품 정보 확인(상품이 존재하는지 확인)
        url = productServiceUrl + stockInRequestDto.productId();
        ProductDetailResponseDto productDto = restTemplate.getForObject(url, ProductDetailResponseDto.class);

        if (productDto.id() == null) {
            return ResponseEntity.badRequest().body("Product not found");
        }

        //3. 창고 정보 확인(창고가 존재하는지 확인)
        url = warehouseServiceUrl + stockInRequestDto.warehouseId();
        WarehouseDetailResponseDto warehouseDto = restTemplate.getForObject(url, WarehouseDetailResponseDto.class);
        if (warehouseDto.id() == null) {
            return ResponseEntity.badRequest().body("Warehouse not found");
        }

        //4. 입고 처리(재고 수량 갱신)
        url = inventoryServiceUrl + stockInRequestDto.


        return null;
    }
}
