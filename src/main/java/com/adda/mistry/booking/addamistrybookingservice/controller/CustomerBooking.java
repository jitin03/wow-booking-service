package com.adda.mistry.booking.addamistrybookingservice.controller;

import com.adda.mistry.booking.addamistrybookingservice.dto.*;
import com.adda.mistry.booking.addamistrybookingservice.exceptions.BookingServiceException;
import com.adda.mistry.booking.addamistrybookingservice.exceptions.ResourceNotFoundException;
import com.adda.mistry.booking.addamistrybookingservice.publisher.OrderProducer;
import com.adda.mistry.booking.addamistrybookingservice.service.CustomerBookingService;
import com.adda.mistry.booking.addamistrybookingservice.service.NotificationsClient;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.bson.Document;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
@Slf4j
public class CustomerBooking {

    @Autowired
    OrderProducer orderProducer;

    @Autowired
    CustomerBookingService customerBookingService;

    @Autowired
    NotificationsClient notificationsClient;
    public CustomerBooking(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping
    public  ResponseEntity<Long> saveCustomerBooking(@RequestBody CustomerBookingRequest customerBookingRequest){

        Long bookingId = customerBookingService.saveCustomerBooking(customerBookingRequest);

        OrderEvent event = new OrderEvent();
        event.setBookingId(bookingId.toString());
        event.setMessage("New Booking is submitted by customer");
        orderProducer.sendMessage(event);
        return new ResponseEntity<>(bookingId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllBooking(){
        List<Document> customerBookingResponses= customerBookingService.getAllBooking();
        return new ResponseEntity<>(customerBookingResponses, HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<List<Document>> getBookingById(@PathVariable String bookingId){
        List<Document> customerBookingResponses= customerBookingService.getBookingById(bookingId);
        return new ResponseEntity<>(customerBookingResponses, HttpStatus.OK);
    }

    @GetMapping("/customer")
    public ResponseEntity<List<Document>> getAllBookingByCustomerId(@RequestParam String customerId ){
        List<Document> customerBookingResponses= customerBookingService.getAllBookingByCustomerId(customerId);
        return new ResponseEntity<>(customerBookingResponses, HttpStatus.OK);
    }

    @GetMapping("/provider")
    public ResponseEntity<List<Document>> getAllBookingByProviderId(@RequestParam String providerId ) throws ResourceNotFoundException {
        List<Document> customerBookingResponses= customerBookingService.getAllBookingByProviderId(providerId);
        return new ResponseEntity<>(customerBookingResponses, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CustomerBookingResponse> updateBookingStatus(@PathVariable String id, @RequestBody BookingStatusRequest bookingStatusRequest) throws BookingServiceException, ResourceNotFoundException {
        CustomerBookingResponse customerBookingStatusResponse = customerBookingService.updateBookingStatus(id,bookingStatusRequest);
        OrderEvent event = new OrderEvent();
        event.setBookingId(id);
        event.setMessage("Your Booking is "+bookingStatusRequest.getStatus()+" now");
        orderProducer.sendMessage(event);
        return new ResponseEntity<>(customerBookingStatusResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/mode")
    public ResponseEntity<CustomerBookingResponse> updateBookingPaymentMode(@PathVariable String id, @RequestBody BookingPaymentRequest bookingPaymentRequest) throws BookingServiceException, ResourceNotFoundException {
        CustomerBookingResponse customerBookingStatusResponse = customerBookingService.updateBookingPaymentMode(id,bookingPaymentRequest);
        return new ResponseEntity<>(customerBookingStatusResponse, HttpStatus.OK);
    }

}
