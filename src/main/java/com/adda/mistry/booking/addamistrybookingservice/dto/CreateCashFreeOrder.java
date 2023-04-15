package com.adda.mistry.booking.addamistrybookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCashFreeOrder {

    private CustomerDetails customer_details;
    private double order_amount;
    private String order_currency;
    private String order_note;
}
