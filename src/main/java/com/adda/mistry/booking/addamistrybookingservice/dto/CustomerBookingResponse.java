package com.adda.mistry.booking.addamistrybookingservice.dto;

import com.adda.mistry.booking.addamistrybookingservice.collection.CustomerProfile;
import com.adda.mistry.booking.addamistrybookingservice.collection.ProviderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBookingResponse implements Serializable {
    private CustomerProfile customerId;

    private ProviderDetail providerId;

    private String bookingTime;

    private String status;
    private String serviceType;

    private String paymentMode;

    private String bookingAddress;
}
