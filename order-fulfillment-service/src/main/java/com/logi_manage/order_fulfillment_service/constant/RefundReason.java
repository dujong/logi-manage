package com.logi_manage.order_fulfillment_service.constant;

public enum RefundReason {
    ORDER_CANCELED, //주문취소
    ITEM_NOT_DELIVERED, //상품 미배송
    DAMAGED_ITEM, //상품 불량
    PAYMENT_ERROR, //결제 오류
    CHANGE_OF_MIND, //단순 변심
    OTHER //기타
}
