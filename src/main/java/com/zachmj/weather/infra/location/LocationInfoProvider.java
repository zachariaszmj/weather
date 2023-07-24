package com.zachmj.weather.infra.location;

import com.zachmj.weather.domain.location.GeoLocation;

import java.util.Optional;

public interface LocationInfoProvider {

    Optional<GeoLocation> getGeoLocationByIpAddress(String ipAddress);
}
