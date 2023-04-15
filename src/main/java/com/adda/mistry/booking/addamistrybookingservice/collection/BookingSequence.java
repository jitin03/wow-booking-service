package com.adda.mistry.booking.addamistrybookingservice.collection;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="booking_sequence")
public class BookingSequence {
    @Id
    private String id;

    private long seq;
}
