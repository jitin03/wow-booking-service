package com.adda.mistry.booking.addamistrybookingservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomFeignClientConfiguration {

    @Bean
    public RequestInterceptor customRequestInterceptor() {
        return new CustomRequestInterceptor();
    }

    public static class CustomRequestInterceptor implements RequestInterceptor {



        @Override
        public void apply(RequestTemplate requestTemplate) {
            requestTemplate.header("x-api-version", "2022-09-01");
            requestTemplate.header("x-client-id", "TEST341760df97eda8b9f315669a90067143");
            requestTemplate.header("x-client-secret", "TEST4df8e4a4d77a9e74a5940ba6083261d64e403932");
        }
    }
}
