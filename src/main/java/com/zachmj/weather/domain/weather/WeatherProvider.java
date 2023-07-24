package com.zachmj.weather.domain.weather;

import com.zachmj.weather.infra.http.CannotGetCurrentWeatherInfo;
import com.zachmj.weather.infra.location.LocationInfoProvider;
import com.zachmj.weather.infra.weather.WeatherInfoProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Slf4j
@Component
class WeatherProvider implements RestWeatherHandler {

    private final LocationInfoProvider locationInfoProvider;
    private final WeatherInfoProvider weatherInfoProvider;

    @Override
     public Model.Weather getCurrentWeather(String ipAddress, String units) {
        if (StringUtils.isNotBlank(ipAddress)) {
            CurrentWeatherInfo currentWeatherInfo = locationInfoProvider.getGeoLocationByIpAddress(ipAddress)
                    .flatMap(weatherInfoProvider::getWeatherInfo)
                    .orElseThrow(() -> new CannotGetCurrentWeatherInfo("Cannot get current weather for IP address " + ipAddress));
            TemperatureUnit temperatureUnit = TemperatureUnit.valueOf(units.toUpperCase());
            double temperature = currentWeatherInfo.getTemperature(temperatureUnit);
            return new Model.Weather(
                    new Model.Temperature(temperature, temperatureUnit),
                    new Model.Wind(currentWeatherInfo.getWindSpeed(), currentWeatherInfo.getWindDirection()),
                    currentWeatherInfo.getTime()
            );
        }
        throw new IllegalArgumentException("IP address cannot be blank");
    }

}
