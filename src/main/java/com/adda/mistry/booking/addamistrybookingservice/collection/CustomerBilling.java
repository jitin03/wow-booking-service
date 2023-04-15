package com.adda.mistry.booking.addamistrybookingservice.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="customer_billing")
public class CustomerBilling {


    @Id
    private String id;

    @DocumentReference
    private CustomerProfile customerId;

    @DocumentReference
    private CustomerBooking bookingId;

    @DocumentReference
    private ProviderDetail providerId;

    private List<Services> serviceLists;
    private String status;
//    private String billingTime;

    private String grossAmount;
}
