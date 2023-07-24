package com.zachmj.weather.infra.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.reactivex.Observable;
import lombok.Data;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

interface IpLocationApi {

    @GET("json/{ipAddress}")
    @Headers("Accept: application/hal+json")
    Observable<IpLocationApi.GeoLocationDto> getGeoLocationInfo(@Path("ipAddress") String ipAddress);

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    class GeoLocationDto {
        @JsonProperty("lat")
        private double latitude;
        @JsonProperty("lon")
        private double longitude;
    }

}
