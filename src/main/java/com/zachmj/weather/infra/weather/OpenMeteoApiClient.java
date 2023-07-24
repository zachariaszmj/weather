package com.zachmj.weather.infra.weather;

import com.zachmj.weather.domain.location.GeoLocation;
import com.zachmj.weather.domain.weather.CurrentWeatherInfo;
import com.zachmj.weather.infra.utils.RetrofitUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.HttpException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
class OpenMeteoApiClient {

    @Value("${retrofit.retry.default-attempts:3}")
    private int retryAttempts;
    @Value("${open-meteo.timeout-millis:1000}")
    private int timeout;
    private final OpenMeteoApi openMeteoApi;

    public Optional<CurrentWeatherInfo> getWeatherInfo(GeoLocation geoLocation) {
        try {
            OpenMeteoApi.WeatherInfoDto weatherInfoDto = this.openMeteoApi
                    .getWeatherInfo(geoLocation.latitude(), geoLocation.longitude(), true, "celsius")
                    .firstOrError()
                    .retry(RetrofitUtils.bailOnDurableErrorPredicate(retryAttempts))
                    .timeout(timeout, TimeUnit.MILLISECONDS)
                    .blockingGet();
            return Optional.of(map(weatherInfoDto));
        } catch (HttpException ex) {
            return RetrofitUtils.on404Return(ex, Optional.empty());
        }
    }

    private CurrentWeatherInfo map(OpenMeteoApi.WeatherInfoDto dto) {
        return new CurrentWeatherInfo(
                dto.getCurrentWeatherDto().getTemperature(),
                dto.getCurrentWeatherDto().getWindSpeed(),
                dto.getCurrentWeatherDto().getWindDirection(),
                dto.getCurrentWeatherDto().getTime()
        );
    }
}
