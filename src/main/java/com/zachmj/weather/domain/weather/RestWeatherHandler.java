package com.zachmj.weather.domain.weather;

public interface RestWeatherHandler {

    Model.Weather getCurrentWeather(String ipAddress, String units);
}
