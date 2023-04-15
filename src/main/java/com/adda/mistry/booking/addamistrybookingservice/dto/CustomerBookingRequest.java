package com.adda.mistry.booking.addamistrybookingservice.dto;

import com.adda.mistry.booking.addamistrybookingservice.collection.CustomerProfile;
import com.adda.mistry.booking.addamistrybookingservice.collection.Services;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBookingRequest {

    private String customerId;

    private String providerId;

    private String bookingTime;

//    private String serviceType;

    private String status;
    private String paymentMode;

    private String bookingAddress;
    private String grossAmount;
    private List<Services> serviceLists;

}
