package com.logi_manage.shipment_service.service;

import com.logi_manage.shipment_service.constant.ShippingStatus;
import com.logi_manage.shipment_service.dto.request.CreateShipmentRequestDto;
import com.logi_manage.shipment_service.dto.request.ShipmentFilterRequestDto;
import com.logi_manage.shipment_service.dto.request.UpdateInventoryRequestDto;
import com.logi_manage.shipment_service.dto.request.UpdateShipmentRequestDto;
import com.logi_manage.shipment_service.dto.response.ShipmentDetailResponseDto;
import com.logi_manage.shipment_service.entity.Shipment;
import com.logi_manage.shipment_service.repository.ShipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ShipmentServiceImpl implements ShipmentService{
    private final ShipmentRepository shipmentRepository;

    private final RestTemplate restTemplate;

    /**
     * 배송 요청 생성
     * @param createShipmentRequestDto 배송 생성 dto
     * @return 생성된 배송 id
     */
    @Override
    public Long createShipment(CreateShipmentRequestDto createShipmentRequestDto) {
        Shipment shipment = Shipment.builder()
                .orderId(createShipmentRequestDto.orderId())
                .customerId(createShipmentRequestDto.customerId())
                .orderFulfillmentId(createShipmentRequestDto.orderFulfillmentId())
                .status(createShipmentRequestDto.status())
                .trackingNumber(createShipmentRequestDto.trackingNumber())
                .courierName(createShipmentRequestDto.courierName())
                .build();
        Shipment savedShipment = shipmentRepository.save(shipment);
        return savedShipment.getId();
    }

    /**
     * 배송 상태 업데이트
     * @param shipmentId 업데이트 할 배송 id
     * @param updateShipmentRequestDto 배송 업데이트 dto
     */
    @Override
    public void updateShipment(Long shipmentId, UpdateShipmentRequestDto updateShipmentRequestDto) {
        Shipment shipment = shipmentRepository.findById(shipmentId).orElseThrow(() -> new EntityNotFoundException("Shipment not found"));

        if (updateShipmentRequestDto.orderId() != null) {
            shipment.setOrderId(updateShipmentRequestDto.orderId());
        }

        if (updateShipmentRequestDto.orderItemId() != null) {
            shipment.setOrderItemId(updateShipmentRequestDto.orderItemId());
        }

        if (updateShipmentRequestDto.customerId() != null) {
            shipment.setCustomerId(updateShipmentRequestDto.customerId());
        }

        if (updateShipmentRequestDto.orderFulfillmentId() != null) {
            shipment.setOrderFulfillmentId(updateShipmentRequestDto.orderFulfillmentId());
        }

        if (updateShipmentRequestDto.status() != null) {
            shipment.setStatus(updateShipmentRequestDto.status());

            //배송이 취소 됐을 때
            if (updateShipmentRequestDto.status() == ShippingStatus.CANCELED) {
                //quantity inventoryId 조회
                int quantityByOrderFulfillmentId = shipmentRepository.findQuantityByOrderFulfillmentId(shipment.getOrderFulfillmentId());
                Long inventoryId = shipmentRepository.findInventoryIdByShipmentId(shipmentId);

                //api 통신으로 quantity increase
                UpdateInventoryRequestDto updateInventoryRequestDto = new UpdateInventoryRequestDto(quantityByOrderFulfillmentId);
                restTemplate.patchForObject("localhost:8084/inventories/"+inventoryId, updateInventoryRequestDto, Void.class);
            }
        }

        if (updateShipmentRequestDto.trackingNumber() != null) {
            shipment.setTrackingNumber(updateShipmentRequestDto.trackingNumber());
        }

        if (updateShipmentRequestDto.courierName() != null) {
            shipment.setCourierName(updateShipmentRequestDto.courierName());
        }
    }

    /**
     * 배송 리스트 조회
     * @param filterRequestDto 필터링 dto
     * @param pageable 페이징
     * @return 배송 list
     */
    @Override
    public Page<ShipmentDetailResponseDto> getShipmentList(ShipmentFilterRequestDto filterRequestDto, Pageable pageable) {
        Sort sort;
        if ("desc".equalsIgnoreCase(filterRequestDto.sortDirection())) {
            sort = Sort.by(Sort.Order.by(filterRequestDto.sortBy()));
        } else {
            sort = Sort.by(Sort.Order.by(filterRequestDto.sortBy()));
        }
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return shipmentRepository.findShipmentWithFilterAndSorting(
                filterRequestDto.orderId(),
                filterRequestDto.orderItemId(),
                filterRequestDto.customerId(),
                filterRequestDto.orderFulfillmentId(),
                filterRequestDto.status(),
                filterRequestDto.trackingNumber(),
                filterRequestDto.courierName(),
                filterRequestDto.deliveredAfter(),
                filterRequestDto.deliveredBefore(),
                filterRequestDto.shippedAfter(),
                filterRequestDto.shippedBefore(),
                sortedPageable
        );
    }

    /**
     * 배송 상세 조회
     * @param shipmentId 조회할 배송 id
     * @return 배송 상세 info
     */
    @Override
    public ShipmentDetailResponseDto getShipment(Long shipmentId) {
        return shipmentRepository.getShipment(shipmentId);
    }

    /**
     * 배송 완료
     * @param shipmentId 배송완료된 배송 id
     */
    @Override
    public void completeShipment(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId).orElseThrow(() -> new EntityNotFoundException("Shipment not found"));

        //배송완료 상태 변경
        shipment.setStatus(ShippingStatus.DELIVERED);
        //배송완료일 저장
        shipment.setDeliveredDate(LocalDateTime.now());
    }

    /**
     * 배송 상태 확인
     * @param orderItemId 주문 아이템 id
     * @return 배송 상태
     */
    @Override
    public ShippingStatus getShipmentStatus(Long orderItemId) {
        return shipmentRepository.findShippingStatusByOrderItemId(orderItemId);
    }
}
