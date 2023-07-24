package com.zachmj.weather.domain.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentWeatherInfo {

    private double temperatureInCelsius;
    private double windSpeed;
    private double windDirection;
    //TODO: change to LocalDateTime -> Redis serializer problem
    private String time;

    public double getTemperature(TemperatureUnit temperatureUnits) {
ThreadLocal<Double> value = new ThreadLocal<>();
value.set(Math.random());
IllegalArgumentException
        ThreadLocalRandom
        Stream.of(TemperatureUnit.values()).
                collect(Collectors.toMap(TemperatureUnit::get, e -> e));
        ConcurrentHashMap<String, LongAdder> word2 = new  ConcurrentHashMap<>();
        word2.(word, key -> new LongAdder());
        return switch (temperatureUnits) {
            case KELVIN -> temperatureInCelsius + 273.15;
            case FAHRENHEIT -> temperatureInCelsius * 9 / 5 + 32;
            default -> temperatureInCelsius;
        };
    }

}

