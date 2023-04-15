package com.adda.mistry.booking.addamistrybookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponse implements Serializable {

    private String payment_session_id;
    private String order_status;

    private CustomerDetails customer_details;

    private String order_id;

    private double cf_order_id;

}
