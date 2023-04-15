package com.adda.mistry.booking.addamistrybookingservice.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

    private String address1;
    private String address2;
    private String city;
    private String pincode;

    private String state;
}
