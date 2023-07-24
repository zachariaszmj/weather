package com.zachmj.weather.infra.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.reactivex.Observable;
import lombok.Data;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.Date;

interface OpenMeteoApi {

    @GET("forecast")
    Observable<WeatherInfoDto> getWeatherInfo(@Query("latitude") double latitude,
                                              @Query("longitude") double longitude,
                                              @Query("current_weather") boolean current,
                                              @Query("temperature_unit") String unit);

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    class WeatherInfoDto {
        @JsonProperty("latitude")
        private double latitude;
        @JsonProperty("longitude")
        private double longitude;
        @JsonProperty("current_weather")
        private CurrentWeatherDto currentWeatherDto;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    class CurrentWeatherDto {
        @JsonProperty("temperature")
        private double temperature;
        @JsonProperty("windspeed")
        private double windSpeed;
        @JsonProperty("winddirection")
        private double windDirection;
        @JsonProperty("time")
        private String time;
    }


}
