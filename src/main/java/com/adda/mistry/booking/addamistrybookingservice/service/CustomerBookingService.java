package com.adda.mistry.booking.addamistrybookingservice.service;

import com.adda.mistry.booking.addamistrybookingservice.dto.BookingPaymentRequest;
import com.adda.mistry.booking.addamistrybookingservice.dto.BookingStatusRequest;
import com.adda.mistry.booking.addamistrybookingservice.dto.CustomerBookingRequest;
import com.adda.mistry.booking.addamistrybookingservice.dto.CustomerBookingResponse;
import com.adda.mistry.booking.addamistrybookingservice.exceptions.BookingServiceException;
import com.adda.mistry.booking.addamistrybookingservice.exceptions.ResourceNotFoundException;
import org.bson.Document;

import java.util.List;

public interface CustomerBookingService {
    Long saveCustomerBooking(CustomerBookingRequest customerBookingRequest);


    List<Document> getAllBooking();

    List<Document> getAllBookingByCustomerId(String customerId);

    List<Document> getAllBookingByProviderId(String providerId) throws ResourceNotFoundException;

    CustomerBookingResponse updateBookingStatus(String id, BookingStatusRequest bookingStatusRequest) throws ResourceNotFoundException, BookingServiceException;

    List<Document> getBookingById(String bookingId);

    CustomerBookingResponse updateBookingPaymentMode(String id, BookingPaymentRequest bookingPaymentRequest) throws BookingServiceException;
}
