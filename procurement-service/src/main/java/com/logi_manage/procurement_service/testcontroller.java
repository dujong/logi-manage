package com.logi_manage.procurement_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testcontroller {
    @GetMapping("/")
    public String test() {
        return "procurement-service";
    }
}
