package com.zachmj.weather.infra.http;

public class CannotGetCurrentWeatherInfo extends RuntimeException {
    public CannotGetCurrentWeatherInfo(String message) {
        super(message);
    }
}
