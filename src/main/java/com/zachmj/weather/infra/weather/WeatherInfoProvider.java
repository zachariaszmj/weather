package com.zachmj.weather.infra.weather;

import com.zachmj.weather.domain.location.GeoLocation;
import com.zachmj.weather.domain.weather.CurrentWeatherInfo;

import java.util.Optional;

public interface WeatherInfoProvider {
    Optional<CurrentWeatherInfo> getWeatherInfo(GeoLocation geoLocation);
}
