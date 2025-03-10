package com.logi_manage.order_service.service;

import com.logi_manage.order_service.dto.request.CreateCustomerRequestDto;
import com.logi_manage.order_service.dto.request.UpdateCustomerRequestDto;
import com.logi_manage.order_service.entity.Customer;
import com.logi_manage.order_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;

    /**
     * 고객 신규 생성
     * @param createCustomerRequestDto 고객 생성 dto
     * @return 생성된 고객 id
     */
    @Override
    public Long createCustomer(CreateCustomerRequestDto createCustomerRequestDto) {
        Customer customer = Customer.builder()
                .name(createCustomerRequestDto.name())
                .email(createCustomerRequestDto.email())
                .phone(createCustomerRequestDto.phone())
                .build();
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer.getId();
    }

    /**
     * 고객 정보 수정
     * @param updateCustomerRequestDto 고객 수정 dto
     */
    @Override
    public void updateCustomer(Long customerId, UpdateCustomerRequestDto updateCustomerRequestDto) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        if (updateCustomerRequestDto.name() != null) {
            customer.setName(updateCustomerRequestDto.name());
        }
        if (updateCustomerRequestDto.email() != null) {
            customer.setEmail(updateCustomerRequestDto.email());
        }
        if (updateCustomerRequestDto.phone() != null) {
            customer.setPhone(updateCustomerRequestDto.phone());
        }
    }

    /**
     * 고객 삭제
     * @param customerId 삭제할 고객 id
     */
    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }
}
