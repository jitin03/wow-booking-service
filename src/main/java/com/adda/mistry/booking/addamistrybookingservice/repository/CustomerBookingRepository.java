package com.adda.mistry.booking.addamistrybookingservice.repository;

import com.adda.mistry.booking.addamistrybookingservice.collection.CustomerBooking;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerBookingRepository extends MongoRepository<CustomerBooking,String> {
    @Query("{'_id' : ?0}")
    CustomerBooking findByBookingId(Integer id);
}
