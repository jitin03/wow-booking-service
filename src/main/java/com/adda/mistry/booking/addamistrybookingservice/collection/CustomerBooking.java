package com.adda.mistry.booking.addamistrybookingservice.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="customer_booking")
public class CustomerBooking {
    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";
    @Id
    private long id;

    @DocumentReference
    private CustomerProfile customerId;

    @DocumentReference
    private ProviderDetail providerId;

//    private String serviceType;
    private String status;
    private String paymentMode;
    private String bookingTime;
    private String bookingAddress;

    private Instant createdTime;

    private String grossAmount;

    private List<Services> serviceLists;

}
