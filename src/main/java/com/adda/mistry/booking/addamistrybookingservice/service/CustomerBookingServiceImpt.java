package com.adda.mistry.booking.addamistrybookingservice.service;

import com.adda.mistry.booking.addamistrybookingservice.collection.*;
import com.adda.mistry.booking.addamistrybookingservice.dto.BookingPaymentRequest;
import com.adda.mistry.booking.addamistrybookingservice.dto.BookingStatusRequest;
import com.adda.mistry.booking.addamistrybookingservice.dto.CustomerBookingRequest;
import com.adda.mistry.booking.addamistrybookingservice.dto.CustomerBookingResponse;
import com.adda.mistry.booking.addamistrybookingservice.exceptions.BookingServiceException;
import com.adda.mistry.booking.addamistrybookingservice.exceptions.ResourceNotFoundException;
import com.adda.mistry.booking.addamistrybookingservice.repository.CustomerBillingRepository;
import com.adda.mistry.booking.addamistrybookingservice.repository.CustomerBookingRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerBookingServiceImpt implements CustomerBookingService {
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;
    @Autowired
    CustomerBookingRepository customerBookingRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    CustomerBillingRepository customerBillingRepository;


    @Override
    @CacheEvict(value = "booking",allEntries = true)
    public Long saveCustomerBooking(CustomerBookingRequest customerBookingRequest) {

        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setId(customerBookingRequest.getCustomerId());

        ProviderDetail providerDetail = new ProviderDetail();
        providerDetail.setId(customerBookingRequest.getProviderId());


        List<Services> services = new ArrayList<Services>();
        List<SubCategories> subCategories = new ArrayList<SubCategories>();



        Services service = new Services();
        SubCategories subCategory = new SubCategories();



        for (int i = 0; i < customerBookingRequest.getServiceLists().size(); i++) {
            service.setName(customerBookingRequest.getServiceLists().get(i).getName());
            service.setPrice(customerBookingRequest.getServiceLists().get(i).getPrice());

            for (int j = 0; j < customerBookingRequest.getServiceLists().get(i).getSubCategories().size(); j++) {

                subCategories.add(customerBookingRequest.getServiceLists().get(i).getSubCategories().get(j));

            }
            System.out.println(subCategories);
            service.setSubCategories(subCategories);

            services.add(service);

        }


        CustomerBooking customerBooking = CustomerBooking.builder().
                id(sequenceGeneratorService.getSequenceNumber(CustomerBooking.SEQUENCE_NAME)).
                customerId(customerProfile).grossAmount(customerBookingRequest.getGrossAmount()).serviceLists(services)
                .bookingTime(customerBookingRequest.getBookingTime()).paymentMode(customerBookingRequest.getPaymentMode())
                .providerId(providerDetail).status(customerBookingRequest.getStatus()).bookingAddress(customerBookingRequest.getBookingAddress()).
                createdTime(Instant.now()).
                build();

        customerBookingRepository.save(customerBooking);
        return customerBooking.getId();
    }

    @Override
    @Cacheable(value = "booking")
    public List<Document> getAllBooking() {

        List<CustomerBookingResponse> customerBookingResponses = new ArrayList<>();
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("customerProfile")
                .localField("customerId")
                .foreignField("_id")
                .as("customerId");

        LookupOperation providerLookUp = LookupOperation.newLookup()
                .from("providers_detail")
                .localField("providerId")
                .foreignField("_id")
                .as("providerId");
        SortOperation sortOperation
                = Aggregation.sort(Sort.Direction.DESC, "createdTime");
//        System.out.println(lookupOperation);
//        AggregationOperation match = Aggregation.match(Criteria.where("_id").is("63e348d6ff67d36f08812792"));
//        ProjectionOperation projectStage = Aggregation.project("_id", "customerId");
        ProjectionOperation projectionOperation
                = Aggregation.project()
               .andExpression("_id").as("bookingId").andExclude("_id")
                .andInclude("customerId").andInclude("paymentMode").andInclude("status").andInclude("bookingTime")
                .andInclude("providerId").andInclude("serviceType").andInclude("bookingAddress");



        Aggregation aggregation = Aggregation.newAggregation(sortOperation,lookupOperation,providerLookUp,projectionOperation);


        List<Document> results = mongoTemplate.aggregate(aggregation, "customer_booking", Document.class).getMappedResults();



        return results;
    }

    @Override
    @Cacheable(value = "booking",key="#customerId")
    public List<Document> getAllBookingByCustomerId(String customerId) {
        List<CustomerBookingResponse> customerBookingResponses = new ArrayList<>();
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



        SortOperation sortOperation
                = Aggregation.sort(Sort.Direction.DESC, "createdTime");
        AggregationOperation match = Aggregation.match(Criteria.where("customerId").is(new ObjectId(customerId)));

        ProjectionOperation projectionOperation
                = Aggregation.project()
                .andExpression("_id").as("bookingId").andExclude("_id")
                .andInclude("customerId").andInclude("paymentMode").andInclude("status").andInclude("bookingTime").andInclude("bookingAddress")
                .andExpression("providerId._id").as("providerId").andExpression("providerId.name").as("providerName").andInclude("serviceType");


        Aggregation aggregation = Aggregation.newAggregation(match,sortOperation,lookupOperation,providerLookUp,projectionOperation);

        System.out.println(aggregation);

        List<Document> results = mongoTemplate.aggregate(aggregation, "customer_booking", Document.class).getMappedResults();

        return results;
    }

    @Override
    @Cacheable(value = "booking",key="#providerId")
    public List<Document> getAllBookingByProviderId(String providerId) throws ResourceNotFoundException {
        List<CustomerBookingResponse> customerBookingResponses = new ArrayList<>();
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("customerProfile")
                .localField("customerId")
                .foreignField("_id")
                .as("customerId");

        LookupOperation providerLookUp = LookupOperation.newLookup()
                .from("providers_detail")
                .localField("providerId")
                .foreignField("_id")
                .as("providerId");
        SortOperation sortOperation
                = Aggregation.sort(Sort.Direction.DESC, "createdTime");
        AggregationOperation match = Aggregation.match(
                Criteria.where("providerId").is(new ObjectId(providerId)));

        ProjectionOperation projectionOperation
                = Aggregation.project()
                .andExpression("_id").as("bookingId").andExclude("_id")
                .andInclude("customerId").andInclude("paymentMode").andInclude("status").andInclude("bookingTime")
                .andInclude("providerId").andInclude("serviceType").andInclude("bookingAddress");

        Aggregation aggregation = Aggregation.newAggregation(match,sortOperation,lookupOperation,providerLookUp,projectionOperation);


        List<Document> results = mongoTemplate.aggregate(aggregation, "customer_booking", Document.class).getMappedResults();
        if (results.isEmpty()) {
            throw new ResourceNotFoundException("Booking not found");
        }
        return results;
    }

    @Override
    @CacheEvict(value = "booking",allEntries = true)
    public CustomerBookingResponse updateBookingStatus(String id, BookingStatusRequest bookingStatusRequest) throws ResourceNotFoundException, BookingServiceException {

        CustomerBooking customerBooking;
        try {
            customerBooking = customerBookingRepository.findByBookingId(Integer.parseInt(id));
            if (customerBooking == null) {
                throw new ResourceNotFoundException("Booking  not found");
            }
            customerBooking.setStatus(bookingStatusRequest.getStatus());

            CustomerBooking updateCustomerBooking = customerBookingRepository.save(customerBooking);
            CustomerBookingResponse customerBookingResponse = new CustomerBookingResponse();
            BeanUtils.copyProperties(updateCustomerBooking, customerBookingResponse);
            //Update the billing status to Payment Done
            if(bookingStatusRequest.getStatus().equals("Done")){

                CustomerBilling customerBilling = customerBillingRepository.findByBookingId(Integer.parseInt(id));
                if (customerBilling == null) {
                    throw new ResourceNotFoundException("Customer Bill has not found");
                }
                customerBilling.setStatus("Paid");

                CustomerBilling updateCustomerBilling = customerBillingRepository.save(customerBilling);
            }
            return  customerBookingResponse;
        } catch (Exception e) {
            throw new BookingServiceException("Internal Server Exception"+e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "booking",key="#bookingId")
    public List<Document> getBookingById(String bookingId) {
        List<CustomerBookingResponse> customerBookingResponses = new ArrayList<>();
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("customerProfile")
                .localField("customerId")
                .foreignField("_id")
                .as("customerId");

        LookupOperation providerLookUp = LookupOperation.newLookup()
                .from("providers_detail")
                .localField("providerId")
                .foreignField("_id")
                .as("providerId");
//        System.out.println(lookupOperation);
        AggregationOperation match = Aggregation.match(Criteria.where("_id").is(Integer.parseInt(bookingId)));
//        ProjectionOperation projectStage = Aggregation.project("_id", "customerId");
        ProjectionOperation projectionOperation
                = Aggregation.project()
                .andExpression("_id").as("bookingId").andExclude("_id")
                .andInclude("customerId").andInclude("paymentMode").andInclude("status").andInclude("bookingTime")
                .andInclude("providerId").andInclude("serviceType").andInclude("bookingAddress");



        Aggregation aggregation = Aggregation.newAggregation(match,lookupOperation,providerLookUp,projectionOperation);


        List<Document> results = mongoTemplate.aggregate(aggregation, "customer_booking", Document.class).getMappedResults();



        return results;
    }

    @Override
    @CacheEvict(value = "booking",allEntries = true)
    public CustomerBookingResponse updateBookingPaymentMode(String id, BookingPaymentRequest bookingPaymentRequest) throws BookingServiceException {
        CustomerBooking customerBooking;
        try {
            customerBooking = customerBookingRepository.findByBookingId(Integer.parseInt(id));
            if (customerBooking == null) {
                throw new ResourceNotFoundException("Booking  not found");
            }
            customerBooking.setPaymentMode(bookingPaymentRequest.getPaymentMode());

            CustomerBooking updateCustomerBooking = customerBookingRepository.save(customerBooking);
            CustomerBookingResponse customerBookingResponse = new CustomerBookingResponse();
            BeanUtils.copyProperties(updateCustomerBooking, customerBookingResponse);
            //Update the billing status to Payment Done
//            if(bookingStatusRequest.getStatus().equals("Done")){
//
//                CustomerBilling customerBilling = customerBillingRepository.findByBookingId(Integer.parseInt(id));
//                if (customerBilling == null) {
//                    throw new ResourceNotFoundException("Customer Bill has not found");
//                }
//                customerBilling.setStatus("Paid");
//
//                CustomerBilling updateCustomerBilling = customerBillingRepository.save(customerBilling);
//            }
            return  customerBookingResponse;
        } catch (Exception e) {
            throw new BookingServiceException("Internal Server Exception"+e.getMessage());
        }
    }


}
