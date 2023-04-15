package com.adda.mistry.booking.addamistrybookingservice.dto;

import com.adda.mistry.booking.addamistrybookingservice.collection.CustomerProfile;
import com.adda.mistry.booking.addamistrybookingservice.collection.ProviderDetail;
import com.adda.mistry.booking.addamistrybookingservice.collection.Services;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerBillingRequest {


    private String customerId;


    private String providerId;

    private List<Services> serviceLists;
    private String status;

//    private String billingTime;

    private String grossAmount;
}
