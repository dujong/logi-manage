package com.logi_manage.order_fulfillment_service.constant;

public enum ShippingStatus {
    PENDING,         // 대기 상태
    SHIPPED,         // 배송 중
    DELIVERED,       // 배송 완료
    RETURNED,        // 반품 완료
    CANCELED,        // 취소
}
