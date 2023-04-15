package com.adda.mistry.booking.addamistrybookingservice.controller;

import com.adda.mistry.booking.addamistrybookingservice.dto.*;
import com.adda.mistry.booking.addamistrybookingservice.exceptions.BookingServiceException;
import com.adda.mistry.booking.addamistrybookingservice.publisher.OrderProducer;
import com.adda.mistry.booking.addamistrybookingservice.service.APIClient;
import com.adda.mistry.booking.addamistrybookingservice.service.CustomerBillingService;
import com.adda.mistry.booking.addamistrybookingservice.service.CustomerBookingService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
public class CustomerBilling {

    @Autowired
    CustomerBillingService customerBillingService;
    @Autowired
    OrderProducer orderProducer;
    @Autowired
    APIClient apiClient;

    @PostMapping("/{customerBookingId}/billing")
    public ResponseEntity<String> generateCustomerBill(@RequestBody CustomerBillingRequest customerBillingRequest,@PathVariable String customerBookingId){
        String billingId = customerBillingService.generateCustomerBill(customerBillingRequest,customerBookingId);
        OrderEvent event = new OrderEvent();
        event.setBookingId(customerBookingId);
        event.setMessage("Bill has been generated now");
        orderProducer.sendMessage(event);
        return new ResponseEntity<>(billingId, HttpStatus.CREATED);
    }

    @PutMapping ("/{customerBookingId}/billing")
    public ResponseEntity<String> updateCustomerBill(@RequestBody CustomerBillingRequest customerBillingRequest,@PathVariable String customerBookingId) throws BookingServiceException {
        String billingId = customerBillingService.updateCustomerBill(customerBillingRequest,customerBookingId);
        return new ResponseEntity<>(billingId, HttpStatus.CREATED);
    }

    @PutMapping ("/{customerBookingId}/billing/status")
    public ResponseEntity<List<Document>> updateCustomerBillStatus(@RequestBody BookingStatusRequest bookingStatusRequest, @PathVariable String customerBillId) throws BookingServiceException {
        List<Document> customerBill = customerBillingService.updateCustomerBillStatus(bookingStatusRequest,customerBillId);
        return new ResponseEntity<>(customerBill, HttpStatus.CREATED);
    }

    @GetMapping("/{customerBookingId}/billing")
    public ResponseEntity<List<Document>> getCustomerBillingByBookingId(@PathVariable String customerBookingId ){
        List<Document> customerBillingRequest= customerBillingService.getCustomerBillingByBookingId(customerBookingId);
        return new ResponseEntity<>(customerBillingRequest, HttpStatus.OK);
    }

    @PostMapping("/create-order")
    public ResponseEntity<CreateOrderResponse> createCashFreeOrderforCustomer(@RequestBody CreateCashFreeOrder createCashFreeOrder){
        CreateOrderResponse orderResponse = apiClient.createCashFreeOrder(createCashFreeOrder);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}
