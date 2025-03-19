package com.logi_manage.shipment_service.controller;

import com.logi_manage.shipment_service.constant.ShippingStatus;
import com.logi_manage.shipment_service.dto.request.CreateShipmentRequestDto;
import com.logi_manage.shipment_service.dto.request.ShipmentFilterRequestDto;
import com.logi_manage.shipment_service.dto.request.UpdateShipmentRequestDto;
import com.logi_manage.shipment_service.dto.response.ShipmentDetailResponseDto;
import com.logi_manage.shipment_service.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/shipments")
public class ShipmentController {
    private final ShipmentService shipmentService;

    /**
     * list up
     * - [O] 배송 요청 생성
     * - [O] 배송 상태 변경
     * - [O] 배송 목록 조회
     * - [O] 배송 상세 조회
     * - [O] 배송 완료 확인
     * - [O] 배송 상태 조회
     */

    /**
     * 배송 요청 생성
     *
     * @param createShipmentRequestDto 배송 생성 dto
     * @return 생성된 배송 id
     */
    @PostMapping
    public ResponseEntity<Long> createShipment(@RequestBody CreateShipmentRequestDto createShipmentRequestDto) {
        Long shipmentId = shipmentService.createShipment(createShipmentRequestDto);
        return ResponseEntity.ok(shipmentId);
    }

    /**
     * 배송 상태 업데이트
     *
     * @param shipmentId               업데이트 할 배송 id
     * @param updateShipmentRequestDto 배송 업데이트 dto
     */
    @PatchMapping("/{shipmentId}")
    public ResponseEntity<Void> updateShipment(@PathVariable Long shipmentId, @RequestBody UpdateShipmentRequestDto updateShipmentRequestDto) {
        shipmentService.updateShipment(shipmentId, updateShipmentRequestDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * 배송 리스트 조회
     *
     * @param filterRequestDto 필터링 dto
     * @param pageable         페이징
     * @return 배송 list
     */
    @GetMapping
    public ResponseEntity<Page<?>> getShipmentList(ShipmentFilterRequestDto filterRequestDto, Pageable pageable) {
        Page<ShipmentDetailResponseDto> shipmentList = shipmentService.getShipmentList(filterRequestDto, pageable);
        return ResponseEntity.ok(shipmentList);
    }

    /**
     * 배송 상세 조회
     *
     * @param shipmentId 조회할 배송 id
     * @return 배송 상세 info
     */
    @GetMapping("/{shipmentId")
    public ResponseEntity<ShipmentDetailResponseDto> getShipment(@PathVariable Long shipmentId) {
        ShipmentDetailResponseDto shipment = shipmentService.getShipment(shipmentId);
        return ResponseEntity.ok(shipment);
    }

    /**
     * 배송 완료
     * @param shipmentId 배송완료된 배송 id
     */
    @PostMapping("/{shipmentId}/complete")
    public ResponseEntity<Void> completeShipment(@PathVariable Long shipmentId) {
        shipmentService.completeShipment(shipmentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 배송 상태 확인
     * @param orderItemId 주문 아이템 id
     * @return 배송 상태
     */
    @GetMapping("/{orderItemId}/status")
    public ResponseEntity<ShippingStatus> getShipmentStatus(@PathVariable Long orderItemId) {
        ShippingStatus shipmentStatus = shipmentService.getShipmentStatus(orderItemId);
        return ResponseEntity.ok(shipmentStatus);
    }
}
