package com.adda.mistry.booking.addamistrybookingservice.service;


import com.adda.mistry.booking.addamistrybookingservice.collection.*;
import com.adda.mistry.booking.addamistrybookingservice.dto.BookingStatusRequest;
import com.adda.mistry.booking.addamistrybookingservice.dto.CustomerBillingRequest;
import com.adda.mistry.booking.addamistrybookingservice.exceptions.BookingServiceException;
import com.adda.mistry.booking.addamistrybookingservice.exceptions.ResourceNotFoundException;
import com.adda.mistry.booking.addamistrybookingservice.repository.CustomerBillingRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerBillingServiceImpl implements CustomerBillingService {


    @Autowired
    CustomerBillingRepository customerBillingRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    @CacheEvict(value = "billing",allEntries = true)
    public String generateCustomerBill(CustomerBillingRequest customerBillingRequest, String customerBookingId) {

        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setId(customerBillingRequest.getCustomerId());

        ProviderDetail providerDetail = new ProviderDetail();
        providerDetail.setId(customerBillingRequest.getProviderId());

        CustomerBooking customerBooking = new CustomerBooking();
        customerBooking.setId(Long.parseLong(customerBookingId));

        List<Services> services = new ArrayList<Services>();
        List<SubCategories> subCategories = new ArrayList<SubCategories>();

        Services service = new Services();
        SubCategories subCategory = new SubCategories();
        for (int i = 0; i < customerBillingRequest.getServiceLists().size(); i++) {
            service.setName(customerBillingRequest.getServiceLists().get(i).getName());
            service.setPrice(customerBillingRequest.getServiceLists().get(i).getPrice());

            for (int j = 0; j < customerBillingRequest.getServiceLists().get(i).getSubCategories().size(); j++) {

                subCategories.add(customerBillingRequest.getServiceLists().get(i).getSubCategories().get(j));

            }
            System.out.println(subCategories);
            service.setSubCategories(subCategories);

            services.add(service);

        }

        CustomerBilling customerBilling = CustomerBilling.builder().
                customerId(customerProfile).providerId(providerDetail).
                status(customerBillingRequest.getStatus()).bookingId(customerBooking)
                .serviceLists(services).grossAmount(customerBillingRequest.getGrossAmount()).
                build();
        customerBillingRepository.save(customerBilling);
        return customerBilling.getId();
    }

    @Override
    @Cacheable(value = "billing")
    public List<Document> getCustomerBillingByBookingId(String bookingId) {
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("customerProfile")
                .localField("customerId")
                .foreignField("_id")
                .as("customerId");

//        AggregationOperation lookup = Aggregation.lookup("providers_detail", "providerId", "_id", "providerId");
        // exclude fields from the lookup table


        LookupOperation providerLookUp = LookupOperation.newLookup()
                .from("providers_detail")
                .localField("providerId")
                .foreignField("_id")
                .as("providerId");

        LookupOperation bookingLookUp = LookupOperation.newLookup()
                .from("customer_booking")
                .localField("bookingId")
                .foreignField("_id")
                .as("bookingId");


        System.out.println(lookupOperation);
        AggregationOperation match = Aggregation.match(Criteria.where("bookingId").is(Integer.parseInt(bookingId)));

        ProjectionOperation projectionOperation
                = Aggregation.project()
                .andExpression("_id").as("billingId").andExclude("_id")
                .andInclude("customerId").andInclude("paymentMode").andInclude("status").andInclude("bookingTime").andInclude("serviceLists").andInclude("grossAmount")
                .andExpression("providerId._id").as("providerId").andExpression("providerId.name").as("providerName").andExpression("customerId._id").as("customerId").andExpression("customerId.phonenumber").as("customerPhoneNo").
                andExpression("customerId.emailaddress").as("customerEmail");


        Aggregation aggregation = Aggregation.newAggregation(match, lookupOperation, providerLookUp, bookingLookUp, projectionOperation);

        System.out.println(aggregation);

        List<Document> results = mongoTemplate.aggregate(aggregation, "customer_billing", Document.class).getMappedResults();
        return results;
    }

    @Override
    @CacheEvict(value = "billing",allEntries = true)
    public String updateCustomerBill(CustomerBillingRequest customerBillingRequest, String bookingId) throws BookingServiceException {
        CustomerBilling customerBilling;
        try {
            customerBilling = customerBillingRepository.findByBookingId(Integer.parseInt(bookingId));
            if (customerBilling == null) {
                throw new ResourceNotFoundException("Billing  not found");
            }


            List<Services> services = new ArrayList<>();
            List<SubCategories> subCategories = new ArrayList<>();

            Services service = new Services();

            for (int i = 0; i < customerBillingRequest.getServiceLists().size(); i++) {
                service.setName(customerBillingRequest.getServiceLists().get(i).getName());
                service.setPrice(customerBillingRequest.getServiceLists().get(i).getPrice());

                for (int j = 0; j < customerBillingRequest.getServiceLists().get(i).getSubCategories().size(); j++) {

                    subCategories.add(customerBillingRequest.getServiceLists().get(i).getSubCategories().get(j));

                }
                System.out.println(subCategories);
                service.setSubCategories(subCategories);


                services.add(service);

            }

            customerBilling.setServiceLists(services);
            customerBilling.setGrossAmount(customerBillingRequest.getGrossAmount());
            customerBilling.setStatus(customerBillingRequest.getStatus());
            customerBillingRepository.save(customerBilling);
            return customerBilling.getId();
        } catch (Exception e) {
            throw new BookingServiceException("Internal Server Exception" + e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "billing",allEntries = true)
    public List<Document> updateCustomerBillStatus(BookingStatusRequest bookingStatusRequest, String customerBillId) throws BookingServiceException {
        CustomerBilling customerBilling;
        try {
            customerBilling = customerBillingRepository.findByBookingId(Integer.parseInt(customerBillId));
            if (customerBilling == null) {
                throw new ResourceNotFoundException("Customer Bill has not found");
            }
            customerBilling.setStatus(bookingStatusRequest.getStatus());

            CustomerBilling updateCustomerBooking = customerBillingRepository.save(customerBilling);
            LookupOperation lookupOperation = LookupOperation.newLookup()
                    .from("customerProfile")
                    .localField("customerId")
                    .foreignField("_id")
                    .as("customerId");

//        AggregationOperation lookup = Aggregation.lookup("providers_detail", "providerId", "_id", "providerId");
            // exclude fields from the lookup table


            LookupOperation providerLookUp = LookupOperation.newLookup()
                    .from("providers_detail")
                    .localField("providerId")
                    .foreignField("_id")
                    .as("providerId");

            LookupOperation bookingLookUp = LookupOperation.newLookup()
                    .from("customer_booking")
                    .localField("bookingId")
                    .foreignField("_id")
                    .as("bookingId");


            System.out.println(lookupOperation);
            AggregationOperation match = Aggregation.match(Criteria.where("bookingId").is(Integer.parseInt(customerBillId)));

            ProjectionOperation projectionOperation
                    = Aggregation.project()
                    .andExpression("_id").as("billingId").andExclude("_id")
                    .andInclude("customerId").andInclude("paymentMode").andInclude("status").andInclude("bookingTime").andInclude("serviceLists").andInclude("grossAmount")
                    .andExpression("providerId._id").as("providerId").andExpression("providerId.name").as("providerName").andExpression("customerId._id").as("customerId");


            Aggregation aggregation = Aggregation.newAggregation(match, lookupOperation, providerLookUp, bookingLookUp, projectionOperation);

            System.out.println(aggregation);

            List<Document> results = mongoTemplate.aggregate(aggregation, "customer_billing", Document.class).getMappedResults();
            return results;
        } catch (Exception e) {
            throw new BookingServiceException("Internal Server Exception"+e.getMessage());
        }
    }
}
