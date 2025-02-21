package com.logi_manage.order_fulfillment_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    @GetMapping("/")
    public String test() {
        return "order-fulfillment-service";
    }
}
