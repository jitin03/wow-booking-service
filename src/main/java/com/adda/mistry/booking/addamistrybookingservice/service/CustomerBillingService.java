package com.adda.mistry.booking.addamistrybookingservice.service;

import com.adda.mistry.booking.addamistrybookingservice.dto.BookingStatusRequest;
import com.adda.mistry.booking.addamistrybookingservice.dto.CustomerBillingRequest;
import com.adda.mistry.booking.addamistrybookingservice.exceptions.BookingServiceException;
import org.bson.Document;

import java.util.List;

public interface CustomerBillingService {
    public String generateCustomerBill(CustomerBillingRequest customerBillingRequest,String customerBookingId);

    List<Document> getCustomerBillingByBookingId(String bookingId);

    String updateCustomerBill(CustomerBillingRequest customerBillingRequest, String customerBookingId) throws BookingServiceException;

    List<Document> updateCustomerBillStatus(BookingStatusRequest bookingStatusRequest, String customerBillId) throws BookingServiceException;
}
