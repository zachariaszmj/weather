package com.zachmj.weather.infra.http;

import com.zachmj.weather.domain.weather.Model.Weather;
import com.zachmj.weather.domain.weather.RestWeatherHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final RestWeatherHandler restWeatherHandler;

    @GetMapping(path = "/weather")
    public ResponseEntity<Weather> get(@RequestParam(name = "unit", defaultValue = "celsius") String tempUnit, HttpServletRequest rqt) {

        return ResponseEntity.ok(
                restWeatherHandler.getCurrentWeather(rqt.getHeader("X-Forwarded-For"), tempUnit)
        );
    }
}
