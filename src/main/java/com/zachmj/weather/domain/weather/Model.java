package com.zachmj.weather.domain.weather;

import java.time.LocalDateTime;
import java.util.Date;

public final class Model {
    private Model() {
    }

    public record Temperature(double value, TemperatureUnit unit) {
    }


    public record Wind(double speed, double direction) {
    }

    public record Weather(Temperature temperature, Wind wind, String timestamp) {
    }

}
