package com.logi_manage.order_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequestDto(

        //고객명
        @NotBlank
        String name,
        //이메일
        @NotBlank
        String email,
        //연락처
        @NotBlank
        String phone
) {
}
