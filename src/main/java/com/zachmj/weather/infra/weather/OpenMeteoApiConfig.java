package com.zachmj.weather.infra.weather;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

@Configuration
class OpenMeteoApiConfig {

    @Bean
    OpenMeteoApi OpenMeteoApi(
            @Value("${open-meteo.url}") String baseUrl,
            Retrofit.Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .build()
                .create(OpenMeteoApi.class);
    }
}
