package com.adda.mistry.booking.addamistrybookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private String bookingId; // pending, progress, completed
    private String message;

}
