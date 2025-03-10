package com.logi_manage.order_service.dto.request;

public record UpdateCustomerRequestDto(
        //고객명
        String name,
        //이메일
        String email,
        //연락처
        String phone
) {
}
