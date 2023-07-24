package com.zachmj.weather.infra.weather;

import com.zachmj.weather.domain.location.GeoLocation;
import com.zachmj.weather.domain.weather.CurrentWeatherInfo;
import com.zachmj.weather.infra.config.GlobalCacheConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
class WeatherInfoCacheSync implements WeatherInfoProvider {

    private final CacheManager cacheManager;
    private final OpenMeteoApiClient openMeteoApiClient;

    @Value("${open-meteo.expiration-time:600}")
    private int expirationTime;


    public Optional<CurrentWeatherInfo> getWeatherInfo(GeoLocation geoLocation) {
        log.debug("Getting current weather info by geo location {}", geoLocation);
        Cache cache = cacheManager.getCache(GlobalCacheConfig.WEATHER);
        CurrentWeatherInfo currentWeatherInfo = cache.get(geoLocation, CurrentWeatherInfo.class);
        if (currentWeatherInfo == null) {
            return handleFirstCall(geoLocation, cache);
        } else {
            return returnCachedValueOrTryRefreshIfExpired(geoLocation, cache, currentWeatherInfo);
        }
    }

    private Optional<CurrentWeatherInfo> returnCachedValueOrTryRefreshIfExpired(GeoLocation geoLocation, Cache cache, CurrentWeatherInfo weatherInfo) {
        LocalDateTime now = LocalDateTime.now();
        if (Duration.between(now, LocalDateTime.parse(weatherInfo.getTime())).getSeconds() > expirationTime) {
            try {
                Optional<CurrentWeatherInfo> newWeatherInfo = openMeteoApiClient.getWeatherInfo(geoLocation);
                cache.evict(geoLocation);
                cache.put(geoLocation, newWeatherInfo);
                return newWeatherInfo;
            } catch (Exception exception) {
                return Optional.of(weatherInfo);
            }
        } else {
            return Optional.of(weatherInfo);
        }
    }

    @NotNull
    private Optional<CurrentWeatherInfo> handleFirstCall(GeoLocation geoLocation, Cache cache) {
        try {
            Optional<CurrentWeatherInfo> weatherInfo = openMeteoApiClient.getWeatherInfo(geoLocation);
            weatherInfo.ifPresent(info ->
                    cache.put(geoLocation, info)
            );
            return weatherInfo;
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
