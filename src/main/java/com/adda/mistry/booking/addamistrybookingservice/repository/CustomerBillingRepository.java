package com.adda.mistry.booking.addamistrybookingservice.repository;

import com.adda.mistry.booking.addamistrybookingservice.collection.CustomerBilling;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerBillingRepository extends MongoRepository<CustomerBilling,String> {

    CustomerBilling findByBookingId(int parseInt);
}
