package com.logi_manage.order_fulfillment_service.service;

import com.logi_manage.order_fulfillment_service.constant.OrderStatus;
import com.logi_manage.order_fulfillment_service.constant.StockInStatus;
import com.logi_manage.order_fulfillment_service.dto.request.*;
import com.logi_manage.order_fulfillment_service.dto.response.InventoryDetailResponseDto;
import com.logi_manage.order_fulfillment_service.dto.response.OrderDetailResponseDto;
import com.logi_manage.order_fulfillment_service.dto.response.ProductDetailResponseDto;
import com.logi_manage.order_fulfillment_service.dto.response.WarehouseDetailResponseDto;
import com.logi_manage.order_fulfillment_service.entity.OrderFulfillment;
import com.logi_manage.order_fulfillment_service.entity.StockIn;
import com.logi_manage.order_fulfillment_service.repository.OrderFulfillmentRepository;
import com.logi_manage.order_fulfillment_service.repository.StockInRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class OrderFulfillmentServiceImpl implements OrderFulfillmentService{





}
