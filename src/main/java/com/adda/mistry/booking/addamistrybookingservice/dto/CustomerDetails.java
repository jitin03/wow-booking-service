package com.adda.mistry.booking.addamistrybookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetails implements Serializable {
    public String customer_id;
    public String customer_email;
    public String customer_phone;
}
