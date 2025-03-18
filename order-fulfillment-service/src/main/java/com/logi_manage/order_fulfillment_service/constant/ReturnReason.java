package com.logi_manage.order_fulfillment_service.constant;

public enum ReturnReason {
    DAMAGED, //상품 불량
    WRONG_ITEM, //상품 오류
    CHANGE_OF_MIN, //단순 변심
    WRONG_DELIVERY, //오배송
    OTHER //기타
}
