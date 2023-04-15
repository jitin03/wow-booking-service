package com.adda.mistry.booking.addamistrybookingservice.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Services {

    private String name;

    private List<SubCategories> subCategories;

    private double price;

}
