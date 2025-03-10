package com.logi_manage.order_service.service;

import com.logi_manage.order_service.dto.request.CreateCustomerRequestDto;
import com.logi_manage.order_service.dto.request.UpdateCustomerRequestDto;

public interface CustomerService {
    Long createCustomer(CreateCustomerRequestDto createCustomerRequestDto);

    void updateCustomer(Long customerId, UpdateCustomerRequestDto updateCustomerRequestDto);

    void deleteCustomer(Long customerId);
}
