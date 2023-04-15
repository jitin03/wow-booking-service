package com.adda.mistry.booking.addamistrybookingservice;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableMongoRepositories
@EnableSwagger2
@EnableFeignClients
@EnableCaching
public class AddaMistryBookingServiceApplication {
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizer()
	{
		return builder -> builder.serializerByType(ObjectId.class,new ToStringSerializer());
	}
	public static void main(String[] args) {
		SpringApplication.run(AddaMistryBookingServiceApplication.class, args);
	}

}
