package com.logi_manage.order_fulfillment_service.service;

import com.logi_manage.order_fulfillment_service.constant.OrderStatus;
import com.logi_manage.order_fulfillment_service.constant.StockInStatus;
import com.logi_manage.order_fulfillment_service.dto.request.*;
import com.logi_manage.order_fulfillment_service.dto.response.*;
import com.logi_manage.order_fulfillment_service.entity.StockIn;
import com.logi_manage.order_fulfillment_service.repository.OrderFulfillmentRepository;
import com.logi_manage.order_fulfillment_service.repository.StockInRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static com.logi_manage.order_fulfillment_service.constant.StockInStatus.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class StockInServiceImpl implements StockInService{
    private final StockInRepository stockInRepository;

    private final RestTemplate restTemplate;

    private final String productServiceUrl = "localhost:8081/products/";
    private final String warehouseServiceUrl = "localhost:8084/warehouses/";
    private final String inventoryServiceUrl = "localhost:8084/inventories/";
    private final String orderServiceUrl = "localhost:8087/orders/";

    /**
     * 발주된 상품 입고 처리
     * @param stockInRequestDto 입고 처리 dto
     */
    @Override
    public ResponseEntity<String> processStockIn(StockInRequestDto stockInRequestDto) {
        try{
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

            //4. 재고 정보 확인(재고가 존재하는지 확인)
            url = inventoryServiceUrl + stockInRequestDto.productId() + "/" + stockInRequestDto.warehouseId();
            InventoryDetailResponseDto inventoryDto = restTemplate.getForObject(url, InventoryDetailResponseDto.class);
            if (inventoryDto.id() == null) {
                return ResponseEntity.badRequest().body("Inventory not found");
            }

            //5. 입고 처리 등록
            StockIn stockIn = StockIn.builder()
                    .orderId(stockInRequestDto.orderId())
                    .productId(stockInRequestDto.productId())
                    .quantity(stockInRequestDto.quantity())
                    .warehouseId(stockInRequestDto.warehouseId())
                    .status(PENDING)
                    .build();
            StockIn savedStockIn = stockInRepository.save(stockIn);

            return ResponseEntity.ok(savedStockIn.getId().toString());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing stock-in: " + e.getMessage());
        }
    }

    /**
     * 입고 검수
     * @param stockInVerifyRequestDto 입고 검수 dto
     */
    @Override
    public ResponseEntity<String> verifyStockIn(StockInVerifyRequestDto stockInVerifyRequestDto) {
        StockIn stockIn = stockInRepository.findById(stockInVerifyRequestDto.id()).orElseThrow(() -> new EntityNotFoundException("StockIn not found"));

        if (stockIn.getStatus() != PENDING) {
            return ResponseEntity.badRequest().body("Only PENDING stock-in can be verified");
        }

        stockIn.setStatus(stockInVerifyRequestDto.status());
        stockIn.setRemarks(stockInVerifyRequestDto.remarks());
        return ResponseEntity.ok("Stock-in verified successfully");
    }

    /**
     * 입고 승인
     * @param stockId 승인된 입고 id
     * @param updateStockInRequestDto 입고 update dto
     */
    @Override
    public ResponseEntity<String> updateStockInQuantity(Long stockId, UpdateStockInRequestDto updateStockInRequestDto) {
        StockIn stockIn = stockInRepository.findById(stockId).orElseThrow(() -> new EntityNotFoundException("StockIn not found"));

        if (stockIn.getStatus() != APPROVED) {
            return ResponseEntity.badRequest().body("Stock-in status must be APPROVED to update the quantity");
        }

        UpdateInventoryRequestDto updateInventoryRequestDto = new UpdateInventoryRequestDto(updateStockInRequestDto.quantity());
        restTemplate.patchForObject(inventoryServiceUrl + updateStockInRequestDto.id(), updateInventoryRequestDto, Void.class);
        return ResponseEntity.noContent().build();
    }

    /**
     * 입고 리스트 조회
     * @param filterRequestDto 필터링 dto
     * @param pageable 페이징
     * @return 입고 리스트
     */
    @Override
    public Page<StockInDetailResponseDto> getStockInList(StockInFilterRequestDto filterRequestDto, Pageable pageable) {
        Sort sort;
        if ("desc".equalsIgnoreCase(filterRequestDto.sortDirection())) {
            sort = Sort.by(Sort.Order.by(filterRequestDto.sortBy()));
        } else {
            sort = Sort.by(Sort.Order.by(filterRequestDto.sortBy()));
        }
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return stockInRepository.findStockInWithFilterAndSorting(filterRequestDto.productId(), filterRequestDto.warehouseId(), filterRequestDto.orderId(), filterRequestDto.status(), filterRequestDto.dateFrom(), filterRequestDto.dateTo(), sortedPageable);
    }

    /**
     * 입고 상세 조회
     * @param stockId 조회할 입고 id
     * @param filterRequestDto 필터링 dto
     * @return 입고 상세 info
     */
    @Override
    public StockInDetailResponseDto getStockIn(Long stockId, StockInFilterRequestDto filterRequestDto) {
        return stockInRepository.getStockInDto(stockId, filterRequestDto.productId(), filterRequestDto.warehouseId());
    }
}
