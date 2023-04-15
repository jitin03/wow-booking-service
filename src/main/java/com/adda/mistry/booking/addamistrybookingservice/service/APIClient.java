package com.adda.mistry.booking.addamistrybookingservice.service;

import com.adda.mistry.booking.addamistrybookingservice.config.CustomFeignClientConfiguration;
import com.adda.mistry.booking.addamistrybookingservice.dto.CreateCashFreeOrder;
import com.adda.mistry.booking.addamistrybookingservice.dto.CreateOrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://sandbox.cashfree.com",value = "CASHFREEGATEWAY",configuration = CustomFeignClientConfiguration.class)
public interface APIClient {

    @PostMapping("/pg/orders")
    CreateOrderResponse createCashFreeOrder(@RequestBody CreateCashFreeOrder createCashFreeOrder);
}
