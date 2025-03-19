package com.logi_manage.shipment_service.service;

import com.logi_manage.shipment_service.constant.ShippingStatus;
import com.logi_manage.shipment_service.dto.request.CreateShipmentRequestDto;
import com.logi_manage.shipment_service.dto.request.ShipmentFilterRequestDto;
import com.logi_manage.shipment_service.dto.request.UpdateShipmentRequestDto;
import com.logi_manage.shipment_service.dto.response.ShipmentDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipmentService {
    Long createShipment(CreateShipmentRequestDto createShipmentRequestDto);

    void updateShipment(Long shipmentId, UpdateShipmentRequestDto updateShipmentRequestDto);

    Page<ShipmentDetailResponseDto> getShipmentList(ShipmentFilterRequestDto filterRequestDto, Pageable pageable);

    ShipmentDetailResponseDto getShipment(Long shipmentId);

    void completeShipment(Long shipmentId);

    ShippingStatus getShipmentStatus(Long orderItemId);
}
