package com.zachmj.weather.infra.location;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

@Configuration
class IpLocationApiConfig {

    @Bean
    IpLocationApi ipLocationApi(
            @Value("${ip-api.url}") String baseUrl,
            Retrofit.Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .build()
                .create(IpLocationApi.class);
    }
}
