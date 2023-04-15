package com.adda.mistry.booking.addamistrybookingservice.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfile implements Serializable {

    @Id
    private String id;

    private String name;

    private String emailaddress;

    private Long phonenumber;
    private Integer age;
    private String gender;

    private Address address;


}
